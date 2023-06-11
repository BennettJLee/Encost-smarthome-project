package Encost;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class LoadDatasetTest {

    private static Stream<Arguments> ValidDeviceData() {
        // simulated dataset with valid device data
        return Stream.of(
            Arguments.of("deviceID,01/02/22,deviceName,router,householdID,routerConnection,yes,no"),
            Arguments.of("deviceID,01/10/22,deviceName,router,householdID,-,yes,yes")
        );
    }

    @ParameterizedTest
    @MethodSource("ValidDeviceData")
    @DisplayName("Valid Loading Check Dataset Test")
    public void validCheckDatsetTest(String validDeviceData) {
        // Create an instance of DeviceGraph
        DeviceGraph deviceGraph = DeviceGraph.getInstance();
        //split 'device data' for checkDataset to read
        String[] deviceData = validDeviceData.split(",");

        // boolean result should be returned to indicate the success or failure of login
        boolean result = deviceGraph.checkDataset(deviceData);
        Assertions.assertTrue(result);
    }

    private static Stream<Arguments> InvalidDeviceData() {
        // simulated dataset with invalid device data
        return Stream.of(
            Arguments.of("deviceID,41/14/22,deviceName,router,householdID,routerConnection,yes,no"),
            Arguments.of("deviceID,01/02/22,deviceName,router,householdID,routerConnection,sends,no"),
            Arguments.of("deviceID,01/02/22,deviceName,tv,householdID,routerConnection,yes,no"),
            Arguments.of("deviceID,01/02/22,deviceName,router,householdID,yes,no")
        );
    }

    @ParameterizedTest
    @MethodSource("InvalidDeviceData")
    @DisplayName("Invalid Loading Check Dataset Test")
    public void invalidCheckDatsetTest(String invalidDeviceData) {
        // Create an instance of DeviceGraph
        DeviceGraph deviceGraph = DeviceGraph.getInstance();
        //split 'device data' for checkDataset to read
        String[] deviceData = invalidDeviceData.split(",");

        // boolean result should be returned to indicate the success or failure of login
        boolean result = deviceGraph.checkDataset(deviceData);
        Assertions.assertFalse(result);
    }
}
