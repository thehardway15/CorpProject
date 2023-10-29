package com.capgemini.programowanie.obiektowe;

public class Main {
    public static void main(String[] args) {
        ClientsManager clientsManager = new ClientsManager();
        String clientId = clientsManager.createNewClient("John", "Doe");
        System.out.println("Client id: " + clientId);

        String clientFullName = clientsManager.getClientFullName(clientId);
        System.out.println("Client full name: " + clientFullName);

        boolean isPremiumClient = clientsManager.isPremiumClient(clientId);
        System.out.println("Is premium client: " + isPremiumClient);

        int numberOfClients = clientsManager.getNumberOfClients();
        System.out.println("Number of clients: " + numberOfClients);

        int numberOfPremiumClients = clientsManager.getNumberOfPremiumClients();
        System.out.println("Number of premium clients: " + numberOfPremiumClients);

        // Add new premium client
        String premiumClientId = clientsManager.createNewClient("Jane", "Doe");
        clientsManager.activatePremiumAccount(premiumClientId);

        isPremiumClient = clientsManager.isPremiumClient(premiumClientId);
        System.out.println("Is premium client: " + isPremiumClient);

        numberOfClients = clientsManager.getNumberOfClients();
        System.out.println("Number of clients: " + numberOfClients);

        numberOfPremiumClients = clientsManager.getNumberOfPremiumClients();
        System.out.println("Number of premium clients: " + numberOfPremiumClients);

        // Check if the client exists
        String nonExistingClientId = "non-existing-client-id";
        try {
            clientsManager.getClientFullName(nonExistingClientId);
        } catch (ClientNotFoundException e) {
            System.out.println("Client not found");
        }

        try {
            clientsManager.isPremiumClient(nonExistingClientId);
        } catch (ClientNotFoundException e) {
            System.out.println("Client not found");
        }
    }
}