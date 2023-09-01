package domain;

import data.PurchaseRequisitionDAO;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import utility.Utility;

public class PurchaseRequisition {

    private String PRID;
    private String itemCode;
    private int quantity;
    private String requiredDate;
    private String supplierCode;
    private String salesManagerID;
    private PurchaseRequisitionDAO dao = new PurchaseRequisitionDAO();

    // Constructor
    public PurchaseRequisition(String salesManagerID) {
        System.out.println("Creating a new Purchase Requisition...");
        this.PRID = generatePRID();

        System.out.print("Enter item code: ");
        this.itemCode = Utility.readString(10); // Assuming max length of item code is 10

        System.out.print("Enter quantity: ");
        this.quantity = Utility.readInt();

        System.out.print("Enter required date (dd/MM/yyyy format): ");
        this.requiredDate = Utility.readDate().toString(); // Convert Date object to String

        System.out.print("Enter supplier code: ");
        this.supplierCode = Utility.readString(10); // Assuming max length of supplier code is 10

        this.salesManagerID = salesManagerID;

        // Save the PR details using DAO
        dao.savePurchaseRequisition(this);
    }

    // Other methods remain the same as the previous version...
    // generatePRID(), displayPRDetails(), etc.

    // Getter methods for the attributes so DAO can access them
    public String getPRID() {
        return PRID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSalesManagerID() {
        return salesManagerID;
    }

    // Generate unique PRID
    private String generatePRID() {
        // For simplicity, we'll use the current timestamp as a unique PRID
        return "PR" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    // Save PR details to a text file
    public void savePRDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PRDetails.txt", true))) {
            writer.write(PRID + "," + itemCode + "," + quantity + "," + requiredDate + "," + supplierCode + "," + salesManagerID);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display PR details from the text file
    public static void displayPRDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader("PRDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.replace(",", "\t"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}