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

    public void readfile_reject_history()
    {
        try
        {
            try (FileReader rejectHistory = new FileReader("Reason_for_reject.txt")) {
                Scanner rejectData = new Scanner(rejectHistory);
                while(rejectData.hasNextLine())
                {
                    String RejectInfo = rejectData.nextLine();
                    String[] rejectArr = RejectInfo.split("/");
                    System.out.println("User ID: " + rejectArr[0] + "\n" + "Reject Reason: " + rejectArr[1] + "\n" + "Date and Time :" + rejectArr[2]+ "\n");
                }
                rejectHistory.close();
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
    }

    public String WhyRejected()
    {
        String UserID01 = "";
        try
        {
            try (FileReader rejectHistory00 = new FileReader("TempUser.txt")) {
                Scanner rejectData00 = new Scanner(rejectHistory00);
                while(rejectData00.hasNext())
                {
                    String RejectInfo00 = rejectData00.next();
                    String[] rejectArr00 = RejectInfo00.split("/");
                    UserID01 = rejectArr00[0];
                    break;
                }
                rejectHistory00.close();
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }


        return UserID01;
    }

    public void WhyRejectedData()
    {
        try
        {
            try (FileReader rejectHistory = new FileReader("Reason_for_reject.txt")) {
                Scanner rejectData = new Scanner(rejectHistory);
                while(rejectData.hasNextLine())
                {
                    String RejectInfo = rejectData.nextLine();
                    String[] rejectArr = RejectInfo.split("/");
                    if (rejectArr[0].equals(WhyRejected()))
                    {
                        System.out.println("User ID: " + rejectArr[0] + "\n" + "Reject Reason: " + rejectArr[1] + "\n" + "Date and Time :" + rejectArr[2]+ "\n");
                    }
                }
                rejectHistory.close();
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
    }
}
