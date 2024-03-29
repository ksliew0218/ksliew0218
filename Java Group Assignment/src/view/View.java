package view;

import data.*;
import domain.*;
import jdk.jshell.execution.Util;
import utility.Utility;
import java.util.Scanner;

public class View {
    public void mainMenu() {
        char choice;
        do {
            System.out.println("\n\t\t\t======Welcome to SIGMA SDN BHD (SSB)=====");
            System.out.println("\n\t\t\t1. Login");
            System.out.println("\t\t\t2. Register");
            System.out.println("\t\t\t3. Exit");
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
                case '3' -> System.out.println("See you again. Have a good day!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != '3');
    }

    public void ItemEntryMenu() {
        char choice;
        Item item = new Item();
        do {
            System.out.println("\n\t\t\t=====Item Entry Menu=====");
            System.out.println("\n\t\t\t1. Add item");
            System.out.println("\t\t\t2. Delete item");
            System.out.println("\t\t\t3. Edit item");
            System.out.println("\t\t\t4. Search item by Name");
            System.out.println("\t\t\t5. Search item by Category");
            System.out.println("\t\t\t6. Add item stock by purchase order");  // New method added
            System.out.println("\t\t\t7. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1':
                    item.add();
                    break;
                case '2':
                    item.delete();
                    break;
                case '3':
                    item.edit();
                    break;
                case '4':
                    item.searchItem_byName();
                    break;
                case '5':
                    item.searchItem_byCategory();
                    break;
                case '6':
                    item.addItemStockByPOID();
                    break;
                case '7':
                    // Exit the menu
                    System.out.println("Exiting item entry menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    public void SupplierEntryMenu() {
        char choice;
        Supplier supplier = new Supplier();
        do {
            System.out.println("\n\t\t\t=====Supplier Entry Menu=====");
            System.out.println("\n\t\t\t1. Add Supplier");
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
            System.out.println("\n\t\t\t=====Daily-Wise Item Entry=====");
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

    public void CreatePrMenu() {
        char choice;
        ItemDAO itemDAO = new ItemDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        PurchaseRequisitionDAO prDAO = new PurchaseRequisitionDAO();

        do {
            System.out.println("\n\t\t\t=====Purchase Requisition Menu======");
            System.out.println("\n\t\t\t1. Auto-Generate Purchase Requisition");
            System.out.println("\t\t\t2. Manually Create New Purchase Requisition");
            System.out.println("\t\t\t3. Display All Purchase Requisitions");
            System.out.println("\t\t\t4. Edit Purchase Requisitions");
            System.out.println("\t\t\t5. Delete Purchase Requisitions");
            System.out.println("\t\t\t6. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> {
                    PurchaseRequisition prAuto = new PurchaseRequisition();
                    String AutoResult = prAuto.autoGeneratePR(itemDAO, supplierDAO, prDAO);
                    System.out.println(AutoResult);
                }
                case '2' -> {
                    PurchaseRequisition prManual = new PurchaseRequisition();
                    String MnResult = prManual.manualGeneratePR(itemDAO, supplierDAO, prDAO);
                    System.out.println(MnResult);
                }
                case '3' -> {
                    PurchaseRequisition pr = new PurchaseRequisition();
                    pr.displayPRList(prDAO);
                }
                case '4' -> {
                    PurchaseRequisition pr = new PurchaseRequisition();
                    pr.editMenu(itemDAO, supplierDAO, prDAO);
                }
                case '5' -> {
                    PurchaseRequisition pr = new PurchaseRequisition();
                    pr.deletePR(prDAO);
                }
                case '6' -> {
                    System.out.println("Exiting purchase requisition menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    public void PurchaseOrderMenu() {
        char choice;
        ItemDAO itemDAO = new ItemDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        PurchaseRequisitionDAO prDAO = new PurchaseRequisitionDAO();
        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        PurchaseOrder poInstance = new PurchaseOrder();

        do {
            System.out.println("\n\t\t\t=====Purchase Order Menu=====");
            System.out.println("\n\t\t\t1. Generate PO from PR");
            System.out.println("\t\t\t2. Display All POs");
            System.out.println("\t\t\t3. Delete PO");
            System.out.println("\t\t\t4. Edit PO");
            System.out.println("\t\t\t5. Back");

            System.out.print("Please enter your choice: ");
            choice = Utility.readChar();

            switch (choice) {
                case '1' -> poInstance.generatePOFromPR(prDAO, poDAO, supplierDAO, itemDAO);
                case '2' -> poInstance.displayPOList(poDAO);
                case '3' -> poInstance.deletePO(poDAO);
                case '4' -> poInstance.editPOItemQuantity(poDAO);
                case '5' -> {
                    System.out.println("Exiting purchase order menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    public void admin_menu()
    {
        Admin ad = new Admin();
        int exit = 0;
        do
        {
            System.out.println("\n\t\t\t=====Admin Menu=====");
            System.out.println("\n0. Personal Information");
            System.out.println("1. Check all applicant information");
            System.out.println("2. Search User");
            System.out.println("3. Approve Or Reject Applicant");
            System.out.println("4. Check the reason for reject the applicant");
            System.out.println("5. Access as Sales Manager");
            System.out.println("6. Access as Purchase Manager");
            System.out.println("7. Exit");
            System.out.print("Enter a number:");
            Scanner input_admin = new Scanner(System.in);
            char menu_admin;
            menu_admin = Utility.readChar();
            switch (menu_admin) {
                case '0' -> {
                    Utility.PersonalInfo();
                }
                case '1' -> ad.readfile();
                case '2' -> ad.search_user_id();
                case '3' -> ad.search_and_decision();
                case '4' -> ad.Reject_reason_history();
                case '5' -> Sales_Manager_Menu();
                case '6' -> Purchase_Manager_Menu();
                case '7' -> exit = 1;
            }
        } while(exit != 1);
    }

    public void Sales_Manager_Menu()
    {
        int exit01 = 0;
        do
        {
            System.out.println("\n\t\t\t=====Sales Manger Menu=====");
            System.out.println(
                    """

                            0. Personal Information
                            1. Item Entry (Create/Read/Update/Edit)
                            2. Supplier Entry (Create/Read/Update/Edit)
                            3. Daily Item-wise Sales Entry (Create/Read/Update/Edit)
                            4. Purchase Requisition
                            5. List of Purchaser Orders
                            6. Back
                            """);
            System.out.print("Please input a number: ");
            char menu_sales_manager;
            menu_sales_manager = Utility.readChar();
            switch (menu_sales_manager) {
                case '0' -> Utility.PersonalInfo();
                case '1' -> ItemEntryMenu();
                case '2' -> SupplierEntryMenu();
                case '3' -> DailySalesEntryMenu();
                case '4' -> CreatePrMenu();
                case '5' -> {
                    PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
                    PurchaseOrder poInstance = new PurchaseOrder();
                    poInstance.displayPOList(poDAO);
                }
                case '6' -> exit01 = 1;
                default -> System.out.println("Invalid value");
            }
        }while(exit01 != 1);
    }

    public void Purchase_Manager_Menu()
    {
        int exit02 = 0;
        do
        {
            System.out.println("\n\t\t\t=====Purchase Manager Menu=====");
            System.out.println(
                    """

                            0. Personal Information
                            1. List of Items (View)
                            2. List of Suppliers (View)
                            3. View Requisition Order
                            4. Purchase Order
                            5. Exit
                            """);
            System.out.print("Please select a number: ");
            char menu_sales_manager;
            menu_sales_manager = Utility.readChar();
            switch (menu_sales_manager) {
                case '0' -> Utility.PersonalInfo();
                case '1' -> new Item().view();
                case '2' -> new Supplier().listOfSuppliers();
                case '3' -> {
                    PurchaseRequisitionDAO prDAO = new PurchaseRequisitionDAO();
                    PurchaseRequisition pr = new PurchaseRequisition();
                    pr.displayPRList(prDAO);
                }
                case '4' -> PurchaseOrderMenu();
                case '5' -> exit02 = 1;
                default -> System.out.println("Invalid value");
            }
        }while(exit02 != 1);
    }
}

