package domain;
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
                    System.out.println("Sales Manager Menu");
                    Sales_Manager SM = new Sales_Manager();
                    break;
                }
                else if (UserArr[4].equals("PM"))
                {
                    System.out.println("Project Manager Menu");
                    Project_Manager PM = new Project_Manager();
                    break;
                }
                else if (UserArr[4].equals("AD"))
                {
                    System.out.println("Admin Menu");
                    Admin AD = new Admin();
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
