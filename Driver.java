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
            MyAuction myauction = new MyAuction();


        } catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
        } finally {
            connection.close();
        }

        reader.close();

    }

    // public void test_MyAuction_Constructor(){
    // 	auction = new MyAuction();
    // }
    public void test_checkCredentials_Customer(){
    	boolean expectedT = true;
    	boolean expectedF = false;
    	boolean result;
    	// test valid login
    	result = auction.checkCredentials("jww36", "mypass", false);
    	if(result == expectedT) System.out.println("Valid login: Test Passed");
    	else System.out.println("Test Failed");
    	// test invalid login
    	result = auction.checkCredentials("abc123", "password", false);
    	if(result == expected) System.out.prntln("Invalid Login: Test Passed");
    	else System.out.println("Test Failed");

    	result = auction.checkCredentials()
    }
}
