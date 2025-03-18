package com.example.catalogo_microservicios;


import com.example.catalogo_microservicios.domains.contracts.repositories.ActorRepository;
import com.example.catalogo_microservicios.domains.contracts.repositories.LanguageRepository;
import com.example.catalogo_microservicios.domains.entities.Actor;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootApplication
@EntityScan(basePackages = "com.example.catalogo_microservicios.domains.entities")
public class CatalogoMicroserviciosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoMicroserviciosApplication.class, args);
	}

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada");
		var actor1 = new Actor(0, "LUIS", "Zahera");
		if(actor1.isValid()) {
			actorRepository.save(actor1);
		} else {
			System.err.println(actor1.getErrorsMessage());
		}
		ejemploDatos();
	}

	private void ejemploDatos() {
		 //actorRepository.findAll().forEach(System.err::println);
		 actorRepository.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(System.err::println);
		 languageRepository.findByNameStartingWith("M").forEach(System.err::println);
		 languageRepository.findTop3By().forEach(System.err::println);
		// actorRepository.findAll().forEach(o-> System.err.println(ActorDTO.from(o)));
		actorRepository.findAll(PageRequest.of(1, 10, Sort.by("actorId"))).forEach(System.err::println);


	}
}
