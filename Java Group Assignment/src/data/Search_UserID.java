package data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Search_UserID {
    public Search_UserID(){}
    public void search_user_id()
    {
        String search = "";
        System.out.println("Please input UserID");
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
                System.out.println("User ID: " + UserArr[0] + "\t" + "User Gender: " + UserArr[2]+ "\t" + "User Age: " + UserArr[3] + "\t" +"User Age: " + UserArr[3] +
                        "\t" + "User Role: " + UserArr[4] + "\t" + "Status: " + UserArr[5]);
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
