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
            jdbcTemplate.query("SELECT id, nombre, correo, ip FROM personas",
                            (res, row) -> new Persona(res.getLong(1), res.getString(2), res.getString(3), res.getString(4)))
                    .forEach(p -> log.info("Fila: " + p));


        }
        log.info(jobExecution.getStatus().toString());
    }
}
