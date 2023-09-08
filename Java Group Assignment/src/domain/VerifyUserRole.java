package domain;
import view.View;

import java.io.*;
import java.util.*;

public class VerifyUserRole {
    public VerifyUserRole(){}
    public void verifyUserRole () {
        try {
            FileReader myData = new FileReader("TempUser.txt");
            Scanner Sc2 = new Scanner(myData);

            while (Sc2.hasNextLine()) {
                String UserInfo = Sc2.nextLine();
                String[] UserArr = UserInfo.split("/");
                if (UserArr[4].equals("SM")) {
                    System.out.println("Welcome to Sales Manager Menu");
                    View SM = new View();
                    SM.Sales_Manager_Menu();
                    break;
                }
                else if (UserArr[4].equals("PM"))
                {
                    System.out.println("Welcome to Purchase Manager Menu");
                    View PM = new View();
                    PM.Project_Manager_Menu();
                    break;
                }
                else if (UserArr[4].equals("AD"))
                {
                    System.out.println("Welcome to Admin Menu");
                    View AD = new View();
                    AD.admin_menu();
                    break;
                }
            }
            Sc2.close();
        } catch (IOException Ex) {
            System.out.println("File Error");
        }
    }
}
