package com.example.batchproject.config;

import com.example.batchproject.listener.PersonaJobListener;
import com.example.batchproject.models.Persona;
import com.example.batchproject.models.PersonaDTO;
import com.example.batchproject.processor.PersonaItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class PersonaJobConfiguration {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    PlatformTransactionManager transactionManager;

    public FlatFileItemReader<PersonaDTO> personaCSVItemReader(String fname) {
        return new FlatFileItemReaderBuilder<PersonaDTO>().name("personaCSVItemReader")
                //.resource(new FileSystemResource(fname))
                .resource(new ClassPathResource("input/personas-1.csv"))
                .linesToSkip(1)
                .delimited()
                .names(new String[] { "id", "first_name", "last_name", "email", "gender", "ip_address" })
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
                .sql("INSERT INTO personas (id, first_name, last_name, email, gender, ip_address) VALUES (:id, :first_name, :last_name, :email, :gender, :ip_address)")
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
    @Bean
    public Job personasJob(PersonaJobListener listener, JdbcBatchItemWriter<Persona>
            personaDBItemWriter) {
        return new JobBuilder("personasJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(importCSV2DBStep(1, "input/personas-1.csv", personaDBItemWriter))
                .build();
    }
}
