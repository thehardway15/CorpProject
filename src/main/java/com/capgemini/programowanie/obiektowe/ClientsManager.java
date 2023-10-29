package com.capgemini.programowanie.obiektowe;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClientsManager implements Clients{

    private final ArrayList<Client> clients;

    public ClientsManager() {
        this.clients = new ArrayList<>();
    }

    @Override
    public String createNewClient(String firstName, String lastName) {
        Client client = new Client(firstName, lastName);
        clients.add(client);
        return client.getId();
    }

    @Override
    public String activatePremiumAccount(String clientId) {
        Client client = clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(ClientNotFoundException::new);
        client.setPremium(true);
        return client.getId();
    }

    @Override
    public String getClientFullName(String clientId) {
        Client client = clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(ClientNotFoundException::new);
        return client.getFirstName() + " " + client.getLastName();
    }

    @Override
    public LocalDate getClientCreationDate(String clientId) {
        Client client = clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(ClientNotFoundException::new);
        return client.getCreateDatetime();
    }

    @Override
    public boolean isPremiumClient(String clientId) {
        Client client = clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(ClientNotFoundException::new);
        return client.isPremium();
    }

    @Override
    public int getNumberOfClients() {
        return clients.size();
    }

    @Override
    public int getNumberOfPremiumClients() {
        return (int) clients.stream()
                .filter(Client::isPremium)
                .count();
    }
}
