import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*; //import the file containing definitions for the parts
import java.text.ParseException; //needed by java for database connection and manipulation


public class MyAuction {

    private static Scanner reader;
    private static Connection connection; // used to hold the jdbc connection to the DB
    private Statement statement; // used to create an instance of the connection
    private PreparedStatement prepStatement; // used to create a prepared statement, that will be later reused
    private ResultSet resultSet; // used to hold the result of your query (if one exists)
    private String query; // this will hold the query we are using

    public MyAuction() {

        String response;
        String login;
        boolean loggedIn = false;

        System.out.println("Welcome to My Auction!");

        // Ask for admin or customer until a valid response is entered
        do {
            System.out.println("Are you an \"admin\" or a \"customer\"?");
            response = reader.nextLine();
        } while(!(response.equals("admin") || response.equals("customer")));

        // CUSTOMER
        if(response.equals("customer")) {
            System.out.println("Welcome Customer! Please enter your login/username:");
            // Ask for username/password until valid credentials are given
            do {
                login = reader.nextLine();
                System.out.println("Please enter your password:");
                String password = reader.nextLine();
                if (checkCredentials(login, password, false)) {
                    loggedIn = true;
                } else {
                    System.out.println("Sorry, those credentials are invalid. Enter your username:");
                }
            } while(!loggedIn);
            do {
                System.out.println("Customer Main Menu\n" +
                    "Select an option below:\n" +
                    "0 - Quit\n" +
                    "1 - Browse products\n" +
                    "2 - Search for product by text\n" +
                    "3 - Put product for auction\n" +
                    "4 - Bid on product\n" +
                    "5 - Suggestions");
                    if(checkIfCustomerSellsProducts(login)) { // this option only available to customers who sell sell products
                        System.out.println("6 - Sell product");
                    }
                response = reader.nextLine();
                switch (response) {
                    case "1":
                        browseProducts();
                        break;
                    case "2":
                        searchForProductsByText();
                        break;
                    case "3":
                        putProductForAuction();
                        break;
                    case "4":
                        bidOnProduct();
                        break;
                    case "5":
                        suggestions();
                        break;
                    case "6":
                        sellProduct();
                        break;
                }
            } while(!response.equals("0"));

        // ADMIN
        } else if(response.equals("admin")) {
            System.out.println("Welcome Admin! Please enter your login/username:");
            // Ask for username/password until valid credentials are given
            do {
                login = reader.nextLine();
                System.out.println("Please enter your password:");
                String password = reader.nextLine();
                if (checkCredentials(login, password, true)) {
                    loggedIn = true;
                } else {
                    System.out.println("Sorry, those credentials are invalid. Enter your username:");
                }
            } while(!loggedIn);
            do {
                System.out.println("Administrator Main Menu\n" +
                    "Select an option below:\n" +
                    "0 - Quit\n" +
                    "1 - New customer registration\n" +
                    "2 - Update system date\n" +
                    "3 - Product statistics (all products)\n" +
                    "4 - Product statistics (by customer)\n" +
                    "5 - Statistics");
                response = reader.nextLine();
                switch (response) {
                    case "1":
                        newCustomerRegistration();
                        break;
                    case "2":
                        updateSystemDate();
                        break;
                    case "3":
                        productStatisticsAll();
                        break;
                    case "4":
                        productStatisticsByCustomer();
                        break;
                    case "5":
                        statistics();
                        break;
                }
            } while(!response.equals("0"));
        }

    }

    private boolean checkCredentials(String login, String password, boolean isAdmin) {

        boolean authorized = false;

        try {
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

        } catch(SQLException Ex) {
            System.out.println("Error running the  queries.  Machine Error: " +
                    Ex.toString());
        } finally{
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

        return authorized;
    }

    private boolean checkIfCustomerSellsProducts(String login) {

        boolean sellsProducts = false;

        try {
            query = "SELECT * FROM product WHERE seller=?";

            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, login);

            resultSet = prepStatement.executeQuery(); //run the query on the DB table

            if(resultSet.next()) {
                // At least one result! This user sells products!
                sellsProducts = true;
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the  queries.  Machine Error: " +
                    Ex.toString());
        } finally{
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

        return sellsProducts;

    }

    public void browseProducts() {
        String response;
        System.out.println("Which category would you like to browse?");
        // List categories where parent_category = NULL
        ArrayList<String> parentCategories = getParentCategories();
        for (int i = 0; i < parentCategories.size(); i++) {
            System.out.println(i + " - " + parentCategories.get(i));
        }
        response = reader.nextLine();
        System.out.println("Which subcategory would you like to browse?");
        // List categories where parent_category = the category selected
        ArrayList<String> childCategories = getChildCategories(parentCategories.get(Integer.parseInt(response)));
        for(int i = 0; i < childCategories.size(); i++) {
            System.out.println(i + " - " + childCategories.get(i));
        }
        String category = reader.nextLine();
        System.out.println("Would you like the products to be sorted by:\n" +
                "1 - highest bid amount\n" +
                "2 - alphabetically by product name, or\n" +
                "3 - no sorting?");
        response = reader.nextLine();
        // List products with key attributes: auction_id, name, description, highest_bid (if sorted)
        displayProducts(childCategories.get((Integer.parseInt(category))), Integer.parseInt(response));
    }

    private ArrayList<String> getParentCategories() {
        ArrayList<String> parentCategories = new ArrayList<>();

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT * FROM category WHERE parent_category IS NULL";

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                parentCategories.add(resultSet.getString("name"));
            }

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

        return parentCategories;
    }

    private ArrayList<String> getChildCategories(String parent) {
        ArrayList<String> childCategories = new ArrayList<>();

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT * FROM category WHERE parent_category='" + parent + "'"; // concatenating because this value does not come from the user, safe to not sanitize

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                childCategories.add(resultSet.getString("name"));
            }

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

        return childCategories;
    }

    private void displayProducts(String category, int sort) {

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT * FROM product JOIN belongsto ON product.auction_id=belongsto.auction_id WHERE product.status='under auction' AND belongsto.category='" + category + "'";

            if(sort == 1) {
                selectQuery += " ORDER BY amount";
            } else if(sort == 2) {
                selectQuery += " ORDER BY name";
            }

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                int auctionID = resultSet.getInt("auction_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int highestBidAmt = resultSet.getInt("amount");
                System.out.println(auctionID + ": " + name + " - " + description + " - Highest Bid Amount: $" + highestBidAmt);
            }

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    public void searchForProductsByText() {
        // TODO
    }

    public void putProductForAuction() {
        // TODO
    }

    public void bidOnProduct() {
        // TODO
    }

    public void suggestions() {
        // TODO
    }

    public void sellProduct() {
        // TODO
    }

    public void newCustomerRegistration() {
        // TODO
    }

    public void updateSystemDate() {
        // TODO
    }

    public void productStatisticsAll() {
        // TODO
    }

    public void productStatisticsByCustomer() {
        // TODO
    }

    public void statistics() {
        // TODO
    }

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

}
