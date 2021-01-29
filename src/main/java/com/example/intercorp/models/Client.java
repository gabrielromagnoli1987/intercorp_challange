package com.example.intercorp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;

@Document
public class Client {

    @Id
    private String id;
    private String name;
    private String lastname;
    private LocalDate birthDate;

    public Client() {
    }

    public Client(String name, String lastname, LocalDate birthDate) {
        this.name = name;
        this.lastname = lastname;
        this.birthDate = birthDate;
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

    public Period getAge() {
        // actual date - birthdate = actual age
        return Period.between(this.getBirthDate(), LocalDate.now());
    }
}
