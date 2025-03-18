package com.example.batchproject.listener;

import com.example.batchproject.models.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class PersonaJobListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(PersonaJobListener.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("-------------------> Finalizado");
            jdbcTemplate.query("SELECT id, first_name, last_name, email, ip_address FROM personas",
                            (result, row) -> new Persona(result.getLong(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)))
                    .forEach(p -> log.info("Fila: " + p));

        }
        log.info(jobExecution.getStatus().toString());
    }
}
