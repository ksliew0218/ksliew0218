package data;
import java.io.*;
import java.util.Scanner;
public class Aprove_Or_Reject_UserRole {
    public Aprove_Or_Reject_UserRole (){}
    public void readfile()
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
                        System.out.println("User ID: " + UserArr[0]);
                        System.out.println("User Gender: " + UserArr[2]);
                        System.out.println("User Age: " + UserArr[3]);
                        System.out.println("User Role: " + UserArr[4]);
                        System.out.println("Status: " + UserArr[5]);
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

    public void search_and_decision()
    {
        String search = "";
        String decision = "";
        Scanner ans = new Scanner(System.in);
        do
        {
            System.out.println("Please input UserID");
            search = ans.nextLine();

            try
            {
                try (FileReader myData = new FileReader("UserData.txt")) {
                Scanner Sc2 = new Scanner(myData);
                while(Sc2.hasNextLine())
                {
                    String UserInfo = Sc2.nextLine();
                    String[] UserArr = UserInfo.split("/");

                    if (UserArr[1].equals(search))
                    {
                        System.out.println("User ID: " + UserArr[0]);
                        System.out.println("User Gender: " + UserArr[2]);
                        System.out.println("User Age: " + UserArr[3]);
                        System.out.println("User Role: " + UserArr[4]);
                        System.out.println("Status: " + UserArr[5]);
                    }
                }
                myData.close();
                }
            }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.println("3. Pending");




        }while(!decision.equals("3"));
    }
}
