package utility;


import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utility {
    private static final Scanner scanner = new Scanner(System.in);

    public static char readChar() {
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, true);
        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }

    public static int readInt() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, false);
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer, please try again:：");
            }
        }
        return n;
    }

    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, true);
            if (str.equals("")) {
                return defaultValue;
            }

            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer, please try again：");
            }
        }
        return n;
    }

    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("")? defaultValue : str;
    }


    public static char readConfirmSelection() {
        System.out.print("Please input your choice(Y/N): ");
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("Invalid input, please try again：");
            }
        }
        return c;
    }


    private static String readKeyBoard(int limit, boolean blankReturn) {

        String line = "";

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.length() == 0) {
                if (blankReturn) return line;
                else continue;
            }

            if (line.length() > limit) {
                System.out.print("String length（cannot exceed" + limit + "）Invalid input, please try again：");
                continue;
            }
            break;
        }

        return line;
    }


    public static Date readDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // this will not enable 25/13/2021 as valid input
        Date date = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Please enter the date in dd/MM/yyyy format: ");
            String dateString = readString(10); // Reading as string
            try {
                date = sdf.parse(dateString);
                validInput = true; // exit the loop if parsing is successful
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please try again. ");
            }
        }

        return date;
    }

    public static double readDouble() {
        double n;
        for (; ; ) {
            String str = readKeyBoard(10, false); // an arbitrary limit of 10 characters
            try {
                n = Double.parseDouble(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input, please try again：");
            }
        }
        return n;
    }

    public static double readDouble(double defaultValue) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or press Enter to use the default value.");
            }
        }
    }

    public static String readContactNumber() {
        String contactNumber;
        for (; ; ) {
            System.out.print("Enter Contact Number: ");
            contactNumber = readString(11);
            if (contactNumber.length() == 10 || contactNumber.length() == 11) {
                try {
                    Long.parseLong(contactNumber); // Check if it's a valid number
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a 10 or 11-digit contact number.");
                }
            } else {
                System.out.println("Invalid length. Please enter a 10 or 11-digit contact number.");
            }
        }
        return contactNumber;
    }

    public static String readEmail() {
        String email;
        for (; ; ) {
            System.out.print("Enter Email: ");
            email = readString(50); // Max length 50 for email
            if (email.contains("@") && email.contains(".")) {
                // Add more validation if needed
                break;
            } else {
                System.out.println("Invalid email format. Please include '@' and '.'");
            }
        }
        return email;
    }

    public static String readContactNumber(String existingValue) {
        String contactNumber;
        for (; ; ) {
            System.out.print("Enter Contact Number: ");
            contactNumber = readString(11, existingValue);
            if (contactNumber.length() == 10 || contactNumber.length() == 11) {
                try {
                    Long.parseLong(contactNumber); // Check if it's a valid number
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a 10 or 11-digit contact number.");
                }
            } else {
                System.out.println("Invalid length. Please enter a 10 or 11-digit contact number.");
            }
        }
        return contactNumber;
    }

    public static String readEmail(String existingValue) {
        String email;
        for (; ; ) {
            System.out.print("Enter Email: ");
            email = readString(50, existingValue); // Max length 50 for email
            if (email.contains("@") && email.contains(".")) {
                // Add more validation if needed
                break;
            } else {
                System.out.println("Invalid email format. Please include '@' and '.'");
            }
        }
        return email;
    }

    public static Date readDate(Date existingValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // this will not enable 25/13/2021 as valid input
        Date date = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Please enter the date in dd/MM/yyyy format: ");
            String dateString = readString(10, sdf.format(existingValue)); // Reading as string
            try {
                date = sdf.parse(dateString);
                validInput = true; // exit the loop if parsing is successful
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please try again. ");
            }
        }

        return date;
    }

    public static void PersonalInfo(){
        try (FileReader myData = new FileReader("TempUser.txt"))
        {
            Scanner readInfo = new Scanner(myData);
            while(readInfo.hasNextLine())
            {
                String UserInfo = readInfo.nextLine();
                String[] UserArr = UserInfo.split("/");
                System.out.println("UserId: " + UserArr[0]);
                System.out.println("Gender: " + UserArr[2]);
                System.out.println("Age:  " + UserArr[3]);
                System.out.println("Role: " + UserArr[4]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
