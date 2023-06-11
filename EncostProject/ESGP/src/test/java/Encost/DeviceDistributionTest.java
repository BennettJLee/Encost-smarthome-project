package Encost;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DeviceDistributionTest {

    @Test
    @DisplayName("Branch Coverage Test For CalculateDeviceDistribution")
    public void testCalculateDeviceDistribution() {
        // Create an instance of DeviceGraph
        DeviceGraph deviceGraph = DeviceGraph.getInstance();
        //Lists for Expected and Branch
        ArrayList<Device> deviceListExpected = new ArrayList<>(Arrays.asList(
            new Device("1", "10/10/22", "1", "router", "householdID", "routerConnection", "yes", "no"), 
            new Device("2", "10/10/22", "2", "extender", "householdID", "routerConnection", "yes", "no"), 
            new Device("3", "10/10/22", "3", "lightbulb", "householdID", "routerConnection", "yes", "no"), 
            new Device("4", "10/10/22", "4", "striplighting", "householdID", "routerConnection", "yes", "no"), 
            new Device("5", "10/10/22", "5", "kettle", "householdID", "routerConnection", "yes", "no"), 
            new Device("6", "10/10/22", "6", "toaster", "householdID", "routerConnection", "yes", "no") 
        ));
        ArrayList<Device>deviceListBranch = new ArrayList<>(Arrays.asList(
            new Device("1", "10/10/22", "1", "router", "householdID", "routerConnection", "yes", "no"), 
            new Device("2", "10/10/22", "2", "extender", "householdID", "routerConnection", "yes", "no"), 
            new Device("3", "10/10/22", "3", "lightbulb", "householdID", "routerConnection", "yes", "no"), 
            new Device("4", "10/10/22", "4", "striplighting", "householdID", "routerConnection", "yes", "no"), 
            new Device("5", "10/10/22", "5", "kettle", "householdID", "routerConnection", "yes", "no"), 
            new Device("6", "10/10/22", "6", "toaster", "householdID", "routerConnection", "yes", "no"), 
            new Device("5", "10/10/22", "5", "kettle", "householdID", "routerConnection", "yes", "no"), 
            new Device("", "10/10/22", "", "", "householdID", "routerConnection", "yes", "no") 
        ));
        // Get the expected and Branch counters
        Map<String, Integer> countersExpected = deviceGraph.calculateDeviceDistribution(deviceListExpected);
        Map<String, Integer> countersBranch = deviceGraph.calculateDeviceDistribution(deviceListBranch);

        //Compare the counters
        Assertions.assertEquals(countersExpected, countersBranch);
    }
}
