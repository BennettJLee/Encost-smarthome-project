package Encost;

import java.io.*;
import java.util.*;

//javac -cp "junit-platform-console-standalone-1.9.0.jar" *.java
//java -jar junit-platform-console-standalone-1.9.0.jar -cp .\ -c CategorizingUsersTest

/*
 * Encost Smart Graph Project
 */
public class Encost {

    private static String userType = ""; //usertype for class access
    private static HashMap<String, String> userPassPairs = new HashMap<String, String>(); //hardcoded username and password pairs
    static {
        userPassPairs.put("encostUserA", "password789");
        userPassPairs.put("encostUserB", "password234");
        userPassPairs.put("encostUserC", "password456");
        userPassPairs.put("encostUserD", "password901");
        userPassPairs.put("encostUserE", "password678");
        userPassPairs.put("encostUserF", "password567");
        userPassPairs.put("encostUserG", "password345");
        userPassPairs.put("encorstUserH", "password890");
        userPassPairs.put("encostUserI", "password123");
        userPassPairs.put("encostUserJ", "password012");
        userPassPairs.put("a", "a");
    }
    
    public static void main(String[] args){
        //initialise the reader that will be passed for all functions in the system
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            //continue to loop the project
            while(true){
                setUserType("");
                //prompt categorising user and ESGP login for encost members
                promptCategorisingUser(br);
                 //prompt AccountLogin if user selected encost user, give 3 attempts
                int attempt = 1;
                while(userType.equals("encost-unverified") && attempt < 4){
                    promptAccountLogin(br, attempt);
                    attempt++;
                }
                if(userType.equals("encost-unverified")){
                    continue;
                }
                //prompt the ESGP Features for the appropriate user
                promptFeatureOptions(br);
            }
        }catch(Exception e){
            System.err.println("Error: " + e);
        }
    }

    /**
     * This function prompts the user to select a user type
     *
     * @param br BufferedReader passed from main
     */
    private static void promptCategorisingUser(BufferedReader br){
        try{
            String input = "";
            //once a valid input is given the loop will end
            while(!userType.equals("encost-unverified") && !userType.equals("community")){
                //welcome user
                System.out.println("\n" + "Welcome to the Encost Smart Graph Project");

                DeviceGraph graph = DeviceGraph.getInstance();
                graph.loadDataset();

                //Prompt user with options
                System.out.println("Enter user (1/2):");
                System.out.println("    " + "(1) Community User");
                System.out.println("    " + "(2) Encost User");
                

                //get input
                System.out.print(">> ");
                input = br.readLine();

                //print error statement if input is invalid
                if(!checkUserType(input)){
                    System.out.println("\n" + "Possible inputs are as stated, (1) for Community Users or (2) for Encost Users");
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * This function prompts the user to enter a username and password
     *
     * @param br BufferedReader passed from main
     * @param attempt the login attempt that the user is on
     */
    private static void promptAccountLogin(BufferedReader br, int attempt) {
        try{
            //login prompt
            System.out.println("\n" + "Encost User Login");
            if(attempt == 2){
                System.out.println("Attempt " + attempt);
            }
            if(attempt == 3){
                System.out.println("Final Attempt");
            }

            //prompt username and password
            System.out.print("\n" + "Username: ");
            String username = br.readLine();
            System.out.print("Password: ");
            String password = br.readLine();

            //Check Username and Password is valid
            if(checkLoginPair(username, password)){
                setUserType("encost-verified");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * This function prompts the user with the feature options applicable to the corresponding user type
     *
     * @param br BufferedReader passed from main
     */
    private static void promptFeatureOptions(BufferedReader br) {
        try {
            //continue to loop until a valid input is given
            while(true){
                //print feature options
                System.out.println("\nESGP Features:");
                System.out.println("    " + "(1) Graph Visualisation");
                if(userType.equals("encost-verified")){
                    System.out.println("    " + "(2) Load Custom Dataset");
                    System.out.println("    " + "(3) View Summary Statistics");
                }
                System.out.println("    " + "(X) Exit");

                System.out.print(">> ");
                String input = br.readLine().toLowerCase();

                //print error statement if input is invalid
                if(!checkFeatureOption(input)){
                    if(userType.equals("community")){
                        System.out.println("\nPossible inputs are as stated, (1) for Graph Visualisation or (X) to Exit");
                    }
                    if(userType.equals("encost-verified")){
                        System.out.println("\nPossible inputs are as stated, (1) for Graph Visualisation, (2) for Load Custom Dataset, (3) for View Summary Statistics or (X) to Exit");
                    }
                    continue;
                }

                //if the input corresponds to Graph Visualisation
                if(input.equals("1")){
                    //open GraphStream window
                    DeviceGraph graph = DeviceGraph.getInstance();
                    graph.graphVisualisation();
                }

                //if the input corresponds to Load Custom Dataset
                if(input.equals("2")){
                    //DO NOTHING
                    System.out.println("\nThis function will be available in the next update");
                }

                //if the input corresponds to View Summary Statistics
                if(input.equals("3")){
                    DeviceGraph graph = DeviceGraph.getInstance();
                    ArrayList<Device> deviceList = graph.getDeviceList(); //get the device list to be passed
                    
                    //calculte the device distribution and print it
                    Map<String, Integer> counters = graph.calculateDeviceDistribution(deviceList);
                    graph.printDeviceDistribution(counters);

                }

                //if the input corresponds to Exit
                if(input.equals("x")){
                    break;
                }
            } 
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * This function checks that the input from the user is valid for the user type
     *
     * @param input the input from the user
     * @return whether the input is valid
     */
    public static boolean checkUserType(String input){
        //if the input is 1 or 2 then input is valid
        if(input.equals("1")){
            setUserType("community");
            return true;
        }
        if(input.equals("2")){
            setUserType("encost-unverified");
            return true;
        }
        return false;
    }

    /**
     * This function checks that the username ane password pair from the user is valid for the login
     *
     * @param Username the username from the user
     * @param Password the password from the user
     * @return whether the Username and Password pair is valid
     */
    public static boolean checkLoginPair(String username, String password){
        //read stored each pair
        for (Map.Entry<String, String> entry : userPassPairs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //if pair matches users pair then input is valid
            if (key.equals(username) && value.equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function checks that the input from the user is valid for the feature options
     *
     * @param input the input from the user
     * @return whether the input is valid
     */
    public static boolean checkFeatureOption(String input) {
        //if the input is 1, 2, 3 or x then input is valid
        if(input.equals("1")){
            return true;
        }
        if(userType.equals("encost-verified")){
            if(input.equals("2")){
                return true;
            }
            if(input.equals("3")){
                return true;
            }
        }
        if(input.equals("x")){
            return true;
        }
        return false;
    }

    /**
     * This function sets the User Type 
     *
     * @param userType Device Type of New Device
     */
    public static void setUserType(String newUserType) {
        userType = newUserType;
    }
}

