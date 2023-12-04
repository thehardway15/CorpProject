package com.capgemini.programowanie.obiektowe.warehouse;

public class ProhibitedMetalTypeException extends RuntimeException {

    final String clientId;
    final SupportedMetalType metalType;

    public ProhibitedMetalTypeException(String message, String clientId, SupportedMetalType metalType) {
        super(message);
        this.clientId = clientId;
        this.metalType = metalType;
    }
}
