package view;

import domain.Item;
import domain.Login;
import domain.Register;
import domain.User;
import jdk.jshell.execution.Util;
import java.util.Scanner;
import utility.Utility;

public class View{
    public void viewMenu() {
        String ans = "";
        Scanner S = new Scanner(System.in);
        do
        {
            System.out.println("Welcome to Menu");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("Please Select a number: ");
            ans = S.nextLine();
            switch (ans)
            {
                case "1":
                    Login L = new Login();
                    L.Login_input();
                    break;
                case "2":
                    Register R = new Register();
                    R.Register_main();
                    break;
                case"3":
                    System.out.println("Thank you, bye");
            }
        } while (!ans.equals("3"));
    }

    public void SalesManagerMenu() {
        System.out.println("testing");
    }

    public void ItemEntryMenu() {
        char choice;
        Item item = new Item();
        do {
            System.out.println("\t\t\t1. Add item");
            System.out.println("\t\t\t2. Delete item");
            System.out.println("\t\t\t3. Edit item");
            System.out.println("\t\t\t4. Search item by Name");
            System.out.println("\t\t\t5. Search item by Category");
            System.out.println("\t\t\t6. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> item.add();
                case '2' -> item.delete();
                case '3' -> item.edit();
                case '4' -> item.searchItem_byName();
                case '5' -> item.searchItem_byCategory();
                case '6' -> {
                    // Exit the menu
                    System.out.println("Exiting item entry menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

// Implement or call the methods like addItem(), deleteItem(), edit

}
