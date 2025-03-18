package com.example.catalogo_sakila.domains.entities.models;


import org.springframework.beans.factory.annotation.Value;

public interface ActorShort {
	@Value("#{target.ActorId}")
	int getId();
	@Value("#{target.firstName + ' ' + target.lastName}")
	String getNombre();
}
