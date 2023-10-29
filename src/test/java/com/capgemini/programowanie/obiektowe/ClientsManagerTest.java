package com.capgemini.programowanie.obiektowe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientsManagerTest {

    ClientsManager clientsManager;

    @BeforeEach
    void setUp() {
        clientsManager = new ClientsManager();
    }

    @Test
    void createNewClient() {
        assertEquals(0, clientsManager.getNumberOfClients());
        clientsManager.createNewClient("Jan", "Kowalski");
        assertEquals(1, clientsManager.getNumberOfClients());
    }

    @Test
    void activatePremiumAccount() {
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        assertFalse(clientsManager.isPremiumClient(clientId));
        clientsManager.activatePremiumAccount(clientId);
        assertTrue(clientsManager.isPremiumClient(clientId));
    }

    @Test
    void getClientFullName() {
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        assertEquals("Jan Kowalski", clientsManager.getClientFullName(clientId));
    }

    @Test
    void getClientCreationDate() {
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        assertNotNull(clientsManager.getClientCreationDate(clientId));
    }

    @Test
    void isPremiumClient() {
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        assertFalse(clientsManager.isPremiumClient(clientId));
        clientsManager.activatePremiumAccount(clientId);
        assertTrue(clientsManager.isPremiumClient(clientId));
    }

    @Test
    void getNumberOfClients() {
        assertEquals(0, clientsManager.getNumberOfClients());
        clientsManager.createNewClient("Jan", "Kowalski");
        assertEquals(1, clientsManager.getNumberOfClients());
    }

    @Test
    void getNumberOfPremiumClients() {
        assertEquals(0, clientsManager.getNumberOfPremiumClients());
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        assertEquals(0, clientsManager.getNumberOfPremiumClients());
        clientsManager.activatePremiumAccount(clientId);
        assertEquals(1, clientsManager.getNumberOfPremiumClients());
    }
}