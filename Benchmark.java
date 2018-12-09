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
        for(int i=0; i<iterations; i++) {
            System.out.println("Running put product for auction.");
            myauction.addProduct("Test Product", "Test Description", "jog89", "Balls,Equipment", 10, 5);
        }
        System.out.println("----------------------------------------------------------------");
    }

}
