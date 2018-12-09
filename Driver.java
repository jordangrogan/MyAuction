// My Auction
// Team 1
// Driver Program
// Jordan Grogan, John Wartonick, Wyatt Bobis

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
import java.text.ParseException; //needed by java for database connection and manipulation

public class Driver {

	// private MyAuction auction;
    private static Scanner reader;
    private static Connection connection; // used to hold the jdbc connection to the DB

    public static void main(String args[]) throws SQLException {

        if(args.length != 2) {
            System.out.println("Include your Oracle username and password as arguments.\n" +
                    "For example, `java MyAuction YOUR_ORACLE_USERNAME YOUR_ORACLE_PASSWORD`");
            System.exit(0);
        }

        reader  = new Scanner(System.in);

        String username, password;
        username = args[0]; //This is your username in oracle
        password = args[1]; //This is your password in oracle

        try {

            // Register the oracle driver.
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

            //This is the location of the database.  This is the database in oracle
            //provided to the class
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

            //create a connection to DB on class3.cs.pitt.edu
            connection = DriverManager.getConnection(url, username, password);
            MyAuction myauction = new MyAuction(connection);

            test_checkCredentials(myauction);
            test_checkIfCustomerSellsProducts(myauction);
            test_getParentCategories(myauction);
            test_getChildCategories(myauction);
            test_displayProducts(myauction);
            test_displayProductsByKeywords(myauction);
            test_putProductForAuction(myauction);
            test_bidOnProduct(myauction);
            test_suggestions(myauction);
            test_sellProduct(myauction);
            test_newCustomerRegisteration(myauction);
            test_registerUser(myauction);
            test_updateSystemDate(myauction);
            test_productStatisticsAll(myauction);
            test_productStatisticsByCustomer(myauction);
            test_statistics(myauction);
            test_topKHighestVolumeSubCategories(myauction);
            test_topKHighestVolumeMainCategories(myauction);
            test_topKMostActiveBidders(myauction);

        } catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
        } finally {
            connection.close();
        }

        reader.close();

    }

    public static void test_checkCredentials(MyAuction myauction){
        boolean result;
        System.out.println("Testing check credentials...");
        result = myauction.checkCredentials("jww36", "mypass", false);
        System.out.println("Expected Output: " + true + "\tResult Output: " + result);
        result = myauction.checkCredentials("dummy", "dummy", false);
        System.out.println("Expected Output: " + false + "\tResult Output: " + result);
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_checkIfCustomerSellsProducts(MyAuction myauction){
        System.out.println("Testing if customer jww36 sells products...");
        boolean result;
    	result = myauction.checkIfCustomerSellsProducts("jww36");
        System.out.println("Expected Output: " + true + "\tResult Output: " + result);
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_getParentCategories(MyAuction myauction){
        System.out.println("Testing get parent categories...");
    	ArrayList<String> expected = new ArrayList<>();
    	ArrayList<String> result;
    	expected.add("Home");
    	expected.add("Sports");
    	result = myauction.getParentCategories();
    	System.out.println("Expected Output: " + expected.toString() + "\tResult Output: " + result.toString());
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_getChildCategories(MyAuction myauction){
        System.out.println("Testing get child categories...");
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> result;
        expected.add("Kitchen");
        result = myauction.getChildCategories("Home");
        System.out.println("Expected Output: " + expected.toString() + "\tResult Output: " + result.toString());
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_displayProducts(MyAuction myauction){
        System.out.println("Testing display products...");
    	String result = myauction.displayProducts("Balls",2);
        System.out.print("Expected Output:\n5: ball - a very bouncy ball - Highest Bid Amount: $3" + "\nResult Output:\n" + result);
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_displayProductsByKeywords(MyAuction myauction){
        System.out.println("Testing search for products...");
    	String[] keywords = new String[]{"kitchen", "sink"};
        System.out.print("Expected Output:\n3: sink - the kitchen sink - Highest Bid Amount: $12" + "\nResult Output:\n" + myauction.displayProductsByKeywords(keywords));
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_putProductForAuction(MyAuction myauction){
        System.out.println("Testing add a product...");
    	myauction.addProduct("Test Product", "Test Description", "jog89", "Balls,Equipment", 10, 5);
        System.out.println("----------------------------------------------------------------");
    }

    public static void test_bidOnProduct(MyAuction myauction){
    	System.out.println("Testing bidding on a product...");
    	myauction.addBid(2, "jww36", 100);
    	System.out.println("----------------------------------------------------------------");
    }

    public static void test_suggestions(MyAuction myauction){
    	System.out.println("Testing suggestions...");
    	myauction.suggestions("jog89");
    	System.out.println("----------------------------------------------------------------");
    }

    public static void test_sellProduct(MyAuction myauction){
    	System.out.println("Testing selling a product...");

    	myauction.sellProduct("jww36");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void test_newCustomerRegisteration(MyAuction myauction){
    	myauction.newCustomerRegistration();
    }

    public static void test_registerUser(MyAuction myauction){
    	boolean isAdmin;
    	System.out.println("Enter your name \n");
    	String name = reader.nextLine();
    	System.out.println("Enter an address \n");
    	String address = reader.nextLine();
    	System.out.println("Enter your email\n");
    	String email = reader.nextLine();
    	System.out.println("Enter your login \n");
    	String login = reader.nextLine();
    	System.out.println("Enter your password \n");
    	String password = reader.nextLine();
    	if(name.equals("Administrator") && login.equals("admin") && password.equals("root") && address.equals("4200 Fifth Ave, Pgh, PA 15260") && email.equals("admin@pitt.edu"))
    	{
 			isAdmin = true;
    	}
    	else
    	{
    		isAdmin = false;
    	}
    	
    	myauction.registerUser(name, address, email, login, password, isAdmin);

    	

    }

    public static void test_updateSystemDate(MyAuction myauction){
    	myauction.updateSystemDate();
    }

    public static void test_productStatisticsAll(MyAuction myauction){
    	myauction.productStatisticsAll();

    }

    public static void test_productStatisticsByCustomer(MyAuction myauction){
    	myauction.productStatisticsByCustomer();

    }

    public static void test_statistics(MyAuction myauction){
    	myauction.statistics();

    }

    public static void test_topKHighestVolumeSubCategories(MyAuction myauction){
    	//I wasnt exactly sure what X was supposed to be in this so I didnt specify in the printout yet
    	System.out.println("Enter an integer value for x \n");
    	int x = reader.nextInt();
    	myauction.topKHighestVolumeSubCategories(x);

    }

    public static void test_topKHighestVolumeMainCategories(MyAuction myauction){
    	//I wasnt exactly sure what X was supposed to be in this so I didnt specify in the printout yet
    	System.out.println("Enter an integer value for x \n");
    	int x = reader.nextInt();
    	myauction.topKHighestVolumeMainCategories(x);

    }

    public static void test_topKMostActiveBidders(MyAuction myauction){
    	//I wasnt exactly sure what X was supposed to be in this so I didnt specify in the printout yet
    	System.out.println("Enter an integer value for x \n");
    	int x = reader.nextInt();
    	myauction.topKMostActiveBidders(x);

    }

}
