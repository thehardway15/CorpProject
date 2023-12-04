package com.capgemini.programowanie.obiektowe.clients;

public class ClientNotFoundException extends RuntimeException {

    final String clientId;
    public ClientNotFoundException(String message, String clientId) {
        super(message);
        this.clientId = clientId;
    }

}
