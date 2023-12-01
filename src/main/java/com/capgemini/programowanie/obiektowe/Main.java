package com.capgemini.programowanie.obiektowe;

import com.capgemini.programowanie.obiektowe.clients.ClientNotFoundException;
import com.capgemini.programowanie.obiektowe.clients.ClientsManager;
import com.capgemini.programowanie.obiektowe.warehouse.FullWarehouseException;
import com.capgemini.programowanie.obiektowe.warehouse.ProhibitedMetalTypeException;
import com.capgemini.programowanie.obiektowe.warehouse.SupportedMetalType;
import com.capgemini.programowanie.obiektowe.warehouse.WarehouseManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientsManager clientsManager = new ClientsManager();
        WarehouseManager warehouseManager = new WarehouseManager(clientsManager, 1000, List.of(SupportedMetalType.GOLD, SupportedMetalType.PLATINUM));


        // Add new client
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
        premiumClientId = clientsManager.activatePremiumAccount(premiumClientId);

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
            System.out.println(e.getMessage());
        }

        try {
            clientsManager.isPremiumClient(nonExistingClientId);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Warehouse manual test
        // Client not found
        try {
            warehouseManager.addMetalIngot(nonExistingClientId, SupportedMetalType.GOLD, 10);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Prohibited metal type
        try {
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.GOLD, 10);
        } catch (ProhibitedMetalTypeException e) {
            System.out.println("Prohibited metal type: " + e.getMessage());
        }

        // Full warehouse
        try {
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 1000000000);
        } catch (FullWarehouseException e) {
            System.out.println("Full warehouse");
        }

        // Add metal ingots
        try {
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
            warehouseManager.addMetalIngot(clientId, SupportedMetalType.SILVER, 10);
        } catch (Exception e) {
            System.out.println("Unexpected exception:" + e.getMessage());
        }

        // Check if the metal ingots were added
        System.out.println("Metal types to mass stored by client: " + warehouseManager.getMetalTypesToMassStoredByClient(clientId));

        // Check if the total volume occupied by client is correct
        System.out.println("Total volume occupied by client: " + warehouseManager.getTotalVolumeOccupiedByClient(clientId));

        // Check if the stored metal types by client are correct
        System.out.println("Stored metal types by client: " + warehouseManager.getStoredMetalTypesByClient(clientId));

        // Add metal premium ingots
        try {
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.GOLD, 1000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.GOLD, 1000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.GOLD, 1000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.GOLD, 1000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.PLATINUM, 10000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.PLATINUM, 10000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.PLATINUM, 10000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.PLATINUM, 10000);
            warehouseManager.addMetalIngot(premiumClientId, SupportedMetalType.IRON, 30000);
        } catch (Exception e) {
            System.out.println("Unexpected exception:" + e.getMessage());
        }

        // Check if the metal ingots were added
        System.out.println("Metal types to mass stored by client: " + warehouseManager.getMetalTypesToMassStoredByClient(premiumClientId));

        // Check if the total volume occupied by client is correct
        System.out.println("Total volume occupied by client: " + warehouseManager.getTotalVolumeOccupiedByClient(premiumClientId));

        // Check if the stored metal types by client are correct
        System.out.println("Stored metal types by client: " + warehouseManager.getStoredMetalTypesByClient(premiumClientId));
    }
}