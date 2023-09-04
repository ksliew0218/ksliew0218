package domain;

import data.Admin_And_UserIDDOA;

import java.io.*;
import java.util.*;

public class Login extends User
{
    public Login(String C,String D)
    {
        super(C,D);
    }

    public Login (){}
    private static String loggedInUsername;

    public void Login_input()
    {
        String User_ID;
        String Password;
        Scanner input = new Scanner(System.in);
        System.out.print("Please input your user id:");
        User_ID = input.nextLine();
        System.out.print("Please input your password:");
        Password = input.nextLine();
        Login L = new Login(User_ID,Password);
        if (L.Login_Detail() == 1)
        {
            VerifyUserRole VUR = new VerifyUserRole();
            VUR.verifyUserRole();
        }
        else
        {
            System.out.println("Invalid Password Or UserID");
        }
    }
    public int Login_Detail()
    {
        int bool = 0;
        ArrayList<String> correctID = new ArrayList<>();
        try
        {
            try (FileReader myData = new FileReader("UserData.txt")) {
                Scanner Sc2 = new Scanner(myData);
                while(Sc2.hasNextLine())
                {
                    String UserInfo = Sc2.nextLine();
                    String[] UserArr = UserInfo.split("/");

                    if (UserArr[0].equals(super.UserId)&& UserArr[1].equals(super.UserPass) && UserArr[5].equals("Approve"))
                    {
                        loggedInUsername = UserArr[0];
                        System.out.println("Correct!");
                        correctID.add(UserInfo);
                        bool = 1;
                    }
                    else if(UserArr[0].equals(super.UserId)&& UserArr[1].equals(super.UserPass) && UserArr[5].equals("Pending"))
                    {
                        System.out.println("Your application is still pending");
                    }

                    else if(UserArr[0].equals(super.UserId)&& UserArr[1].equals(super.UserPass) && UserArr[5].equals("Reject"))
                    {
                        correctID.add(UserInfo);
                        try {
                            FileWriter fileWriter = new FileWriter("TempUser.txt");
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                            for (String value : correctID) {
                                bufferedWriter.write(value);
                                bufferedWriter.newLine();
                            }

                            bufferedWriter.close();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        correctID.clear();
                        System.out.println("Your application was rejected");
                        Admin_And_UserIDDOA User = new Admin_And_UserIDDOA();
                        User.WhyRejectedData();
                    }
                }
                if (bool ==1)
                {
                    myData.close();
                    String fileName = "TempUser.txt";
                    try {
                        FileWriter fileWriter = new FileWriter(fileName);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        for (String value : correctID) {
                            bufferedWriter.write(value);
                            bufferedWriter.newLine();
                        }

                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    correctID.clear();
                    return 1;
                }
                else
                {
                    myData.close();
                    return 0;
                }
            }
        }

        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
        return 0;
    }
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }
}

