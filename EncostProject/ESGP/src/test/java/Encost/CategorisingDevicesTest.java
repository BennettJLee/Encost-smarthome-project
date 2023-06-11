package Encost;

import org.junit.Test;
import org.junit.jupiter.api.*;

public class CategorisingDevicesTest {

    @Test
    @DisplayName("Valid Device Type Test (router) For Categorising User Type")
    public void routerCategoriseDeviceValid() {
        // Encost is the main class where the user check exists
        Device device = new Device("deviceID", "01/02/22", "deviceName", "router", "householdID", "routerConnection", "yes", "no");
        String deviceType = "router";

        // The result of of setDeviceCategory should match the expected category
        device.setDeviceCategory(deviceType);
        String real = device.getDeviceCategory();
        String expected = "EncostWifiRouters";
        Assertions.assertEquals(real, expected);
    }

    @Test
    @DisplayName("Valid Device Type Test (toaster) For Categorising User Type")
    public void toasterCategoriseDeviceValid() {
        // Encost is the main class where the user check exists
        Device device = new Device("deviceID", "01/02/22", "deviceName", "toaster", "householdID", "routerConnection", "yes", "no");
        String deviceType = "toaster";

        // The result of of setDeviceCategory should match the expected category
        device.setDeviceCategory(deviceType);
        String real = device.getDeviceCategory();
        String expected = "EncostSmartAppliance";
        Assertions.assertEquals(real, expected);
    }

    @Test
    @DisplayName("Invalid Device Type Test (non-Encost device) For Categorising User Type")
    public void tvCategoriseDeviceInvalid() {
        // Encost is the main class where the user check exists
        Device device = new Device("deviceID", "01/02/22", "deviceName", "tv", "householdID", "routerConnection", "yes", "no");
        String deviceType = "tv";

        // The result of of setDeviceCategory should match the expected category
        device.setDeviceCategory(deviceType);
        String real = device.getDeviceCategory();
        String expected = "Invalid";
        Assertions.assertEquals(real, expected);
    }

    @Test
    @DisplayName("Invalid Device Type Test (fake device) For Categorising User Type")
    public void deviceCategoriseDeviceInvalid() {
        // Encost is the main class where the user check exists
        Device device = new Device("deviceID", "01/02/22", "deviceName", "device", "householdID", "routerConnection", "yes", "no");
        String deviceType = "device";

        // The result of of setDeviceCategory should match the expected category
        device.setDeviceCategory(deviceType);
        String real = device.getDeviceCategory();
        String expected = "Invalid";
        Assertions.assertEquals(real, expected);
    }
}
