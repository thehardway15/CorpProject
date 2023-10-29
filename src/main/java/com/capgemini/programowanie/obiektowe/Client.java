package com.capgemini.programowanie.obiektowe;

import java.time.LocalDate;
import java.util.UUID;

public class Client {

    private final String firstName;
    private final String lastName;
    private final LocalDate createDatetime;

    private boolean isPremium;
    private final String id;

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDatetime = LocalDate.now();
        this.isPremium = false;
        this.id = UUID.randomUUID().toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getCreateDatetime() {
        return createDatetime;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public String getId() {
        return id;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

}
