// My Auction
// Team 1
// Benchmark Program
// Jordan Grogan, John Wartonick, Wyatt Bobis

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
import java.text.ParseException; //needed by java for database connection and manipulation

public class Benchmark {

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

            // Check credentials
            int iterations = 10;
            benchmark_checkCredentials(myauction, iterations);
            benchmark_checkIfCustomerSellsProducts(myauction, iterations);
            benchmark_getParentCategories(myauction, iterations);
            benchmark_getChildCategories(myauction, iterations);
            benchmark_displayProducts(myauction, iterations);
            benchmark_displayProductsByKeywords(myauction, iterations);
            benchmark_putProductForAuction(myauction, iterations);
            benchmark_bidOnProduct(myauction, iterations);
            benchmark_suggestions(myauction, iterations);
            benchmark_sellProduct(myauction, iterations);
            benchmark_registerUser(myauction, iterations);
            benchmark_updateSystemDate(myauction, iterations);
            benchmark_productStatisticsAll(myauction, iterations);
            benchmark_productStatisticsByCustomer(myauction, iterations);
            benchmark_topKHighestVolumeSubCategories(myauction, iterations);
            benchmark_topKMostActiveBidders(myauction, iterations);
            benchmark_topKMostActiveBuyers(myauction, iterations);

        } catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
        } finally {
            connection.close();
        }

        reader.close();

    }

    public static void benchmark_checkCredentials(MyAuction myauction, int iterations){
        for(int i=0; i<iterations; i++) {
            System.out.println("Check credentials " + i + ": " + myauction.checkCredentials("jww36", "mypass", false));
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_checkIfCustomerSellsProducts(MyAuction myauction, int iterations){
        for(int i=0; i<iterations; i++) {
            System.out.println("Check if customer sells products " + i + ": " + myauction.checkIfCustomerSellsProducts("jww36"));
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_getParentCategories(MyAuction myauction, int iterations){
        for(int i=0; i<iterations; i++) {
            System.out.println("Check get parent categories " + i + ": " + myauction.getParentCategories());
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_getChildCategories(MyAuction myauction, int iterations){
        for(int i=0; i<iterations; i++) {
            System.out.println("Check get child categories " + i + ": " + myauction.getChildCategories("Home"));
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_displayProducts(MyAuction myauction, int iterations){
        for(int i=0; i<iterations; i++) {
            System.out.print("Check display products " + i + ": " + myauction.displayProducts("Balls",2));
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_displayProductsByKeywords(MyAuction myauction, int iterations){
        String[] keywords = new String[]{"kitchen", "sink"};
        for(int i=0; i<iterations; i++) {
            System.out.print("Check display products " + i + ": " + myauction.displayProductsByKeywords(keywords));
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_putProductForAuction(MyAuction myauction, int iterations){
        System.out.println("Running put product for auction.");
        for(int i=0; i<iterations; i++) {
            myauction.addProduct("Test Product", "Test Description", "jog89", "Balls,Equipment", 10, 5);
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_bidOnProduct(MyAuction myauction, int iterations){
    	int bid = 20;
    	System.out.println("Running bid on product.");
    	for(int i = 0; i < iterations; i++){
    		myauction.addBid(2, "jww36", bid++);
    	}
    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_suggestions(MyAuction myauction, int iterations){
    	System.out.println("Running suggestions.");
    	myauction.suggestions("jog89");
    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_sellProduct(MyAuction myauction, int iterations){
        System.out.println("Running selling product.");
        for(int i = 0; i < iterations; i++){
            System.out.print("Display current products for auction:\n" + myauction.displayCurrentProductsUnderAuction("jog89"));
        }
        for(int i = 0; i < iterations; i++){
            System.out.println("Display second highest bid of #1: " + myauction.displaySecondHighestBid(1));
        }
        for(int i = 0; i < iterations; i++){
            System.out.println("Running withdraw product (auction id #2)");
            myauction.withdrawProduct(2);
        }
        for(int i = 0; i < iterations; i++){
            System.out.println("Testing sell product (auction id #5)");
            myauction.sellProduct(5);
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_registerUser(MyAuction myauction, int iterations){
    	System.out.println("Running register user.");
    	String name = "Test";
    	String address = "Test";
    	String email = "Test";
    	String username = "Test";
    	String password = "Test";
    	for(int i = 0; i < iterations; i++){
    		myauction.registerUser(name, address, email, username + i, password, false);
    	}
    	System.out.println("----------------------------------------------------------------");
    }
    public static void benchmark_updateSystemDate(MyAuction myauction, int iterations){
    	System.out.println("Running update system date.");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_productStatisticsAll(MyAuction myauction, int iterations){
    	System.out.println("Running product statistics all.");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_productStatisticsByCustomer(MyAuction myauction, int iterations){
    	System.out.println("Running product statistics by customer.");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_topKHighestVolumeSubCategories(MyAuction myauction, int iterations){
    	System.out.println("Running top k highest volume sub categories.");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_topKMostActiveBidders(MyAuction myauction, int iterations){
    	System.out.println("Running top k most active bidders.");

    	System.out.println("----------------------------------------------------------------");
    }

    public static void benchmark_topKMostActiveBuyers(MyAuction myauction, int iterations){
    	System.out.println("Running top k most active buyers.");

    	System.out.println("----------------------------------------------------------------");
    }
}
