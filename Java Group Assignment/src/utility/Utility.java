package utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);


    /**
     * 功能：读取键盘输入的一个菜单选项，值：1——5的范围
     * @return 1——5
     */
    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);//包含一个字符的字符串
            c = str.charAt(0);//将字符串转换成字符char类型
            if (c != '1' && c != '2' &&
                    c != '3' && c != '4' && c != '5') {
                System.out.print("选择错误，请重新输入：");
            } else break;
        }
        return c;
    }

    /**
     * 功能：读取键盘输入的一个字符
     * @return 一个字符
     */
    public static char readChar() {
        String str = readKeyBoard(1, false);//就是一个字符
        return str.charAt(0);
    }
    /**
     * 功能：读取键盘输入的一个字符，如果直接按回车，则返回指定的默认值；否则返回输入的那个字符
     * @param defaultValue 指定的默认值
     * @return 默认值或输入的字符
     */

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, true);//要么是空字符串，要么是一个字符
        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }

    /**
     * 功能：读取键盘输入的整型，长度小于2位
     * @return 整数
     */
    public static int readInt() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, false);//一个整数，长度<=2位
            try {
                n = Integer.parseInt(str);//将字符串转换成整数
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }
    /**
     * 功能：读取键盘输入的 整数或默认值，如果直接回车，则返回默认值，否则返回输入的整数
     * @param defaultValue 指定的默认值
     * @return 整数或默认值
     */
    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, true);
            if (str.equals("")) {
                return defaultValue;
            }

            //异常处理...
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }

    /**
     * 功能：读取键盘输入的指定长度的字符串
     * @param limit 限制的长度
     * @return 指定长度的字符串
     */

    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    /**
     * 功能：读取键盘输入的指定长度的字符串或默认值，如果直接回车，返回默认值，否则返回字符串
     * @param limit 限制的长度
     * @param defaultValue 指定的默认值
     * @return 指定长度的字符串
     */

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("")? defaultValue : str;
    }


    /**
     * 功能：读取键盘输入的确认选项，Y或N
     * 将小的功能，封装到一个方法中.
     * @return Y或N
     */
    public static char readConfirmSelection() {
        System.out.print("Please input your choice(Y/N): ");
        char c;
        for (; ; ) {//无限循环
            //在这里，将接受到字符，转成了大写字母
            //y => Y n=>N
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

    /**
     * 功能： 读取一个字符串
     * @param limit 读取的长度
     * @param blankReturn 如果为true ,表示 可以读空字符串。
     * 					  如果为false表示 不能读空字符串。
     *
     *	如果输入为空，或者输入大于limit的长度，就会提示重新输入。
     * @return
     */
    private static String readKeyBoard(int limit, boolean blankReturn) {

        //定义了字符串
        String line = "";

        //scanner.hasNextLine() 判断有没有下一行
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();//读取这一行

            //如果line.length=0, 即用户没有输入任何内容，直接回车
            if (line.length() == 0) {
                if (blankReturn) return line;//如果blankReturn=true,可以返回空串
                else continue; //如果blankReturn=false,不接受空串，必须输入内容
            }

            //如果用户输入的内容大于了 limit，就提示重写输入
            //如果用户如的内容 >0 <= limit ,我就接受
            if (line.length() < 1 || line.length() > limit) {
                System.out.print("输入长度（不能大于" + limit + "）错误，请重新输入：");
                continue;
            }
            break;
        }

        return line;
    }

    public static boolean readBoolean() {
        boolean validInput = false;
        boolean result = false;

        while (!validInput) {
            System.out.print("Please enter 'true' or 'false':");
            String available = readString(5); // Reading as string
            if ("true".equalsIgnoreCase(available) || "false".equalsIgnoreCase(available)) {
                result = "true".equalsIgnoreCase(available);
                validInput = true;  // exit the loop
            } else {
                System.out.print("Invalid input. Please try again. ");
            }
        }

        return result;
    }

    /**
     * 功能：读取键盘输入的日期值，格式为 dd/MM/yyyy
     * @return 日期
     */
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

        /**
     * 功能：读取键盘输入的双精度浮点数
     * @return 双精度浮点数
     */
    public static double readDouble() {
        double n;
        for (; ; ) {
            String str = readKeyBoard(10, false); // an arbitrary limit of 10 characters
            try {
                n = Double.parseDouble(str); //将字符串转换成双精度浮点数
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
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
            contactNumber = readString(11); // Max length 11 to accommodate 10 or 11 digit numbers
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
            contactNumber = readString(11, existingValue); // Max length 11 to accommodate 10 or 11 digit numbers
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
            myData.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
