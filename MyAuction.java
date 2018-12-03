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
    private String login;

    public MyAuction() {

        String response;

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
                        putProductForAuction(login);
                        break;
                    case "4":
                        bidOnProduct(login);
                        break;
                    case "5":
                        suggestions();
                        break;
                    case "6":
                        sellProduct(login);
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

    private void displayProductsByKeywords(String[] keywords) {

        try {

            if(keywords.length == 1) {
                query = "SELECT * FROM product WHERE description LIKE '%' || ? || '%'";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setString(1, keywords[0]);

            } else {
                query = "SELECT * FROM product WHERE description LIKE '%' || ? || '%' AND description LIKE '%' || ? || '%'";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setString(1, keywords[0]);
                prepStatement.setString(2, keywords[1]);
            }
            resultSet = prepStatement.executeQuery(); //run the query on the DB table

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
            System.out.println("Error running the queries. Machine Error: " +
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
        String[] keywordsArr;
        do {
            System.out.println("Enter up to 2 keywords to search by (separated by a space):");
            String keywords = reader.nextLine();
            keywordsArr = keywords.split(" ");
        } while(keywordsArr.length != 1 && keywordsArr.length != 2);
        displayProductsByKeywords(keywordsArr);
    }


    public void putProductForAuction(String login) {
        boolean go = true;
        
        //Product name
        String productName = "";
        while(go == true){
	        System.out.println("Enter a product name: ");
	        productName = reader.nextLine();
        	if(productName.equals("") == false){ go = false;}
        }

        //product description(optional)
        System.out.println("Enter a product description (optional): ");
        String desc = "";
        if(reader.hasNextLine()) {desc = reader.nextLine(); }

        //product categories
        String category1 = "";
        go = true;
        while(go == true){        	
	        System.out.println("Enter an item category: ");
	        category1 = reader.nextLine();

	        ArrayList<String> parentCategories = getParentCategories();
	       	
	        for(int i = 0; i < parentCategories.size(); i++){
	        	ArrayList<String> childCategories = getChildCategories(parentCategories.get(i));
	        	if(childCategories.contains(category1) == true){
	        		go = false;
	        	}	        	
	        }
	        if(go == true){System.out.println("Invalid item category: " + category1);}	    
        }
        go = true;
        String category2 = "";
        while(go == true){        	
	        System.out.println("Enter a second item category(optional): ");
	        category2 = reader.nextLine();
	        // System.out.println("Category 2: "+category);
	        if(category2.equals("")){ break;}

	        ArrayList<String> parentCategories = getParentCategories();
	       	
	        for(int i = 0; i < parentCategories.size(); i++){
	        	ArrayList<String> childCategories = getChildCategories(parentCategories.get(i));
	        	if(childCategories.contains(category2) == true){
	        		go = false;
	        	}	        	
	        }
	        if(go == true){System.out.println("Invalid item category: " + category2);}	    
        }

        String categories_csv = "";
        if(category2.equals("") == true){categories_csv = category1;}
        else{categories_csv = category1 + "," + category2;}

        //starting price
        go = true;
        int price = -1;
        while(go == true){
        	System.out.println("Enter a price (whole number): ");
        	String sPrice = reader.nextLine();
        	if(sPrice.equals("") == false){go = false;}
        	price = Integer.parseInt(sPrice);
        }

        //produt auction days
        go = true;
        String auctDays = "";
        while(go == true){
		    System.out.println("Enter a number of days for auction: ");
		    auctDays = reader.nextLine();
        	if(auctDays.equals("") == false){go = false;}
        }
		int numDays = Integer.parseInt(auctDays);
			
        try{
        	// proc_putProduct (product_name, product_description, seller, categories_csv, min_price, num_days)
        	CallableStatement cStatement = connection.prepareCall("{call proc_putProduct (?, ?, ?, ?, ?, ?)}");
        	cStatement.setString(1, productName);
        	cStatement.setString(2, desc);
        	cStatement.setString(3, login);
        	cStatement.setString(4, categories_csv);
        	cStatement.setInt(5, price);
        	cStatement.setInt(6, numDays);
        	cStatement.execute();
        }catch(SQLException e){
        	System.out.println("Cannot close Statement. Machine error: "+e.toString());
        }

    }

    public void bidOnProduct(String bidder){
        System.out.print("Enter an Auction Id: ");
        int auction_id = reader.nextInt();
        reader.nextLine();

        try{
            query = "SELECT amount FROM ( SELECT amount, DENSE_RANK() OVER (ORDER BY amount DESC) ranking FROM bidlog WHERE auction_id=" + auction_id + " ) WHERE ranking= 1";
            prepStatement = connection.prepareStatement(query);
            resultSet = prepStatement.executeQuery();
            int bid_amount = -1;
            ResultSetMetaData rsltMD = resultSet.getMetaData();
            int colNumber = rsltMD.getColumnCount();

            while(resultSet.next()){
                for(int i = 1; i <= colNumber; i++){
                    if(rsltMD.getColumnName(i).equalsIgnoreCase("amount")){
                        bid_amount = resultSet.getInt(i);
                    }
                }
            }
            if(bid_amount == -1){
                bid_amount = 0;
            }
            System.out.println("The current highest bid for This product is: " + bid_amount);


            System.out.print("Enter an Amount to bid on Auction " + auction_id + ": ");
            int your_amount = reader.nextInt();
            reader.nextLine();
            while(your_amount <= bid_amount){
                System.out.println("The current highest bid for This product is: " + bid_amount);
                System.out.print("Enter an Amount to bid on : " + auction_id);
                your_amount = reader.nextInt();
                reader.nextLine();
            }

            // Gets the dystem date
            java.sql.Date bidTime = null;
            query = "SELECT c_date FROM oursysdate WHERE ROWNUM=1";
            prepStatement = connection.prepareStatement(query);
            resultSet = prepStatement.executeQuery();
            while(resultSet.next()){
                bidTime = resultSet.getDate("c_date");
            }

            // System.out.println("BIDTIME: " + bidTime);
            //add bid to DB
            query = "INSERT INTO Bidlog (auction_id, bidder, bid_time, amount) VALUES (?, ?, ?, ?)";
            
            prepStatement = connection.prepareStatement(query);

            prepStatement.setInt(1, auction_id);
            prepStatement.setString(2, bidder);
            prepStatement.setDate(3, bidTime);
            prepStatement.setInt(4, your_amount);

            prepStatement.executeUpdate();

            System.out.println("Bid Added!");

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
        } 
        finally{
            try {
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }        
    }

    public void suggestions() {

        System.out.println("Here are some products you may want to bid on based on your bidding friends:");

        try {

            statement = connection.createStatement(); //create an instance
            String query = "SELECT DISTINCT product.auction_id, product.name, product.description FROM product JOIN bidlog on product.auction_id = bidlog.auction_id WHERE bidder IN (" +
                    "SELECT DISTINCT bidder as bidding_friend FROM bidlog WHERE auction_id IN (SELECT auction_id FROM bidlog WHERE bidder='" + login + "') AND bidder != '" + login + "' AND product.status = 'under auction')";

            resultSet = statement.executeQuery(query); //run the query on the DB table

            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Auction ID: " + resultSet.getInt("auction_id") + " | " + resultSet.getString("name") + " | " + resultSet.getString("description"));
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    public void sellProduct(String login) {
    	boolean go = true;
    	// this try catch just gets the items with closed status and displays them
        try {
            query = "SELECT * FROM product WHERE seller=? AND status=?";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, login);
            prepStatement.setString(2, "closed");
            resultSet = prepStatement.executeQuery(); //run the query on the DB table

            ResultSetMetaData rsltMD = resultSet.getMetaData();
            int colNumber = rsltMD.getColumnCount();
            int auction_id = -1;
            System.out.println("Current products:");
            while(resultSet.next()){
            	for(int i = 1; i <= colNumber; i++){
                    if(rsltMD.getColumnName(i).equalsIgnoreCase("auction_id")) {
                        auction_id = resultSet.getInt(i);
                        System.out.print(auction_id + "");
                    }
            		if(rsltMD.getColumnName(i).equalsIgnoreCase("name")){
	            		String val = resultSet.getString(i);
	            		System.out.print(" - " + val);
	            	}
	            	if(rsltMD.getColumnName(i).equalsIgnoreCase("status")){
	            		String val = resultSet.getString(i);
	            		System.out.print("  " + val);
	            	}
            	}
	            System.out.println();
            }

            while(go == true){
            	System.out.println("Which product would you like to sell (enter id):");
                String response = reader.nextLine();
                auction_id = Integer.parseInt(response);
                if(response.equals("") == false) go = false;
            }
            resultSet.close();

            //this gets the second highest bid and displays it
            query = "SELECT amount FROM ( SELECT amount, DENSE_RANK() OVER (ORDER BY amount DESC) ranking FROM bidlog WHERE auction_id=" + auction_id + " ) WHERE ranking=2";
            prepStatement = connection.prepareStatement(query);
            resultSet = prepStatement.executeQuery();
            int bid_amount = -1;
            rsltMD = resultSet.getMetaData();
            colNumber = rsltMD.getColumnCount();
            while(resultSet.next()){
                for(int i = 1; i <= colNumber; i++){
                    if(rsltMD.getColumnName(i).equalsIgnoreCase("amount")) bid_amount = resultSet.getInt(i);
                }
            }
            go = true;
            String res = "";
            while(go == true){
                System.out.println("Second highest bid: " + bid_amount);
                System.out.println("Choose an option: \"withdraw\" or \"sell\":");
                res = reader.nextLine();
                if(res.equals("") == false) go = false;                
            }

            if(res.equalsIgnoreCase("Withdraw")){
                //update product status
                query = "UPDATE product SET status='withdrawn' WHERE auction_id=?";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setInt(1, auction_id);
                prepStatement.executeUpdate();
                System.out.println("Product withdrawn.");
            }else if(res.equalsIgnoreCase("Sell")){
                // update DB
                query = "UPDATE product SET status='sold' WHERE auction_id=?";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setInt(1, auction_id);
                prepStatement.executeUpdate();
                System.out.println("Product sold.");
            }
            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the queries.  Machine Error: " +
                    Ex.toString());
        } finally{
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }

    public void newCustomerRegistration() {
        System.out.println("Enter new user's name:");
        String name = reader.nextLine();
        System.out.println("Enter new user's address:");
        String address = reader.nextLine();
        System.out.println("Enter new user's email address:");
        String email = reader.nextLine();
        System.out.println("Enter new user's preferred login name:");
        String login = reader.nextLine();
        System.out.println("Enter new user's password:");
        String password = reader.nextLine();
        String adminResponse;
        boolean isAdmin;
        do {
            System.out.println("Is this new user an administrator (\"yes\" or \"no\":");
            adminResponse = reader.nextLine();
        } while(!(adminResponse.equals("yes") || adminResponse.equals("no")));
        if(adminResponse.equals("yes")) isAdmin = true; else isAdmin = false;
        registerUser(name, address, email, login, password, isAdmin);
    }

    private void registerUser(String name, String address, String email, String login, String password, boolean isAdmin) {
        try {

            if(isAdmin) {
                query = "INSERT INTO Administrator (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES (?, ?, ?, ?, ?)";
            } else {
                query = "INSERT INTO Customer (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES (?, ?, ?, ?, ?)";
            }
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, login);
            prepStatement.setString(2, password);
            prepStatement.setString(3, name);
            prepStatement.setString(4, address);
            prepStatement.setString(5, email);

            prepStatement.executeUpdate();

            System.out.println("User added!");

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }

    public void updateSystemDate() {
        System.out.println("Enter a month (01 = January, 02 = February, etc.):");
        int month = reader.nextInt();
        System.out.println("Enter a day (01, 02, 03, 04, etc.):");
        int day = reader.nextInt();
        System.out.println("Enter a year (2018, 2019, etc.):");
        int year = reader.nextInt();
        System.out.println("Enter an hour (in 24 hour format):");
        int hour = reader.nextInt();
        System.out.println("Enter a minute (00, 01, 02, etc.):");
        int minute = reader.nextInt();
        System.out.println("Enter a second (00, 01, 02, etc.):");
        int second = reader.nextInt();
        updateSystemDate(month, day, year, hour, minute, second);
    }

    private void updateSystemDate(int month, int day, int year, int hour, int minute, int second) {

        try{

            query = "UPDATE oursysdate SET c_date=? WHERE ROWNUM=1";
            prepStatement = connection.prepareStatement(query);

            // This is how you can specify the format for the dates you will use
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM.dd/yyyy kk:mm:ss");
            // This is how you format a date so that you can use the setDate format below
            String userDate = month + "." + day + "/" + year + " " + hour + ":" + minute + ":" + second;
            java.sql.Date date_reg = new java.sql.Date (df.parse(userDate).getTime());

            prepStatement.setDate(1, date_reg);
            prepStatement.executeUpdate();

            System.out.println("System date updated!");

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } catch (ParseException e) {
            System.out.println("Error parsing the date. Machine Error: " +
                    e.toString());
        } finally{
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }

    public void productStatisticsAll() {

        System.out.println("All Products:");

        try{
            statement = connection.createStatement(); //create an instance
            query = "SELECT product.auction_id, product.name, product.status, product.amount, bidlog.bidder FROM product JOIN bidlog ON product.auction_id = bidlog.auction_id AND product.amount = bidlog.amount";

            resultSet = statement.executeQuery(query); //run the query on the DB table

            while (resultSet.next()) {
                int auction_id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String status = resultSet.getString(3);
                int amount = resultSet.getInt(4);
                String bidder = resultSet.getString(5);

                System.out.print(auction_id + " - " + name + " - " + status + " - $" + amount);

                if(!status.equals("sold")) {
                    System.out.println(" - Highest bidder: " + bidder);
                } else {
                    System.out.println("");
                }
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }

    public void productStatisticsByCustomer() {

        System.out.println("What is the seller's login name?");
        String seller = reader.nextLine();

        try{
            query = "SELECT product.auction_id, product.name, product.status, product.amount, bidlog.bidder FROM product JOIN bidlog ON product.auction_id = bidlog.auction_id AND product.amount = bidlog.amount WHERE product.seller=?";
            prepStatement = connection.prepareStatement(query);

            prepStatement.setString(1, seller);

            resultSet = prepStatement.executeQuery(); //run the query on the DB table

            System.out.println(seller + "'s Products:");

            while (resultSet.next()) {
                int auction_id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String status = resultSet.getString(3);
                int amount = resultSet.getInt(4);
                String bidder = resultSet.getString(5);

                System.out.print(auction_id + " - " + name + " - " + status + " - $" + amount);

                if(!status.equals("sold")) {
                    System.out.println(" - Highest bidder: " + bidder);
                } else {
                    System.out.println("");
                }
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    public void statistics() {

        int statistic;

        do {

            System.out.println("Which statistic would you like to run?\n" +
                    "1 - the top k highest volume sub-categories (highest count of products sold)\n" +
                    "2 - the top k highest volume main categories (highest count of products sold)\n" +
                    "3 - the top k most active bidders (highest count of bids placed)\n" +
                    "4 - the top k most active buyers (highest total dollar amount spent)");

            statistic = reader.nextInt();

        } while(statistic != 1 && statistic != 2 && statistic != 3 && statistic != 4);

        System.out.println("How many months back should this statistic go?");
        int x = reader.nextInt();

        switch (statistic) {
            case 1:
                topKHighestVolumeSubCategories(x);
                break;
            case 2:
                topKHighestVolumeMainCategories(x);
                break;
            case 3:
                System.out.println("here 3");
                topKMostActiveBidders(x);
                break;
            case 4:
                topKMostActiveBuyers(x);
                break;
        }

    }

    private void topKHighestVolumeSubCategories(int x) {

        System.out.println("How many top k sub-categories would you like reported? k=");
        int k = reader.nextInt();
        reader.nextLine();

        topKHighestVolumeSubCategories(x, k);

    }

    private void topKHighestVolumeSubCategories(int x, int k) {


        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT name, func_productCount(" + x + ", name) as product_count FROM category WHERE parent_category IS NOT NULL ORDER BY product_count DESC";

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            int count = 0;
            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Category: " + resultSet.getString("name") + " | Number of Products: " + resultSet.getString("product_count"));
                count++;
                if(count == k) break;
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    private void topKHighestVolumeMainCategories(int x) {

        System.out.println("How many top k main categories would you like reported? k=");
        int k = reader.nextInt();
        reader.nextLine();

        topKHighestVolumeMainCategories(x, k);

    }

    private void topKHighestVolumeMainCategories(int x, int k) {

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT name, func_productCount(" + x + ", name) as product_count FROM category WHERE parent_category IS NULL ORDER BY product_count DESC";

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            int count = 0;
            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Category: " + resultSet.getString("name") + " | Number of Products: " + resultSet.getString("product_count"));
                count++;
                if(count == k) break;
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    private void topKMostActiveBidders(int x) {

        System.out.println("How many top k active buyers would you like reported? k=");
        int k = reader.nextInt();
        reader.nextLine();

        topKMostActiveBidders(x, k);

    }

    private void topKMostActiveBidders(int x, int k) {

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT bidder, func_bidCount(" + x + ", bidder) as bid_count FROM bidlog GROUP BY bidder ORDER BY bid_count DESC";

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            int count = 0;
            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Bidder: " + resultSet.getString("bidder") + " | Number of Bids: " + resultSet.getString("bid_count"));
                count++;
                if(count == k) break;
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

    }

    private void topKMostActiveBuyers(int x) {

        System.out.println("How many top k active buyers would you like reported? k=");
        int k = reader.nextInt();
        reader.nextLine();

        topKMostActiveBuyers(x, k);

    }

    private void topKMostActiveBuyers(int x, int k) {

        try {

            statement = connection.createStatement(); //create an instance
            String selectQuery = "SELECT buyer, func_buyingAmount(" + x + ", buyer) as amount FROM product WHERE status='sold' GROUP BY buyer ORDER BY amount DESC";

            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table

            int count = 0;
            while (resultSet.next()) //this not only keeps track of if another record
            //exists but moves us forward to the first record
            {
                System.out.println("Buyer: " + resultSet.getString("buyer") + " | Amount Spent: " + resultSet.getString("amount"));
                count++;
                if(count == k) break;
            }

            resultSet.close();

        } catch(SQLException Ex) {
            System.out.println("Error running the queries.  Machine Error: " +
                    Ex.toString());
        } finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }

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
