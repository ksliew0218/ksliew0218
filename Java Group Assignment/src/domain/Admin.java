package domain;
import data.Admin_And_UserIDDOA;
import utility.Utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.*;

public class Admin {
    public Admin()
    {}
    public void admin_menu()
    {
        int exit = 0;
        do
        {
            System.out.println("1. Check all applicant information");
            System.out.println("2. Search User");
            System.out.println("3. Approve Or Reject Applicant");
            System.out.println("4. Check the reason for reject the applicant");
            System.out.println("5. Exit");
            System.out.print("Enter a number:");
            Scanner input_admin = new Scanner(System.in);
            char menu_admin;
            menu_admin = Utility.readChar();
            switch (menu_admin)
            {
                case '1':
                    readfile();
                    break;
                case '2':
                    search_user_id();
                    break;
                case '3':
                    search_and_decision();
                    break;
                case '4':
                    Reject_reason_history();
                    break;
                case '5':
                    exit = 1;
                    break;
            }
        } while(exit != 1);
    }

    public void Reject_reason_history()
    {
        Admin_And_UserIDDOA RH = new Admin_And_UserIDDOA();
        RH.readfile_reject_history();
    }
    public void readfile()
    {
        Admin_And_UserIDDOA AUID = new Admin_And_UserIDDOA();
        AUID.readfile_process();
    }

    public void search_and_decision()
    {
        Scanner reason01 = new Scanner(System.in);
        String reason = "";
        ArrayList<String> updatedStatus = new ArrayList<>();
        ArrayList<String> reason_for_reject = new ArrayList<>();
        String search = "";
        String decision = "";
        boolean exit = true;
        Scanner ans = new Scanner(System.in);
        do
        {
            System.out.print("Please input UserID: ");
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
                            System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                    "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                            System.out.println("1. Approve");
                            System.out.println("2. Reject");
                            System.out.println("3. Pending");
                            decision = ans.nextLine();
                            if (decision.equals("1"))
                            {
                                UserArr[5] = "Approve";
                                System.out.println("Sucess to update!");
                                System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                        "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                            }
                            else if (decision.equals("2"))
                            {
                                System.out.println("Do not input / symbol" + "\n" +
                                        "Please input the reason (input -1 to cancel reject):");
                                reason = reason01.nextLine();
                                if (reason.equals("-1"))
                                {
                                    System.out.println("Canceled reject");
                                    UserArr[5] = "Pending";
                                    System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                            "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);

                                }
                                else if (reason.contains("/"))
                                {
                                    System.out.println("Canceled reject, because you input / symbol");
                                    UserArr[5] = "Pending";
                                    System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                            "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                                }
                                else
                                {
                                    UserArr[5] = "Reject";
                                    System.out.println("Sucess to update!");
                                    System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                            "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                                    reason_for_reject.add(UserArr[0]);
                                    reason_for_reject.add("/");
                                    reason_for_reject.add(reason);
                                    LocalDateTime currentDateTime = LocalDateTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd|MM|yyyy hh:mm:ss a");
                                    String currentTimeString = currentDateTime.format(formatter);
                                    reason_for_reject.add("/");
                                    reason_for_reject.add(currentTimeString);
                                    reason_for_reject.add("/");
                                    reason_for_reject.add("\n");
                                }
                            }
                            else if (decision.equals("3"))
                            {
                                UserArr[5] = "Pending";
                                System.out.println("Sucess to update!");
                                System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                                        "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
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
                        FW.write(value);
                    }
                    FW.close();
                    updatedStatus.clear();

                    FileWriter ReasonForReject = new FileWriter("Reason_for_reject.txt",true);
                    for (String reject_reason: reason_for_reject)
                    {
                        ReasonForReject.write(reject_reason);
                    }
                    ReasonForReject.close();
                    reason_for_reject.clear();
                    //bug, when have double "\n" (empty line)
                }
            }

            catch(IOException Ex)
            {
                System.out.println("File Error");
            }
            exit = false;
        }while(exit != false);
    }

    public void search_user_id()
    {
        String search = "";
        System.out.print("Please input UserID: ");
        Scanner ans = new Scanner(System.in);
        search = ans.nextLine();
        boolean search_id = false;
        try (FileReader myData = new FileReader("UserData.txt"))
        {
            Scanner Sc2 = new Scanner(myData);

            while(Sc2.hasNextLine())
            {
                String UserInfo = Sc2.nextLine();
                String[] UserArr = UserInfo.split("/");
                if (UserArr[0].equals(search))
                {
                    System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +
                            "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
                    search_id = true;
                    break;
                }
            }
            if (search_id  == false)
            {
                System.out.println("Cannot find this User_Id");
            }
            myData.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
