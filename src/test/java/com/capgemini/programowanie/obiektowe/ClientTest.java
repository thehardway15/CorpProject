package com.capgemini.programowanie.obiektowe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client client;

    @BeforeEach
    void setUp() {
        client = new Client("Jan", "Kowalski");
    }


    @Test
    void getFirstName() {
        assertEquals("Jan", client.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Kowalski", client.getLastName());
    }

    @Test
    void getCreateDatetime() {
        assertEquals(LocalDate.now(), client.getCreateDatetime());
    }

    @Test
    void isPremium() {
        assertFalse(client.isPremium());
        client.setPremium(true);
        assertTrue(client.isPremium());
    }

    @Test
    void getId() {
        assertNotNull(client.getId());
    }

    @Test
    void setPremium() {
        assertFalse(client.isPremium());
        client.setPremium(true);
        assertTrue(client.isPremium());
    }
}