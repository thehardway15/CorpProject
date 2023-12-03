package com.capgemini.programowanie.obiektowe.clients;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientsManager implements Clients{

    private final List<Client> clients;

    public ClientsManager() {
        this.clients = new ArrayList<>();
    }

    @Override
    public String createNewClient(String firstName, String lastName) {
        String newClientId = UUID.randomUUID().toString();
        LocalDate createdDate = LocalDate.now();

        Client client = new Client(firstName, lastName, newClientId, createdDate);
        clients.add(client);
        return client.getId();
    }

    @Override
    public String activatePremiumAccount(String clientId) {
        return this.getClientById(clientId).setPremium(true).getId();
    }

    @Override
    public String getClientFullName(String clientId) {
        return this.getClientById(clientId).getFullName();
    }

    @Override
    public LocalDate getClientCreationDate(String clientId) {
        return this.getClientById(clientId).getCreateDatetime();
    }

    @Override
    public boolean isPremiumClient(String clientId) {
        return this.getClientById(clientId).isPremium();
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

    public Client getClientById(String clientId) {
        return clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + clientId));
    }
}
