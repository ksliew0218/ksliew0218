package domain;

import view.View;

import java.io.*;
import java.util.Scanner;
public class Register extends User{

    public Register(String A, String B, String C, String D,String E, String F) {
        super(A, B,C,D,E,F);
    }

    public Register(){}

    public void Register_Detail()
    {
        try
        {
            File MyData = new File("UserData.txt");
            FileWriter fw = new FileWriter(MyData,true);
            BufferedWriter bw = new BufferedWriter(fw);
            try (PrintWriter pw = new PrintWriter(bw)) {
                String UserData = super.UserId + "/" + super.UserGender + "/" + super.UserPass + "/" + super.UserAge + "/" + super.UserRole + "/" + super.Status + "/" + "\n";
                pw.write(UserData);
                pw.close();
            }
        }
        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
    }

    public void Register_main()
    {
        Scanner Sc3 = new Scanner(System.in);
        String UserId = "";
        String UserPass = "";
        String Usergender = "";
        String UserGender = "";
        String UserAge = "";
        String UserRole = "";
        String Role = "";

        int UserId01 = 0;
        int UserPass01 = 0;
        int UserGender01 = 0;
        int UserAge01 = 0;
        int UserRole01 = 0;

        while (UserId01 == 0)
        {
            int bool = 0;
            System.out.print("Enter User ID: ");
            UserId = Sc3.nextLine();
            try (FileReader myData = new FileReader("UserData.txt"))
            {
                Scanner Sc2 = new Scanner(myData);
                while(Sc2.hasNextLine())
                {
                    String UserInfo = Sc2.nextLine();
                    String[] UserArr = UserInfo.split("/");

                    if (UserArr[0].equals(UserId))
                    {
                        System.out.println("This UserID already exist, please try another UserID");
                        bool = 1;
                    }
                }
                myData.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < UserId.length(); i++)
            {
                if (UserId.charAt(i) == '/') {
                    System.out.println("Invalid with '/' Symbol");
                    bool = 1;
                    break;
                }
            }
            if (bool == 1)
            {
                UserId01 = 0;
            }

            else
            {
                UserId01 = 1;
            }
        }

        while (UserPass01 == 0)
        {
            int bool = 0;
            System.out.print("Enter User Pass: ");
            UserPass = Sc3.nextLine();
            for (int i = 0; i < UserPass.length(); i++)
            {
                if (UserPass.charAt(i) == '/') {
                    System.out.println("Invalid with '/' Symbol");
                    bool = 1;
                    break;
                }
            }
            if (bool == 1)
            {
                UserPass01 = 0;
            }

            else
            {
                UserPass01 = 1;
            }
        }

        while (UserGender01 == 0)
        {
            int bool = 0;
            System.out.print("Enter User Gender: ");
            Usergender = Sc3.nextLine();
            UserGender = Usergender.toUpperCase();
            for (int i = 0; i < UserGender.length(); i++)
            {
                if (UserGender.charAt(i) == '/') {
                    System.out.println("Invalid with '/' Symbol");
                    bool = 1;
                    break;
                }
            }
            if (bool == 1 || (!UserGender.equals("MALE") && !UserGender.equals("FEMALE")))
            {
                System.out.println(UserGender);
                UserGender01 = 0;
            }

            else
            {
                UserGender01 = 1;
            }
        }


        while (UserAge01 == 0)
        {
            System.out.print("Enter User Age: ");
            UserAge = Sc3.nextLine();
            Register TI = new Register();
            UserAge01 = TI.tryParseInt(UserAge);
        }

        while (UserRole01 == 0)
        {
            System.out.println("1. Sales Manager");
            System.out.println("2. Purchase Manager");
            System.out.print("Enter a number: ");
            UserRole = Sc3.nextLine();
            if (UserRole.equals("1"))
            {
                Role = "SM";
                UserRole01 = 1;
            }
            else if (UserRole.equals("2"))
            {
                Role = "PM";
                UserRole01 = 1;
            }
            else
            {
                System.out.println("Please input the valid number.");
            }
        }
        Register R = new Register(UserId,UserPass,UserGender,UserAge,Role,"Pending");
        R.Register_Detail();
    }
    public int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Please input integer.");
            return 0;
        }
    }
}

