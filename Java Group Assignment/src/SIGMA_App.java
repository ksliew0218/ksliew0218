
import data.Aprove_Or_Reject_UserRole;
import data.DailySalesDAO;
import domain.DailySales;
import domain.VerifyUserRole;
import view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SIGMA_App {
    public static void main(String[] args) {
        //View V = new View();
        //V.mainMenu();
        //new View().ItemEntryMenu();
//        Aprove_Or_Reject_UserRole AOR = new Aprove_Or_Reject_UserRole();
//        AOR.search_and_decision();
        new View().DailySalesEntryMenu();
    }
}
