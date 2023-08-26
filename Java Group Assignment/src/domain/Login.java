package domain;

import java.io.*;
import java.util.Scanner;


public class Login extends User
{
    public Login(String C,String D)
    {
        super(C,D);
    }
    public int Login_Detail()
    {
        try
        {
            try (FileReader myData = new FileReader("UserData.txt")) {
                Scanner Sc2 = new Scanner(myData);
                while(Sc2.hasNextLine())
                {
                    String UserInfo = Sc2.nextLine();
                    String[] UserArr = UserInfo.split("/");

                    if (UserArr[0].equals(super.UserId)&& UserArr[1].equals(super.UserPass))
                    {
                        System.out.println("User Id Exist");
                        System.out.println("User Id =" + UserArr[0]);
                        System.out.println("User Pass =" + UserArr[1]);
                        System.out.println("User Gender =" + UserArr[2]);
                        System.out.println("User Age =" + UserArr[3]);
                        System.out.println("User Role =" + UserArr[4]);
                        return 1;
                    }
                }
                myData.close();
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
        return 0;
    }
}

