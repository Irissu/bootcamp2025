package com.example.catalogo_microservicios;


import com.example.catalogo_microservicios.domains.contracts.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.catalogo_microservicios.domains.entities")
public class CatalogoMicroserviciosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoMicroserviciosApplication.class, args);
	}

	@Autowired
	private ActorRepository actorRepository;

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada");
		ejemploDatos();
	}

	private void ejemploDatos() {
		 // actorRepository.findAll().forEach(System.err::println);
		 actorRepository.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(System.err::println);
	}
}
