package com.example.intercorp.dto;

import java.time.LocalDate;

public class ClientDetailDto {
    private String id;
    private String name;
    private String lastname;
    private LocalDate birthDate;
    private LocalDate estimatedDateOfDeath;

    public ClientDetailDto() {
    }

    public ClientDetailDto(String id, String name, String lastname, LocalDate birthDate, LocalDate estimatedDateOfDeath) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.estimatedDateOfDeath = estimatedDateOfDeath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getEstimatedDateOfDeath() {
        return estimatedDateOfDeath;
    }

    public void setEstimatedDateOfDeath(LocalDate estimatedDateOfDeath) {
        this.estimatedDateOfDeath = estimatedDateOfDeath;
    }
}
