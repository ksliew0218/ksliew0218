package data;
import java.io.*;
import java.util.ArrayList;
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
        ArrayList<String> updatedStatus = new ArrayList<>();
        String search = "";
        String decision = "";
        boolean exit = true;
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

                    if (UserArr[0].equals(search) && UserArr[5].equals("Pending"))
                    {
                        System.out.println("User ID: " + UserArr[0]);
                        System.out.println("User Gender: " + UserArr[2]);
                        System.out.println("User Age: " + UserArr[3]);
                        System.out.println("User Role: " + UserArr[4]);
                        System.out.println("Status: " + UserArr[5]);
                        System.out.println("1. Approve");
                        System.out.println("2. Reject");
                        System.out.println("3. Pending");
                        decision = ans.nextLine();
                        if (decision.equals("1"))
                        {
                            UserArr[5] = "Approve";
                        }
                        else if (decision.equals("2"))
                        {
                            UserArr[5] = "Reject";
                        }
                        else if (decision.equals("3"))
                        {
                            UserArr[5] = "Pending";
                        }
                        updatedStatus.add(UserArr[0]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[1]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[2]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[3]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[4]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[5]);
                        updatedStatus.add("/");
                        updatedStatus.add("\n");
                    }
                    else
                    {
                        updatedStatus.add(UserArr[0]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[1]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[2]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[3]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[4]);
                        updatedStatus.add("/");
                        updatedStatus.add(UserArr[5]);
                        updatedStatus.add("/");
                        updatedStatus.add("\n");
                    }
                }
                myData.close();
                FileWriter FW = new FileWriter("UserData.txt");
                for (String value: updatedStatus)
                {
                    System.out.print(value);
                    FW.write(value);
                }
                FW.close();
                updatedStatus.clear();
                //bug, when have double "\n" (empty line)
                }
            }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
        }while(exit != false);
    }
}
