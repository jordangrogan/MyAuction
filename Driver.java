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

        Scanner reader  = new Scanner(System.in);
        Connection connection = null;
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

            test_checkCredentials(myauction);
            test_checkIfCustomerSellsProducts(myauction);
            test_browseProducts(myauction);
            test_browseProducts(myauction);
            test_getParentCategories(myauction);



        } catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
        } finally {
            connection.close();
        }

        reader.close();

    }

    public static void test_checkCredentials(MyAuction myauction){
    	boolean expected = true;
        boolean result;

        result = myauction.checkCredentials("jww36", "mypass", false);
        System.out.println("Expected Output: " + expected + "\tResult Output: " + result);
    }

    public static void test_checkIfCustomerSellsProducts(MyAuction myauction){
		boolean expected = true;
        boolean result;

    	result = myauction.checkIfCustomerSellsProducts("jww36");
        System.out.println("Expected Output: " + expected + "\tResult Output: " + result);
    }

    public static void test_browseProducts(MyAuction myauction){
    	myauction.browseProducts();
    }

    public static void test_getParentCategories(MyAuction myauction){
    	ArrayList<String> expected = new ArrayList<>();
    	ArrayList<String> result;
    	expected.add("Home");
    	expected.add("Sports");

    	result = myauction.getParentCategories();
    	System.out.println("Expected Output: " + expected.toString() + "\tResult Output: " + result.toString());
    }

    public static void test_getChildCategories(MyAuction myauction){
    	ArrayList<String> parentCategories = myauction.getParentCategories();
    	String childCategories;
    	for(int i = 0; i < parentCategories.size(); i++){
    		childCategories = parentCategories.get(i);
    		System.out.println(childCategories.toString());
    	}
    }

    public static void test_displayProducts(MyAuction myauction){
    	myauction.displayProducts();

    }

    public static void test_displayProductsByKeywords(MyAuction myauction){
    	String[] keywords = new String[100];
    	System.out.println("Enter a description of the product \n")
    	int i = 0;
    	while(reader.hasNext())
    	{
         keywords[i] = reader.next();
         i++;
    	}
    	myauction.displayProductsByKeyword(keywords);

    }

    public static void test_searchForProductsByText(MyAuction myauction){
    	myauction.searchForProductsByText();
    }

    public static void test_putProductForAuction(MyAuction myauction){
    	System.out.println("Enter a login name \n");
    	String login = reader.nextLine();
    	myauction.putProductForAuction(login);

    }

    public static void test_bidOnProduct(MyAuction myauction){
    	Sytem.out.println("Enter Bidder Name \n");
    	String bidder = reader.nextLine();
    	myauction.bidOnProduct(bidder);

    }

    public static void test_suggestions(MyAuction myauction){
    	myauction.suggestions();
    }

    public static void test_sellProduct(MyAuction myauction){
    	System.out.println("Enter a login name \n");
    	String login = reader.nextLine();
    	myauction.sellProduct(login);

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
    	if(name.equals("Administrator"))
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

    }

    public static void test_productStatisticsByCustomer(MyAuction myauction){

    }

    public static void test_statistics(MyAuction myauction){

    }

    public static void test_topKHighestVolumeSubCategories(MyAuction myauction){

    }

    public static void test_topKHighestVolumeMainCategories(MyAuction myauction){

    }

    public static void test_topKMostActiveBidders(MyAuction myauction){

    }

}
