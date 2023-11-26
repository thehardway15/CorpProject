package com.capgemini.programowanie.obiektowe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseManagerTest {

    WarehouseManager warehouseManager;
    ClientsManager clientsManager;
    Client client;
    Client premiumClient;

    @BeforeEach
    void setUp() {
        clientsManager = new ClientsManager();
        String clientId = clientsManager.createNewClient("Jan", "Kowalski");
        String premiumClientId = clientsManager.createNewClient("Jan", "Kowalski");
        clientsManager.activatePremiumAccount(premiumClientId);

        client = clientsManager.getClientById(clientId);
        premiumClient = clientsManager.getClientById(premiumClientId);

        warehouseManager = new WarehouseManager(clientsManager, 10, List.of(SupportedMetalType.GOLD, SupportedMetalType.PLATINUM));
    }

    @Test
    void testAddMetalIngotClientNotFoundException() {
        // Given
        String nonExistingClientId = "non-existing-client-id";

        // Then
        assertThrows(ClientNotFoundException.class, () -> warehouseManager.addMetalIngot(nonExistingClientId, SupportedMetalType.GOLD, 10));
    }

    @Test
    void testAddMetalIngotProhibitedMetalTypeException() {
        // Then
        assertThrows(ProhibitedMetalTypeException.class, () -> warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.GOLD, 10));
    }

    @Test
    void testAddMetalIngotFullWarehouseException() {
        // Then
        assertThrows(FullWarehouseException.class, () -> warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.PLATINUM, SupportedMetalType.PLATINUM.getDensity() * 10 + 1));
    }

    @Test
    void testAddMetalIngot() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(List.of(SupportedMetalType.IRON), warehouseManager.getStoredMetalTypesByClient(client.getId()));
    }

    @Test
    void testAddMetalIngotMultiple() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // Then
        assertEquals(20 / SupportedMetalType.IRON.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
        assertEquals(20, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(List.of(SupportedMetalType.IRON), warehouseManager.getStoredMetalTypesByClient(client.getId()));
    }

    @Test
    void testAddMetalIngotMultipleMetalTypes() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity() + 10 / SupportedMetalType.SILVER.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.SILVER));
        assertEquals(2, storedMetalTypes.size());
        assertTrue(storedMetalTypes.contains(SupportedMetalType.IRON));
        assertTrue(storedMetalTypes.contains(SupportedMetalType.SILVER));
    }

    @Test
    void testAddMetalIngotMultipleClients() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.SILVER, 10);

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
        assertEquals(10 / SupportedMetalType.SILVER.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(premiumClient.getId()));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(premiumClient.getId()).get(SupportedMetalType.SILVER));
        assertEquals(List.of(SupportedMetalType.IRON), warehouseManager.getStoredMetalTypesByClient(client.getId()));
        assertEquals(List.of(SupportedMetalType.SILVER), warehouseManager.getStoredMetalTypesByClient(premiumClient.getId()));
    }

    @Test
    void testGetMetalTypesToMassStoredByClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // Then
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.SILVER));
    }

    @Test
    void testGetMetalTypesToMassStoredByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).size());
    }

    @Test
    void testGetMetalTypesToMassStoredByClientMultipleClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.IRON, 10);

        // Then
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON));
        assertEquals(10, warehouseManager.getMetalTypesToMassStoredByClient(premiumClient.getId()).get(SupportedMetalType.IRON));
    }

    @Test
    void testGetTotalVolumeOccupiedByClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity() + 10 / SupportedMetalType.SILVER.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
    }

    @Test
    void testGetTotalVolumeOccupiedByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
    }

    @Test
    void testGetTotalVolumeOccupiedByClientMultipleClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.IRON, 10);

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
        assertEquals(10 / SupportedMetalType.IRON.getDensity(), warehouseManager.getTotalVolumeOccupiedByClient(premiumClient.getId()));
    }

    @Test
    void testGetStoredMetalTypesByClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertEquals(2, storedMetalTypes.size());
        assertTrue(storedMetalTypes.contains(SupportedMetalType.IRON));
        assertTrue(storedMetalTypes.contains(SupportedMetalType.SILVER));
    }

    @Test
    void testGetStoredMetalTypesByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getStoredMetalTypesByClient(client.getId()).size());
    }

    @Test
    void testGetStoredMetalTypesByClientMultiple() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // Then
        assertEquals(List.of(SupportedMetalType.IRON), warehouseManager.getStoredMetalTypesByClient(client.getId()));
    }

    @Test
    void testGetStoredMetalTypesByClientMultipleClient() {
        // When
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.PLATINUM, 10);

        // Then
        assertEquals(List.of(SupportedMetalType.IRON), warehouseManager.getStoredMetalTypesByClient(client.getId()));
        assertEquals(List.of(SupportedMetalType.PLATINUM), warehouseManager.getStoredMetalTypesByClient(premiumClient.getId()));
    }
}