import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
import java.text.ParseException; //needed by java for database connection and manipulation


public class MyAuction {

    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using

    public MyAuction() {

        Scanner reader = new Scanner(System.in);
        String response;
        boolean loggedIn = false;

        System.out.println("Welcome to My Auction!");

        do {
            System.out.println("Are you an \"admin\" or a \"customer\"?");
            response = reader.nextLine();
        } while(!(response.equals("admin") || response.equals("customer")));

        if(response.equals("customer")) {
            System.out.println("Welcome Customer! Please enter your login/username:");
            do {
                String login = reader.nextLine();
                System.out.println("Please enter your password:");
                String password = reader.nextLine();
                if (checkCredentials(login, password, false)) {
                    loggedIn = true;
                } else {
                    System.out.println("Sorry, those credentials are invalid. Enter your username:");
                }
            } while(!loggedIn);
            System.out.println("Customer Main Menu\n" +
                    "Select an option below:\n" +
                    "1 - Browse products\n" +
                    "2 - Search for product by text\n" +
                    "3 - Put product for auction\n" +
                    "4 - Bid on product\n" +
                    "5 - Suggestions\n" +
                    "6 - Sell product"); // TODO: this option only available to customers who sell sell products
        } else if(response.equals("admin")) {
            System.out.println("Welcome Admin! Please enter your login/username:");
            do {
                String login = reader.nextLine();
                System.out.println("Please enter your password:");
                String password = reader.nextLine();
                if (checkCredentials(login, password, true)) {
                    loggedIn = true;
                } else {
                    System.out.println("Sorry, those credentials are invalid. Enter your username:");
                }
            } while(!loggedIn);
            System.out.println("Administrator Main Menu\n" +
                    "Select an option below:\n" +
                    "1 - New customer registration\n" +
                    "2 - Update system date\n" +
                    "3 - Product statistics (all products)\n" +
                    "4 - Product statistics (by customer)\n" +
                    "5 - Statistics");
        }

    }

    public boolean checkCredentials(String login, String password, boolean isAdmin) {

        boolean authorized = false;

        try{
            if(isAdmin) {
                query = "SELECT * FROM administrator WHERE login=? AND password=?";
            } else {
                query = "SELECT * FROM customer WHERE login=? AND password=?";
            }

            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, login);
            prepStatement.setString(2, password);

            resultSet = prepStatement.executeQuery(); //run the query on the DB table

            if(resultSet.next()) {
                // User found! Authorize!
                authorized = true;
            }

            resultSet.close();

        }
        catch(SQLException Ex) {
            System.out.println("Error running the  queries.  Machine Error: " +
                    Ex.toString());
        }
        finally{
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

        return authorized;
    }

    public static void main(String args[]) throws SQLException
    {
    /* Making a connection to a DB causes certain exceptions.  In order to handle
	   these, you either put the DB stuff in a try block or have your function
	   throw the Exceptions and handle them later.  For this demo I will use the
	   try blocks */

        String username, password;
        username = args[0]; //This is your username in oracle
        password = args[1]; //This is your password in oracle

        try{

            // Register the oracle driver.
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

            //This is the location of the database.  This is the database in oracle
            //provided to the class
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

            //create a connection to DB on class3.cs.pitt.edu
            connection = DriverManager.getConnection(url, username, password);
            MyAuction myauction = new MyAuction();

        }
        catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
        }
        finally
        {
            connection.close();
        }
    }

}
