package com.example.batchproject.config;

import com.example.batchproject.listener.PersonaJobListener;
import com.example.batchproject.models.Persona;
import com.example.batchproject.models.PersonaDTO;
import com.example.batchproject.processor.PersonaItemProcessor;
import com.example.batchproject.tasklet.FTPLoadTasklet;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PersonaJobConfiguration {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    PlatformTransactionManager transactionManager;

    // CSV A DB
    public FlatFileItemReader<PersonaDTO> personaCSVItemReader(String fname) {
        return new FlatFileItemReaderBuilder<PersonaDTO>().name("personaCSVItemReader")
                // .resource(new FileSystemResource(fname))
                .resource(new ClassPathResource(fname))
                .linesToSkip(1)
                .delimited()
                .names(new String[] { "id", "nombre", "apellidos", "correo", "sexo", "ip" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PersonaDTO>() { {
                    setTargetType(PersonaDTO.class);
                }})
                .build();
    }
    @Autowired
    public PersonaItemProcessor personaItemProcessor;
    @Bean
    @DependsOnDatabaseInitialization
    public JdbcBatchItemWriter<Persona> personaDBItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Persona>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO personas VALUES (:id,:nombre,:correo,:ip)")
                .dataSource(dataSource)
                .build();
    }

    Step importCSV2DBStep(int index, String file, JdbcBatchItemWriter<Persona> toDB) {
        return new StepBuilder("importCSV2DBStep" + index, jobRepository)
                .<PersonaDTO, Persona>chunk(10, transactionManager)
                .reader(personaCSVItemReader(file))
                .processor(personaItemProcessor)
                .writer(toDB)
                .build();
    }


//    @Bean
//    public Job personasJob(PersonaJobListener listener, JdbcBatchItemWriter<Persona>
//            personaDBItemWriter) {
//        return new JobBuilder("personasJob", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
//                .start(importCSV2DBStep(1, "input/personas-1.csv", personaDBItemWriter))
//                .build();
//    }

    // XML A DB
    public StaxEventItemReader<PersonaDTO> personaXMLItemReader() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("Persona", PersonaDTO.class);
        marshaller.setAliases(aliases);
        marshaller.setTypePermissions(AnyTypePermission.ANY);
        return new StaxEventItemReaderBuilder<PersonaDTO>()
                .name("personaXMLItemReader")
                .resource(new ClassPathResource("Personas.xml"))
                .addFragmentRootElements("Persona")
                .unmarshaller(marshaller).build();
    }
    @Primary
    @Bean
    public Step importXML2DBStep1(JdbcBatchItemWriter<Persona> personaDBItemWriter) {
        return new StepBuilder("importXML2DBStep1", jobRepository)
                .<PersonaDTO, Persona>chunk(10, transactionManager)
                .reader(personaXMLItemReader())
                .processor(personaItemProcessor)
                .writer(personaDBItemWriter).build();
    }

    // DB A XML
    public StaxEventItemWriter<Persona> personaXMLItemWriter() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("Persona", Persona.class);
        marshaller.setAliases(aliases);
        return new StaxEventItemWriterBuilder<Persona>()
                .name("personaXMLItemWriter")
                .resource(new FileSystemResource("output/outputData.xml"))
                .marshaller(marshaller)
                .rootTagName("Personas")
                .overwriteOutput(true)
                .build();
    }
    @Bean
    public Step exportDB2XMLStep(JdbcCursorItemReader<Persona> personaDBItemReader) {
        return new StepBuilder("exportDB2XMLStep", jobRepository)
                .<Persona, Persona>chunk(100, transactionManager)
                .reader(personaDBItemReader)
                .writer(personaXMLItemWriter())
                .build();
    }
    // TASKLET

    @Bean
    FTPLoadTasklet ftpLoadTasklet(@Value("${input.dir.name:./ftp}") String dir) {
        FTPLoadTasklet tasklet = new FTPLoadTasklet();
        tasklet.setDirectoryResource(new FileSystemResource(dir));
        return tasklet;
    }

    @Bean
    Step copyFilesInDir(FTPLoadTasklet ftpLoadTasklet) {
        return new StepBuilder("copyFilesInDir", jobRepository)
                .tasklet(ftpLoadTasklet, transactionManager)
                .build();
    }


    // JOB

    @Bean
    public Job personasJob(PersonaJobListener listener, JdbcBatchItemWriter<Persona> personaDBItemWriter,
                           Step exportDB2CSVStep,
                           Step importXML2DBStep1,
                           Step exportDB2XMLStep) {
        return new JobBuilder("personasJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(importCSV2DBStep(1, "input/personas-1.csv", personaDBItemWriter)) // CSV a DB
                .next(importXML2DBStep1) // XML a DB
                .next(exportDB2CSVStep) // DB a CSV
                .next(exportDB2XMLStep) // DB a XML
                .build();
    }
}
