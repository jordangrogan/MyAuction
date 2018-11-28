import java.util.Scanner;

public class MyAuction {

  public static void main(String args[]) {

    Scanner reader = new Scanner(System.in);
    String response;

    System.out.println("Welcome to My Auction!");

    do {
      System.out.println("Are you an admin or a customer?");
      response = reader.nextLine();
    } while(!(response.equals("admin") || response.equals("customer")));

    if(response.equals("customer")) {
      System.out.println("Welcome Customer!");
    } else if(response.equals("admin")) {
      System.out.println("Welcome Admin!");
    }

  }

}
