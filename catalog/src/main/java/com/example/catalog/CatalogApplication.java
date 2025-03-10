package com.example.catalog;

import com.example.catalog.ioc.Repository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.catalog.ioc.Configuration;
import com.example.catalog.ioc.Service;
import com.example.catalog.ioc.Rango;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.ioc")
@ComponentScan(basePackages = {"com.example.catalog.ioc"})
public class CatalogApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

//	@Autowired //(required = false)
//	Servicio srv;

	@Autowired //(required = false)
//	@Qualifier("verdad")
	Repository repo1;
	@Autowired //(required = false)
//	@Qualifier("mentira")
	Repository repo2;
//	@Autowired //(required = false)
//	Repositorio repo;

	@Value("${mi.valor:valor por defecto}")
	String valor;

	@Autowired
	Rango rango;

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
		ejemplosIOC();
	}

	private void ejemplosIOC() {
		//Servicio srv = new Servicio(new Repositorio(new Configuracion()));
		//AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

//		srv.guardar();
//		repo.guardar();
		repo1.guardar();
		repo2.guardar();
		System.err.println("Valor: " + valor);
		System.err.println("Rango: " + rango);
	}

//	@Bean
//  	CommandLineRunner demo() {
//		return (args) -> {
//			System.err.println("Aplicacion arrancada");
//		};
//	}
}
