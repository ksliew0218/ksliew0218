package domain;

import java.io.*;
import java.util.*;

public class Login extends User
{
    public Login(String C,String D)
    {
        super(C,D);
    }

    public Login (){}

    public void Login_input()
    {
        String User_ID;
        String Password;
        Scanner input = new Scanner(System.in);
        System.out.println("Please input your user id:");
        User_ID = input.nextLine();
        System.out.println("Please input your password");
        Password = input.nextLine();
        Login L = new Login(User_ID,Password);
        L.Login_Detail();
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

                    if (UserArr[0].equals(super.UserId)&& UserArr[1].equals(super.UserPass))
                    {
                        System.out.println("User Id Exist");
                        System.out.println("User Id =" + UserArr[0]);
                        System.out.println("User Gender =" + UserArr[2]);
                        System.out.println("User Age =" + UserArr[3]);
                        System.out.println("User Role =" + UserArr[4]);
                        correctID.add(UserInfo);
                        bool = 1;
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
}

