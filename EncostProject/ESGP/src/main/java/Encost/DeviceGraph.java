package Encost;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

/*
 * The DeviceGraph class is where a device dataset is loaded into, it can visualise and calculate the distributivity of the dataset
 */
public class DeviceGraph {
    private static DeviceGraph instance = null;
    private static ArrayList<Device> deviceList = new ArrayList<>();
    private static String encostDataset = "/EncostSmartHomesDatasetBig.txt";

    //encost device types
    private static final List<String> encostDeviceTypes = new ArrayList<>(Arrays.asList(
        "router", "extender", "hub/controller", "lightbulb", "striplighting", "otherlighting",
        "kettle", "toaster", "coffeemaker", "washingmachine/dryer", "refrigerator/freezer", "dishwasher")
    );

    /**
     * private constructor to prevent external instantiation
     */
    private DeviceGraph() { }

    /**
     * Initialise the Device Graph
     */
    public static DeviceGraph getInstance() {
        if (instance == null) {
            instance = new DeviceGraph();
        }
        return instance;
    }

    /**
     * This function loads a dataSet and checks it's valid
     */
    public void loadDataset(){
        clearDeviceList();
        InputStream inputStream = Encost.class.getResourceAsStream(encostDataset);
        try{ BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String lineData = "";
            //read the dataset line by line
            while((lineData = br.readLine()) != null){
                //extract data from the line
                String[] deviceData = lineData.split(",");
                for(int i = 0; i < deviceData.length; i++){
                    deviceData[i] = cleanData(deviceData[i]);
                }
                //if the first data extracted is Device ID
                if(deviceData[0].equals("deviceid")){
                    continue;
                }
                //validate dataset
                if(!checkDataset(deviceData)){
                    clearDeviceList();
                    return;
                }
                //extract device data
                String deviceID = deviceData[0];
                String dateConnected = deviceData[1];
                String deviceName = deviceData[2];
                String deviceType = deviceData[3];
                String householdID = deviceData[4];
                String routerConnection = deviceData[5];
                String sends = deviceData[6];
                String receives = deviceData[7];
                //add device to device list
                Device device = new Device(deviceID, dateConnected, deviceName, deviceType, householdID, routerConnection, sends, receives);
                deviceList.add(device);
            }
            br.close();
            System.out.println("\nDataset successfully loaded\n");
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * This function uses GraphStream to visualise the device dataset to show connections between devices
     */
    public void graphVisualisation(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        //initialise the Graph
        Graph graph = new SingleGraph("DeviceGraph");

        // get the stylesheet from resources
        InputStream inputStream = Encost.class.getResourceAsStream("/stylesheet.css");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String css = scanner.next();
        graph.addAttribute("ui.stylesheet", css);
        scanner.close();

        //insert all router devices into the graph
        for(Device device : deviceList){
            if(device.getDeviceCategory().equals("EncostWifiRouters")){
                //initialise device
                Node node = graph.addNode(device.getDeviceID());
                node.setAttribute("Device", device);

                //set the colour and size of the node to deviceRouter
                Object deviceCategory = getGraphCategory(device);

                //set the shape of the node by checking sends and receives
                Object deviceSendsReceives = getGraphSendsReceives(device);

                //add the attributes to the device node
                node.addAttribute("ui.class", deviceCategory, deviceSendsReceives);
            }
        }

        //insert all non-router devices into the graph
        for(Device device : deviceList){
            if(!device.getDeviceCategory().equals("EncostWifiRouters")){
                //initialise device
                Node node = graph.addNode(device.getDeviceID());
                node.setAttribute("Device", device);

                //set the colour and size of the node by checking category
                Object deviceCategory = getGraphCategory(device);

                //set the shape of the node by checking sends and receives
                Object deviceSendsReceives = getGraphSendsReceives(device);

                //add the attributes to the device node
                node.addAttribute("ui.class", deviceCategory, deviceSendsReceives);

                //add an edge to router connection
                String id = device.getDeviceID();
                String routerConneciton = device.getRouterConnection();
                graph.addEdge(routerConneciton + ":" + id, routerConneciton, id, false);
            }
        }

        //display the graph
        graph.display().setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        //Print the legend to the console
        System.out.printf("%nNode Legend%n");
        System.out.printf("%nColours%n");
        System.out.printf("%-1s %-10s : %30s", " ", "White", "Encost Wifi Routers\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Orange", "Encost Hubs/Controllers\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Blue", "Encost Smart Lighting\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Pink", "Encost Smart Appliances\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Green", "Encost Smart Whiteware\n");
        System.out.printf("%nShape%n");
        System.out.printf("%-1s %-10s : %30s", " ", "Circle", "Sends and Receives\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Diamond", "Only Sends\n");
        System.out.printf("%-1s %-10s : %30s", " ", "Square", "Only Receives\n");
    }

    /**
     * This function calculates the distributivity of devices in the dataset
     * 
     * @param deviceList the list of devices to perform the calculation on
     * @return the counters from each device type and cateogry
     */
    public Map<String,Integer> calculateDeviceDistribution(ArrayList<Device> deviceList){
        Map<String, Integer> counters = new LinkedHashMap<>();

        //Initialise counters for categories and types
        counters.put("EncostWifiRouters", 0);
        counters.put("router", 0);
        counters.put("extender", 0);

        counters.put("EncostHub/Controller", 0);
        counters.put("hub/controller", 0);

        counters.put("EncostSmartLighting", 0);
        counters.put("lightbulb", 0);
        counters.put("striplighting", 0);
        counters.put("otherlighting", 0);

        counters.put("EncostSmartAppliance", 0);
        counters.put("kettle", 0);
        counters.put("toaster", 0);
        counters.put("coffeemaker", 0);

        counters.put("EncostSmartWhiteware", 0);
        counters.put("washingmachine/dryer", 0);
        counters.put("refrigerator/freezer", 0);    
        counters.put("dishwasher", 0);

        //Initialise a list for insertedDevices
        ArrayList<String> insertedDevices = new ArrayList<>();

        // Iterate through the device list and update the counters
        for (Device device : deviceList) {
            String id = device.getDeviceID();
            String category = device.getDeviceCategory();
            String type = device.getDeviceType();

            //if id OR category OR type is empty, continue to the next device
            if(id.isEmpty() || category.isEmpty() || type.isEmpty()){
                continue;
            }

            //if the device already exists, continue to the next device
            if(insertedDevices.contains(id)){
                continue;
            }
            insertedDevices.add(id);

            //add counters to device type and category
            if(counters.containsKey(category)){
                counters.put(category, counters.get(category) + 1);
            }
            if(counters.containsKey(type)){
                counters.put(type, counters.get(type) + 1);
            }
        }

        return counters;
    }

    /**
     * This function prints the distributivity of devices in the dataset
     * 
     * @param counters the counters from each device type and cateogry
     */
    public void printDeviceDistribution(Map<String, Integer> counters) {
        System.out.printf("%nDevice Distribution%n");
        System.out.printf("______________________________________________%n");

        // iterate through the counters and print the values
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            String category = "";
            String type = "";
            String device = entry.getKey();
            int count = entry.getValue();

            // Set category or type depending on key
            switch (device) {
                case "EncostWifiRouters":
                    category = "Encost Wifi Routers";
                    break;
                case "router":
                    type = "Router";
                    break;
                case "extender":
                    type = "Extender";
                    break;
                case "EncostHub/Controller":
                    category = "Encost Hub/Controllers";
                    break;
                case "hub/controller":
                    type = "Hub/Controllers";
                    break;
                case "EncostSmartLighting":
                    category = "Encost Smart Lighting";
                    break;
                case "lightbulb":
                    type = "Light Bulb";
                    break;
                case "striplighting":
                    type = "Strip Lighting";
                    break;
                case "otherlighting":
                    type = "Other Lighting";
                    break;
                case "EncostSmartAppliance":
                    category = "Encost Smart Appliances";
                    break;
                case "kettle":
                    type = "Kettle";
                    break;
                case "toaster":
                    type = "Toaster";
                    break;
                case "coffeemaker":
                    type = "Coffee Maker";
                    break;
                case "EncostSmartWhiteware":
                    category = "Encost Smart Whiteware";
                    break;
                case "washingmachine/dryer":
                    type = "Washing Machine/Dryer";
                    break;
                case "refrigerator/freezer":
                    type = "Refrigerator/Freezer";
                    break;
                case "dishwasher":
                    type = "Dishwasher";
                    break;
            }
        
            // Print the output
            if (!category.isEmpty()) {
                System.out.printf("%-30s %-10s | %-4s %n", "", "", "");
                System.out.printf("%-30s %-10s | %-4d %n", category, "", count);
            } else {
                System.out.printf("%-5s %-35s | %-4d %n", "", type, count);
            }
        }
        System.out.println();
    }

    /**
     * This function checks the dataset is valid
     *
     * @param deviceData Extracted Data from Dataset
     * @return boolean for if dataSet is valid
     */
    public boolean checkDataset(String[] deviceData){
        //if format is invalid (length of 8) then return false
        if(deviceData.length != 8){
            System.out.println("\nDataset unsuccessfully loaded " + " due to invalid format");
            return false;
        }
        //check the Date Connected is valid
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(deviceData[1]);
        } catch (ParseException e) {
            System.out.println("\nDataset unsuccessfully loaded " + " due to Date Connected being invalid");
            return false;
        }
        //if device type isn't an encost device type then return false
        boolean validType = false;
        for(String type : encostDeviceTypes){
            if(deviceData[3].equals(type)){
                validType = true;
            }
        }
        if(!validType){
            System.out.println("\nDataset unsuccessfully loaded" + " due to Device Type being invalid");
            return false;
        }
        //if Sends and Receives is invalid
        if(!deviceData[6].equals("yes") && !deviceData[6].equals("no")){
            System.out.println("\nDataset unsuccessfully loaded" + "due to Sends being invalid");
            return false;
        }
        if(!deviceData[7].equals("yes") && !deviceData[7].equals("no")){
            System.out.println("\nDataset unsuccessfully loaded" + "due to Receives being invalid");
            return false;
        }
        return true;
    }

