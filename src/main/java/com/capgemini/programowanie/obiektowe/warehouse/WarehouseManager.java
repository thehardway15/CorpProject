package com.capgemini.programowanie.obiektowe.warehouse;

import com.capgemini.programowanie.obiektowe.clients.Client;
import com.capgemini.programowanie.obiektowe.clients.ClientNotFoundException;
import com.capgemini.programowanie.obiektowe.clients.ClientsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseManager implements Warehouse{

    private final ClientsManager clientsManager;
    private final double volume;
    private final List<SupportedMetalType> onlyForPremiumMetalType;
    private final Map<String, Map<SupportedMetalType, Double>> storedMetalTypesByClient = new HashMap<>(); // <clientId, Map<SupportedMetalType, mass>>

    public WarehouseManager(ClientsManager clientsManager, double volume, List<SupportedMetalType> onlyForPremiumMetalType) {
        this.clientsManager = clientsManager;
        this.volume = volume;
        this.onlyForPremiumMetalType = onlyForPremiumMetalType;
    }

    private  double occupiedStorage() {
        double occupied = 0;

        for (String clientId : storedMetalTypesByClient.keySet()) {
            occupied += getTotalVolumeOccupiedByClient(clientId);
        }

        return occupied;
    }

    @Override
    public void addMetalIngot(String clientId, SupportedMetalType metalType, double mass) throws ClientNotFoundException, ProhibitedMetalTypeException, FullWarehouseException {
        Client client = clientsManager.getClientById(clientId);

        if (!client.isPremium() && this.onlyForPremiumMetalType.contains(metalType)) {
            throw new ProhibitedMetalTypeException("Client with id: " + clientId + " is not premium and cannot store " + metalType);
        }

        double area = mass / metalType.getDensity();

        if (area + occupiedStorage() > volume) {
            throw new FullWarehouseException();
        }

        if (!storedMetalTypesByClient.containsKey(clientId)) {
            storedMetalTypesByClient.put(clientId, new HashMap<>());
        }

        Map<SupportedMetalType, Double> metalTypeToMass = storedMetalTypesByClient.get(clientId);
        metalTypeToMass.put(metalType, metalTypeToMass.getOrDefault(metalType, 0.0) + mass);
        this.storedMetalTypesByClient.put(clientId, metalTypeToMass);
    }

    @Override
    public Map<SupportedMetalType, Double> getMetalTypesToMassStoredByClient(String clientId) {
        if (!storedMetalTypesByClient.containsKey(clientId)) {
            return new HashMap<>();
        }

        return storedMetalTypesByClient.get(clientId);
    }

    @Override
    public double getTotalVolumeOccupiedByClient(String clientId) {
        if (!storedMetalTypesByClient.containsKey(clientId)) {
            return 0;
        }

        double occupied = 0;
        Map<SupportedMetalType, Double> metalTypeToMass = storedMetalTypesByClient.get(clientId);

        for (SupportedMetalType metalType : metalTypeToMass.keySet()) {
            occupied += metalTypeToMass.get(metalType) / metalType.getDensity();
        }

        return occupied;
    }

    @Override
    public List<SupportedMetalType> getStoredMetalTypesByClient(String clientId) {
        if (!storedMetalTypesByClient.containsKey(clientId)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(storedMetalTypesByClient.get(clientId).keySet());
    }
}
