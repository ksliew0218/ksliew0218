
import java.util.Scanner;
public class SIGMA_App {
    public static void main(String[] args) {
        public static void main (String[]args){
            SIGMA_App J = new SIGMA_App();
            J.Register_main();
        }
    }

    public void Register_main()
    {
        Scanner Sc3 = new Scanner(System.in);
        String UserId = "";
        String UserPass = "";
        String UserGender = "";
        String UserAge = "";
        String UserRole = "";

        int UserId01 = 0;
        int UserPass01 = 0;
        int UserGender01 = 0;
        int UserAge01 = 0;
        int UserRole01 = 0;

        while (UserId01 == 0)
        {
            int bool = 0;
            System.out.println("Enter User ID");
            UserId = Sc3.nextLine();
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
            System.out.println("Enter User Pass");
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
            System.out.println("Enter User Gender");
            UserGender = Sc3.nextLine();
            for (int i = 0; i < UserGender.length(); i++)
            {
                if (UserGender.charAt(i) == '/') {
                    System.out.println("Invalid with '/' Symbol");
                    bool = 1;
                    break;
                }
            }
            if (bool == 1)
            {
                UserGender01 = 0;
            }

            else
            {
                UserGender01 = 1;
            }
        }


        while (UserAge01 == 0)
        {
            System.out.println("Enter User Age");
            UserAge = Sc3.nextLine();
            SIGMA_App TI = new SIGMA_App();
            UserAge01 = TI.tryParseInt(UserAge);
        }

        while (UserRole01 == 0)
        {
            int bool = 0;
            System.out.println("Enter User Role");
            UserRole = Sc3.nextLine();
            for (int i = 0; i < UserRole.length(); i++)
            {
                if (UserRole.charAt(i) == '/') {
                    System.out.println("Invalid with '/' Symbol");
                    bool = 1;
                    break;
                }
            }
            if (bool == 1)
            {
                UserRole01 = 0;
            }

            else
            {
                UserRole01 = 1;
            }
        }

        Register R = new Register(UserId,UserPass,UserGender,UserAge,UserRole);
        R.Register_Detail();
    }

    public void Login_main()
    {
        Scanner Sc3 = new Scanner(System.in);
        String UserId;
        String UserPass;
        System.out.println("Enter User Id");
        UserId = Sc3.nextLine();
        System.out.println("Enter User Pass");
        UserPass = Sc3.nextLine();
        Login L = new Login (UserId,UserPass);
        int result = L.Login_Detail();
        if (result == 1)
        {
            System.out.println("Sucess Login!");
        }
        else
        {
            System.out.println("Invalid Password or UserID");
        }
    }

    public int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Please input integer");
            return 0;
        }
    }
}
