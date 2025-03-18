package com.example.catalogo_microservicios.domains.entities.dtos;

import com.example.catalogo_microservicios.domains.entities.Actor;
import lombok.*;


public class ActorDTO {
    private int actorId;
    private String firstName;
    private String lastName;

    // Constructor vacío
    public ActorDTO() {}

    // Constructor con parámetros
    public ActorDTO(int actorId, String firstName, String lastName) {
        this.actorId = actorId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public int getActorId() {
        return actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Setters
    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public static ActorDTO from(Actor source)  {
        return new ActorDTO(source.getActorId(), source.getFirstName(), source.getLastName());
    }
    public static Actor from(ActorDTO source) {
        return new Actor(source.getActorId(), source.getFirstName(), source.getLastName());
    }
}
