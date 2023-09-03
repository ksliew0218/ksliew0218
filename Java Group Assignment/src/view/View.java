package view;

import domain.*;
import utility.Utility;

public class View {
    public void mainMenu() {
        char choice;
        do {
            System.out.println("Welcome to Menu");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please Select a number: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> {
                    Login L = new Login();
                    L.Login_input();
                }
                case '2' -> {
                    Register R = new Register();
                    R.Register_main();
                }
                case '3' -> System.out.println("Thank you, bye");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != '3');
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


    public void SupplierEntryMenu() {
        char choice;
        Supplier supplier = new Supplier();
        do {
            System.out.println("\t\t\t1. Add Supplier");
            System.out.println("\t\t\t2. Delete Supplier");
            System.out.println("\t\t\t3. Edit Supplier");
            System.out.println("\t\t\t4. Search Supplier by Supplier Name");
            System.out.println("\t\t\t5. View Supplier Details");
            System.out.println("\t\t\t6. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> supplier.add();
                case '2' -> supplier.delete();
                case '3' -> supplier.edit();
                case '4' -> supplier.searchSupplierByID();
                case '5' -> supplier.view();
                case '6' -> {
                    System.out.println("Exiting supplier entry menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    public void DailySalesEntryMenu() {
        char choice;
        DailySales dailySales = new DailySales();
        do {
            System.out.println("\n\t\t\t1. Add Daily Sales Entry");
            System.out.println("\t\t\t2. Delete Daily Sales Entry");
            System.out.println("\t\t\t3. Edit Daily Sales Entry");
            System.out.println("\t\t\t4. Search Daily Sales Entry by Date");
            System.out.println("\t\t\t5. View Daily Sales Report");
            System.out.println("\t\t\t6. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> dailySales.add();
                case '2' -> dailySales.delete();
                case '3' -> dailySales.edit();
                case '4' -> dailySales.searchByDate();
                case '5' -> dailySales.view();
                case '6' -> {
                    System.out.println("Exiting daily sales entry menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }
}
