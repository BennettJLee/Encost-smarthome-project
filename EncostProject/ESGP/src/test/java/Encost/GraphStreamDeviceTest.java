package Encost;

import org.junit.jupiter.api.*;

public class GraphStreamDeviceTest {
    @Test
    @DisplayName("Router Device is style distinguishable")
    public void routerGraphStreamDevice() {
        // Device to test
        Device device = new Device("deviceID", "01/02/22", "deviceName", "router", "householdID", "routerConnection", "yes", "yes");
        // Create an instance of DeviceGraph
        DeviceGraph deviceGraph = DeviceGraph.getInstance();
        
        // device category style
        Object realCategory = deviceGraph.getGraphCategory(device);
        Object expectedCategory = "deviceRouter";

        // The result of of getGraphCategory should match the expected category
        Assertions.assertEquals(realCategory, expectedCategory);

        // device sends and receives style
        Object realSendsReceives = deviceGraph.getGraphSendsReceives(device);
        Object expectedSendsReceives = "deviceSendsReceives";

        // The result of of getGraphSendsReceives should match the expected SendsReceives 
        Assertions.assertEquals(realSendsReceives, expectedSendsReceives);
    }

    @Test
    @DisplayName("Kettle Device is style distinguishable")
    public void kettleGraphStreamDevice() {
        // Device to test
        Device device = new Device("deviceID", "01/02/22", "deviceName", "kettle", "householdID", "routerConnection", "yes", "no");
        // Create an instance of DeviceGraph
        DeviceGraph deviceGraph = DeviceGraph.getInstance();

        // device category style
        Object realCategory = deviceGraph.getGraphCategory(device);
        Object expectedCategory = "deviceAppliance";

        // The result of of setDeviceCategory should match the expected category
        Assertions.assertEquals(realCategory, expectedCategory);

        // device sends and receives style
        Object realSendsReceives = deviceGraph.getGraphSendsReceives(device);
        Object expectedSendsReceives = "deviceSends";

        // The result of of getGraphSendsReceives should match the expected SendsReceives
        Assertions.assertEquals(realSendsReceives, expectedSendsReceives);
    }
}
