package com.capgemini.programowanie.obiektowe;

import java.time.LocalDate;

public class Client {

    private final String firstName;
    private final String lastName;
    private final LocalDate createDatetime;

    private boolean isPremium;
    private final String id;

    public Client(String firstName, String lastName, String id, LocalDate createDatetime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDatetime = createDatetime;
        this.isPremium = false;
        this.id = id;
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
