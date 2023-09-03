package domain;
import utility.Utility;

import java.util.*;
public class Sales_Manager {
    public Sales_Manager(){}
    public void Sales_Manager_Menu()
    {
        int exit = 0;
        do
        {
            System.out.println(
                    "1. Item Entry (Add/Save/Delete/Edit)" + "\n" +
                    "2. Supplier Entry (Add/Save/Delete/Edit)" + "\n" +
                    "3. Daily Item-wise Sales Entry (Add/Save/Delete/Edit)" + "\n" +
                    "4. Create a Purchase Requisition (Add/Save/Delete/Edit)" + "\n" +
                    "5. Display Requisition (View)" + "\n" +
                    "6. List of Purchaser Orders(View)" + "\n" +
                    "7. Exit" + "\n");
            System.out.print("Please input a number: ");
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
                    break;
                case '7':
                    exit = 1;
                    break;
                default:
                    System.out.println("Invalid value");
                    break;
            }
        }while(exit != 1);
    }
}
