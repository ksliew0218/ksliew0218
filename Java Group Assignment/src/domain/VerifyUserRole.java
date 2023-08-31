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
                    System.out.println("Sales Manager");
                    Sales_Manager SM = new Sales_Manager();
                }
            }
            Sc2.close();
        } catch (IOException Ex) {
            System.out.println("File Error");
        }
    }
}
