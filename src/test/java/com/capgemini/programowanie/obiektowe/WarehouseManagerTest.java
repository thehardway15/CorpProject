package com.capgemini.programowanie.obiektowe;

import com.capgemini.programowanie.obiektowe.clients.Client;
import com.capgemini.programowanie.obiektowe.clients.ClientNotFoundException;
import com.capgemini.programowanie.obiektowe.clients.ClientsManager;
import com.capgemini.programowanie.obiektowe.warehouse.FullWarehouseException;
import com.capgemini.programowanie.obiektowe.warehouse.ProhibitedMetalTypeException;
import com.capgemini.programowanie.obiektowe.warehouse.SupportedMetalType;
import com.capgemini.programowanie.obiektowe.warehouse.WarehouseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // When
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertEquals(1, storedMetalTypes.size());
    }

    @Test
    void testAddMetalIngotMultiple() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // When
        Double storedMass = warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).get(SupportedMetalType.IRON);

        // Then
        assertEquals(20, storedMass);
    }

    @Test
    void testAddMetalIngotMultipleMetalTypes() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // When
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertTrue(storedMetalTypes.containsAll(List.of(SupportedMetalType.IRON, SupportedMetalType.SILVER)));
    }

    @Test
    void testAddMetalIngotMultipleClients() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.SILVER, 10);

        // When
        List<SupportedMetalType> storedMetalTypesClient = warehouseManager.getStoredMetalTypesByClient(client.getId());
        List<SupportedMetalType> storedMetalTypesClientPremium = warehouseManager.getStoredMetalTypesByClient(premiumClient.getId());

        // Then
        assertAll(
                () -> assertEquals(List.of(SupportedMetalType.IRON), storedMetalTypesClient),
                () -> assertEquals(List.of(SupportedMetalType.SILVER), storedMetalTypesClientPremium)
        );
    }

    @Test
    void testGetMetalTypesToMassStoredByClient() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // When
        Map<SupportedMetalType, Double> storedMetalTypes = warehouseManager.getMetalTypesToMassStoredByClient(client.getId());

        // Then
        assertAll(
                () -> assertEquals(10, storedMetalTypes.get(SupportedMetalType.IRON)),
                () -> assertEquals(10, storedMetalTypes.get(SupportedMetalType.SILVER))
        );
    }

    @Test
    void testGetMetalTypesToMassStoredByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getMetalTypesToMassStoredByClient(client.getId()).size());
    }

    @Test
    void testGetMetalTypesToMassStoredByClientMultipleClient() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.IRON, 10);

        // When
        Map<SupportedMetalType, Double> storedMetalTypesClient = warehouseManager.getMetalTypesToMassStoredByClient(client.getId());
        Map<SupportedMetalType, Double> storedMetalTypesClientPremium = warehouseManager.getMetalTypesToMassStoredByClient(premiumClient.getId());

        // Then
        assertAll(
                () -> assertEquals(10, storedMetalTypesClient.get(SupportedMetalType.IRON)),
                () -> assertEquals(10, storedMetalTypesClientPremium.get(SupportedMetalType.IRON))
        );
    }

    @Test
    void testGetTotalVolumeOccupiedByClient() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // When
        Double occupied = warehouseManager.getTotalVolumeOccupiedByClient(client.getId());

        // Then
        assertEquals(10 / SupportedMetalType.IRON.getDensity() + 10 / SupportedMetalType.SILVER.getDensity(), occupied);
    }

    @Test
    void testGetTotalVolumeOccupiedByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getTotalVolumeOccupiedByClient(client.getId()));
    }

    @Test
    void testGetTotalVolumeOccupiedByClientMultipleClient() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.IRON, 10);

        // When
        Double occupiedClient = warehouseManager.getTotalVolumeOccupiedByClient(client.getId());
        Double occupiedClientPremium = warehouseManager.getTotalVolumeOccupiedByClient(premiumClient.getId());

        // Then
        assertAll(
                () -> assertEquals(10 / SupportedMetalType.IRON.getDensity(), occupiedClient),
                () -> assertEquals(10 / SupportedMetalType.IRON.getDensity(), occupiedClientPremium)
        );
    }

    @Test
    void testGetStoredMetalTypesByClient() {
        // Gien
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.SILVER, 10);

        // When
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertTrue(storedMetalTypes.containsAll(List.of(SupportedMetalType.IRON, SupportedMetalType.SILVER)));
    }

    @Test
    void testGetStoredMetalTypesByClientEmpty() {
        // Then
        assertEquals(0, warehouseManager.getStoredMetalTypesByClient(client.getId()).size());
    }

    @Test
    void testGetStoredMetalTypesByClientMultiple() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);

        // When
        List<SupportedMetalType> storedMetalTypes = warehouseManager.getStoredMetalTypesByClient(client.getId());

        // Then
        assertEquals(List.of(SupportedMetalType.IRON), storedMetalTypes);
    }

    @Test
    void testGetStoredMetalTypesByClientMultipleClient() {
        // Given
        warehouseManager.addMetalIngot(client.getId(), SupportedMetalType.IRON, 10);
        warehouseManager.addMetalIngot(premiumClient.getId(), SupportedMetalType.PLATINUM, 10);

        // When
        List<SupportedMetalType> storedMetalTypesClient = warehouseManager.getStoredMetalTypesByClient(client.getId());
        List<SupportedMetalType> storedMetalTypesClientPremium = warehouseManager.getStoredMetalTypesByClient(premiumClient.getId());

        // Then
        assertAll(
                () -> assertEquals(List.of(SupportedMetalType.IRON), storedMetalTypesClient),
                () -> assertEquals(List.of(SupportedMetalType.PLATINUM), storedMetalTypesClientPremium)
        );
    }
}