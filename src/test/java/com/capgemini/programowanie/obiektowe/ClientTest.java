package com.capgemini.programowanie.obiektowe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    String clientId;
    LocalDate createdDatetmie;

    @BeforeEach
    void setUp() {
        this.clientId = UUID.randomUUID().toString();
        this.createdDatetmie = LocalDate.now();
    }
    @Test
    void testGetFirstName() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        String firstName = client.getFirstName();

        // Then
        assertEquals("Jan", firstName, "Incorrect first name");
    }

    @Test
    void testGetLastName() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        String lastName = client.getLastName();

        // Then
        assertEquals("Kowalski", lastName, "Incorrect last name");

    }

    @Test
    void testGetCreateDatetime() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        LocalDate createDatetime = client.getCreateDatetime();

        // Then
        assertEquals(LocalDate.now(), createDatetime, "Incorrect create datetime");
    }

    @Test
    void testIsPremium() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);
        client.setPremium(true);

        // When
        boolean isPremium = client.isPremium();

        // Then
        assertTrue(isPremium, "Incorrect premium status");
    }

    @Test
    void testGetId() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        String id = client.getId();

        // Then
        assertNotNull(id, "Incorrect id");
    }

    @Test
    void testSetPremiumTrue() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        client.setPremium(true);

        // Then
        assertTrue(client.isPremium(), "Incorrect premium status");
    }

    @Test
    void testSetPremiumFalse() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // When
        client.setPremium(false);

        // Then
        assertFalse(client.isPremium(), "Incorrect premium status");
    }

    @Test
    void testSetPremiumDefault() {
        // Given
        Client client = new Client("Jan", "Kowalski", this.clientId, this.createdDatetmie);

        // Then
        assertFalse(client.isPremium(), "Incorrect premium status");
    }
}