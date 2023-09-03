package domain;

import utility.Utility;

import java.util.Scanner;

public class Project_Manager {
    public Project_Manager(){}

    public void Project_Manager_Menu()
    {
        int exit = 0;
        do
        {
            System.out.println(
                    "1. List of Items (View)" + "\n" +
                            "2. List of Suppliers (View)" + "\n" +
                            "3. Display Requisition (View)" + "\n" +
                            "4. Generate Purchase Order (Add/Save/Delete/Edit)" + "\n" +
                            "5. List of Purchaser Orders (View)" + "\n" +
                    "6. Exit" + "\n");
            System.out.print("Please select a number: ");
            Scanner input_admin = new Scanner(System.in);
            char menu_sales_manahger;
            menu_sales_manahger = Utility.readChar();
            switch (menu_sales_manahger)
            {
                case '1':
                    break;
                case '2':
                    break;
                case '3':
                    break;
                case '4':
                    break;
                case '5':
                    break;
                case '6':
                    exit = 1;
                    break;
                default:
                    System.out.println("Invalid value");
                    break;
            }
        }while(exit != 1);
    }
}
