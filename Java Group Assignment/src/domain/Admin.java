package domain;
import data.Aprove_Or_Reject_UserRole;
import data.Search_UserID;
import utility.Utility;

import java.io.*;
import java.util.Scanner;

public class Admin {
    public Admin()
    {}
    public void admin_menu()
    {
        int exit = 0;
        do
        {   Aprove_Or_Reject_UserRole AORU = new Aprove_Or_Reject_UserRole();
            System.out.println("1. Check all applicant information");
            System.out.println("2. Search User");
            System.out.println("3. Approve Or Reject Applicant");
            System.out.println("4. Exit");
            System.out.println("Enter a number");
            Scanner input_admin = new Scanner(System.in);
            char menu_admin;
            menu_admin = Utility.readChar();
            switch (menu_admin)
            {
                case '1':
                    AORU.readfile();
                    break;
                case '2':
                    Search_UserID SU = new Search_UserID();
                    SU.search_user_id();
                    break;
                case '3':
                    AORU.search_and_decision();
                    break;
                case '4':
                    exit = 1;
                    break;
            }
        } while(exit != 1);
    }
}
