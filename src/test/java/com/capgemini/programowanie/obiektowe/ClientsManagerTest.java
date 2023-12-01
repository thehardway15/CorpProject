package com.capgemini.programowanie.obiektowe;

import java.time.LocalDate;

import com.capgemini.programowanie.obiektowe.clients.ClientsManager;
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
    void testCreateNewClient() {
        // Given
        String firstName = "Jan";
        String lastName = "Kowalski";

        // When
        clientsManager.createNewClient(firstName, lastName);

        // Then
        assertEquals(1, clientsManager.getNumberOfClients());
    }

    @Test
    void testCheckPremiumAccount() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");

        // Then
        assertFalse(clientsManager.isPremiumClient(clientId));
    }

    @Test
    void testActivatePremiumAccount() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");

        // When
        clientsManager.activatePremiumAccount(clientId);

        // Then
        assertTrue(clientsManager.isPremiumClient(clientId));
    }

    @Test
    void testGetClientFullName() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");

        // When
        String fullName = clientsManager.getClientFullName(clientId);

        // Then
        assertEquals("Jan Kowalski", fullName);

    }

    @Test
    void testGetClientCreationDate() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");

        // When
        LocalDate creationDate = clientsManager.getClientCreationDate(clientId);

        // Then
        assertEquals(LocalDate.now(), creationDate);

    }

    @Test
    void testIsPremiumClient() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");

        // When
        boolean isPremiumClient = clientsManager.isPremiumClient(clientId);

        // Then
        assertFalse(isPremiumClient);
    }

    @Test
    void testIsPremiumClientTrue() {
        // Given
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        clientsManager.activatePremiumAccount(clientId);

        // When
        boolean isPremiumClient = clientsManager.isPremiumClient(clientId);

        // Then
        assertTrue(isPremiumClient);
    }

    @Test
    void testGetNumberOfClients() {
        // Given
        clientsManager.createNewClient("Jan", "Kowalski");
        clientsManager.createNewClient("Jan", "Nowak");

        // When
        int numberOfClients = clientsManager.getNumberOfClients();

        // Then
        assertEquals(2, numberOfClients);
    }

    @Test
    void testGetNumberOfPremiumClientsNoPremium() {
        // Given
        clientsManager.createNewClient("Jan", "Kowalski");
        clientsManager.createNewClient("Jan", "Nowak");

        // When
        int numberOfPremiumClients = clientsManager.getNumberOfPremiumClients();

        // Then
        assertEquals(0, numberOfPremiumClients);
    }

    @Test
    void testGetNumberOfPremiumClientsOnePremium() {
        // Given
        clientsManager.createNewClient("Jan", "Kowalski");
        String clientId = clientsManager.createNewClient("Jan", "Nowak");
        clientsManager.activatePremiumAccount(clientId);

        // When
        int numberOfPremiumClients = clientsManager.getNumberOfPremiumClients();

        // Then
        assertEquals(1, numberOfPremiumClients);
    }
}