package com.example.catalogo_sakila.domains.entities.models;

import com.example.catalogo_sakila.domains.entities.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(name = "Actor", description = "Información sobre los actores que participan en las películas del catalogo")
public class ActorDTO {
	@JsonProperty("id")
	private int actorId;
	@JsonProperty("nombre")
	@Schema(description = "Nombre del actor", example = "Alan",  required = true, minLength = 2, maxLength = 45)
	private String firstName;

	@NotBlank // forma actual de hacerlo
	@Size(min = 2, max = 45) // forma actual de hacerlo
	@Schema(description = "Apellidos del actor", example = "Rickman")
	@JsonProperty("apellidos")
	private String lastName;

	public static ActorDTO from(Actor source) {
		return new ActorDTO(
				source.getActorId(), 
				source.getFirstName(), 
				source.getLastName()
				);
	}
	public static Actor from(ActorDTO source) {
		return new Actor(
				source.getActorId(), 
				source.getFirstName(), 
				source.getLastName()
				);
	}
}
