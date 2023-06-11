package Encost;

/*
 * The Device class is an object that will be used to create new Devices
 */
public class Device {
    // initialise all Device data
    private String deviceID, dateConnected, deviceName, deviceType, householdID, routerConnection, sends, receives, deviceCategory;
    
     /**
     * This function creates a new device
     *
     * @param deviceID Device ID of New Device
     * @param dateConnected Date Connected of New Device
     * @param devicename Device Name of New Device
     * @param deviceType Device Type of New Device
     * @param householdID Household ID of New Device
     * @param routerConnection Router Connection of New Device
     * @param sends Sends of New Device
     * @param receives Receives Type of New Device
     */
    public Device(String deviceID, String dateConnected, String deviceName, String deviceType, String householdID, String routerConnection, String sends, String receives) {
        this.deviceID = deviceID;
        this.dateConnected = dateConnected;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.householdID = householdID;
        this.routerConnection = routerConnection;
        this.sends = sends;
        this.receives = receives;
        setDeviceCategory(deviceType);
    }

    /**
     * This function gets the category of a device from the type
     *
     * @param deviceType Device Type of New Device
     */
    public void setDeviceCategory(String deviceType) {
        String deviceCategory = "";
        //switch case to set device category
        switch (deviceType) {
            case "router":
            case "extender":
                deviceCategory = "EncostWifiRouters";
                break;
            case "hub/controller":
                deviceCategory = "EncostHub/Controller";
                break;
            case "lightbulb":
            case "striplighting":
            case "otherlighting":
                deviceCategory = "EncostSmartLighting";
                break;
            case "kettle":
            case "toaster":
            case "coffeemaker":
                deviceCategory = "EncostSmartAppliance";
                break;
            case "washingmachine/dryer":
            case "refrigerator/freezer":
            case "dishwasher":
                deviceCategory = "EncostSmartWhiteware";
                break;
            default:
                deviceCategory = "Invalid";
        }
        this.deviceCategory = deviceCategory;
    }

    /**
     * This function returns Device ID
     *
     * @return deviceID
     */
    public String getDeviceID(){
        return deviceID;
    }

    /**
     * This function returns Date Connected
     *
     * @return dateConnected
     */
    public String getDateConnected(){
        return dateConnected;
    }

    /**
     * This function returns Device Name
     *
     * @return deviceName
     */
    public String getDeviceName(){
        return deviceName;
    }

    /**
     * This function returns Device Type
     *
     * @return deviceType
     */
    public String getDeviceType(){
        return deviceType;
    }

    /**
     * This function returns Household ID
     *
     * @return householdID
     */
    public String getHouseholdID(){
        return householdID;
    }

    /**
     * This function returns Router Connection
     *
     * @return routerConnection
     */
    public String getRouterConnection(){
        return routerConnection;
    }

    /**
     * This function returns a boolean depending if sends equal Yes or No
     *
     * @return deviceSends boolean
     */
    public Boolean getDeviceSends(){
        if(sends.equals("yes")){
            return true;
        }
        return false;
    }

    /**
     * This function returns a boolean depending if receives equal Yes or No
     *
     * @return deviceReceives boolean
     */
    public Boolean getDeviceReceives(){
        if(receives.equals("yes")){
            return true;
        }
        return false;
    }

    /**
     * This function returns Device Category
     *
     * @return deviceCategory
     */
    public String getDeviceCategory(){
        return deviceCategory;
    }
}
