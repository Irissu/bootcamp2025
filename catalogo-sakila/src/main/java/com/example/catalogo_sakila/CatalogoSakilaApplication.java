package com.example.catalogo_sakila;

import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import jakarta.transaction.Transactional;

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableAspectJAutoProxy
@SpringBootApplication
public class CatalogoSakilaApplication implements CommandLineRunner {
	private final Logger log = Logger.getLogger(getClass().getName());

	public static void main(String[] args) {
		SpringApplication.run(CatalogoSakilaApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada...");
		log.info("Aplicación arrancada...");

	}

}