    /**
     * This function gets the style category for the device
     *
     * @param device The device we are setting the style for
     * @return the object for GraphStream
     */
    public Object getGraphCategory(Device device){

        //set the colour and size of the node by checking category
        Object deviceCategory = "";
        String category = device.getDeviceCategory();

        //switch case for setting style class by category
        switch (category) {
            case "EncostWifiRouters":
                deviceCategory = "deviceRouter";
                break;
            case "EncostHub/Controller":
                deviceCategory = "deviceHubController";
                break;
            case "EncostSmartLighting":
                deviceCategory = "deviceLighting";
                break;
            case "EncostSmartAppliance":
                deviceCategory = "deviceAppliance";
                break;
            case "EncostSmartWhiteware":
                deviceCategory = "deviceWhiteware";
                break;
        }

        return deviceCategory;
    }

    /**
     * This function gets the style sendsReceives for the device
     *
     * @param device The device we are setting the style for
     * @return the object for GraphStream
     */
    public Object getGraphSendsReceives(Device device){

        //set the shape of the node by checking sends and receives
        Object deviceSendsReceives = "";
        if(device.getDeviceSends() && device.getDeviceReceives()){
            //if Device can send and recieve
            deviceSendsReceives = "deviceSendsReceives";

        } else if(device.getDeviceSends()){
            //if Device can only send
            deviceSendsReceives = "deviceSends";

        } else if(device.getDeviceReceives()){
            //if Device can only receives
            deviceSendsReceives = "deviceReceives";

        }
        return deviceSendsReceives;
    }

    /**
     * This function gets the deviceList
     *
     * @return deviceList
     */
    public ArrayList<Device> getDeviceList(){
        return deviceList;
    }

    /**
     * This function clears the deviceList
     */
    public void clearDeviceList(){
        deviceList.clear();
    }

    /**
     * This function cleans any data by removing spaces and 
     *
     * @param data data to be cleaned
     * @return cleaned string
     */
    private static String cleanData(String data) {
        String cleanedData = "";
        cleanedData = data.toLowerCase();
        cleanedData = cleanedData.replace(" ", "");
        return cleanedData;
    }
}
