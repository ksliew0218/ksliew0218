package data;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Admin_And_UserIDDOA {
    public Admin_And_UserIDDOA(){}
    public void readfile_process()
    {
        try
        {
            try (FileReader myData = new FileReader("UserData.txt")) {
                Scanner Sc2 = new Scanner(myData);
                while(Sc2.hasNextLine())
                {
                    String UserInfo = Sc2.nextLine();
                    String[] UserArr = UserInfo.split("/");

                    if (UserArr[5].equals("Pending"))
                    {
                        System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                    }
                }
                myData.close();
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
    }
}
