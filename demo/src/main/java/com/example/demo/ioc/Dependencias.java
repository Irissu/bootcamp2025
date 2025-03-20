package com.example.catalog.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Dependencias {
    @Bean
    Repository repository (com.example.catalog.ioc.Configuration config, Registro registro) {
        System.err.println("soy el bean");
        return new RepositoryImpl(config, registro);
    }
    @Bean
    Repository repo1(com.example.catalog.ioc.Configuration config, Registro registro) {
        return new RepositoryImpl(config, registro);
    }
    @Bean
    Repository repo2() {
        return new RepositoryMock();
    }
}
