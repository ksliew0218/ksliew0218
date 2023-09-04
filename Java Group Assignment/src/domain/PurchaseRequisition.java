package domain;

import data.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public String autoGeneratePR(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO, String loggedInUsername) {
        List<String> items = itemDAO.getAllItems();
        List<String> suppliers = supplierDAO.getAllSuppliers();

        List<String> itemsToOrder = new ArrayList<>();
        List<String> feedbackForPM = new ArrayList<>();

        for (String item : items) {
            String[] details = item.split("\\$");
            if ("false".equals(details[details.length - 2])) {
                itemsToOrder.add(item);
            }
        }

        if (itemsToOrder.isEmpty()) {
            return "Stock is sufficient for all items.";
        }

        Map<String, List<String>> supplierDict = new HashMap<>();

        for (String supplier : suppliers) {
            String[] supplierDetails = supplier.split("\\$");
            if (!supplierDict.containsKey(supplierDetails[0])) {
                supplierDict.put(supplierDetails[0], new ArrayList<>());
            }
            supplierDict.get(supplierDetails[0]).add(supplier);
        }

        List<Map<String, String>> prDetailsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String creationDate = sdf.format(new Date());

        // Generate a unique PR ID for this batch
        String prID = "PR" + String.format("%03d", prDAO.getNextAvailablePRID());

        for (String item : itemsToOrder) {
            String[] itemDetails = item.split("\\$");
            List<String> itemSuppliers = supplierDict.get(itemDetails[4]);

            for (String supplier : itemSuppliers) {
                String[] supplierDetails = supplier.split("\\$");
                Map<String, String> prDetails = new HashMap<>();
                prDetails.put("ItemCode", itemDetails[0]);
                prDetails.put("ProductName", itemDetails[1]);
                prDetails.put("Category", itemDetails[5]);
                prDetails.put("SupplierCode", supplierDetails[0]);
                prDetails.put("SupplierName", supplierDetails[1]);
                prDetails.put("CreationDate", creationDate);
                prDetails.put("CurrentStock", itemDetails[2]);
                prDetails.put("CreatedBy", loggedInUsername);
                prDetails.put("SupplierContact", supplierDetails[2]);
                prDetails.put("ExpectedArrivalDays", supplierDetails[6]);
                prDetails.put("PRID", prID);  // Use the same PR ID for all items in this batch
                prDetails.put("POStatus", "false");  // Add this line to set the PO status as false
                prDetailsList.add(prDetails);

                // Construct feedback for PM
                String feedback = "Generated PR for Item: " + itemDetails[1] + " (" + itemDetails[0] + ")" +
                        " with Supplier: " + supplierDetails[1] + " (" + supplierDetails[0] + ")" +
                        ", Current Stock: " + prDetails.get("CurrentStock") +
                        ", Expected Arrival Day: " + prDetails.get("ExpectedArrivalDays");
                feedbackForPM.add(feedback);
            }
        }

        for (Map<String, String> prDetails : prDetailsList) {
            prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails.toString()));
        }

        return "Purchase Requisitions have been generated:\n" + String.join("\n", feedbackForPM);
    }

    public String manualGeneratePR(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO, String loggedInUsername, Scanner scanner) {
        List<String> items = itemDAO.getAllItems();
        List<String> suppliers = supplierDAO.getAllSuppliers();
        Map<String, String> itemDict = new HashMap<>();
        Map<String, List<String>> supplierDict = new HashMap<>();

        for (String item : items) {
            String[] itemDetails = item.split("\\$");
            itemDict.put(itemDetails[0], item);
        }

        for (String supplier : suppliers) {
            String[] supplierDetails = supplier.split("\\$");
            if (!supplierDict.containsKey(supplierDetails[0])) {
                supplierDict.put(supplierDetails[0], new ArrayList<>());
            }
            supplierDict.get(supplierDetails[0]).add(supplier);
        }

        List<Map<String, String>> prDetailsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String creationDate = sdf.format(new Date());
        String prID = "PR" + String.format("%03d", prDAO.getNextAvailablePRID());

        System.out.println("\n------ Manual Purchase Requisition Generation ------\n");

        while (true) {
            System.out.println("Available items:");
            for (String item : items) {
                String[] details = item.split("\\$");
                System.out.println(details[0] + ": " + details[1] + " (Current Stock: " + details[2] + ")");
            }

            System.out.print("Enter Item Code (or 'exit' to finish): ");
            String itemCode = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(itemCode)) {
                break;
            }

            if (!itemDict.containsKey(itemCode)) {
                System.out.println("\nInvalid Item Code. Please try again.\n");
                continue;
            }

            String selectedItem = itemDict.get(itemCode);
            String[] selectedItemDetails = selectedItem.split("\\$");
            List<String> itemSuppliers = supplierDict.get(selectedItemDetails[4]);

            for (String supplier : itemSuppliers) {
                String[] supplierDetails = supplier.split("\\$");
                Map<String, String> prDetails = new HashMap<>();
                prDetails.put("ItemCode", selectedItemDetails[0]);
                prDetails.put("ProductName", selectedItemDetails[1]);
                prDetails.put("Category", selectedItemDetails[5]);
                prDetails.put("SupplierCode", supplierDetails[0]);
                prDetails.put("SupplierName", supplierDetails[1]);
                prDetails.put("CreationDate", creationDate);
                prDetails.put("CurrentStock", selectedItemDetails[2]);
                prDetails.put("CreatedBy", loggedInUsername);
                prDetails.put("SupplierContact", supplierDetails[2]);
                prDetails.put("ExpectedArrivalDays", supplierDetails[6]);
                prDetails.put("PRID", prID);  // Use the same PR ID for all items in this batch
                prDetails.put("POStatus", "false");  // Add this line to set the PO status as false
                prDetailsList.add(prDetails);
            }

            System.out.println("\nItem " + selectedItemDetails[1] + " added to PR.\n");
        }

        if (prDetailsList.isEmpty()) {
            return "No items selected. PR generation aborted.";
        }

        for (Map<String, String> prDetails : prDetailsList) {
            prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails.toString()));
        }

        System.out.println("\n------ Items Added to PR ------");
        for (Map<String, String> prDetails : prDetailsList) {
            System.out.println(prDetails.get("ProductName") + " with Supplier: " + prDetails.get("SupplierName"));
        }

        StringBuilder feedbackForPM = new StringBuilder("\n------ Purchase Requisition Details ------\n");
        feedbackForPM.append("PR ID: ").append(prID).append("\n");
        feedbackForPM.append("Creation Date: ").append(creationDate).append("\n");
        feedbackForPM.append("Created By: ").append(loggedInUsername).append("\n\n");
        feedbackForPM.append("Items in this PR:\n");
        for (Map<String, String> prDetails : prDetailsList) {
            feedbackForPM.append("- Item: ").append(prDetails.get("ProductName"))
                    .append(", Supplier: ").append(prDetails.get("SupplierName"))
                    .append(", Expected Arrival Days: ").append(prDetails.get("ExpectedArrivalDays"))
                    .append("\n");
        }
        feedbackForPM.append("\n");

        return feedbackForPM.toString() + "Purchase Requisitions have been manually generated and saved.";
    }


    public void displayPRList(PurchaseRequisitionDAO prDAO, Scanner scanner) {
        List<String> allPRs = prDAO.getAllPRs();
        Map<Integer, String> prMenu = new HashMap<>();

        while (true) {
            System.out.println("\nAvailable PRs:");
            int counter = 1;
            for (String pr : allPRs) {
                String prID = pr.split("\\$")[0];
                System.out.println(counter + ". " + prID);
                prMenu.put(counter, prID);
                counter++;
            }

            System.out.print("\nEnter the number of PR to view details (or 'exit' to finish): ");
            String choice = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(choice)) {
                break;
            }

            try {
                int chosenNumber = Integer.parseInt(choice);
                if (prMenu.containsKey(chosenNumber)) {
                    displayPRDetails(prMenu.get(chosenNumber), prDAO);
                } else {
                    System.out.println("\nInvalid choice. Please select a valid number.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.\n");
            }
        }
    }

    private void displayPRDetails(String prID, PurchaseRequisitionDAO prDAO) {
        List<String> prDetails = prDAO.getPRDetails(prID);
        System.out.println("\nDetails for PR: " + prID + "\n");

        for (String detail : prDetails) {
            String[] detailsArray = detail.split("\\$");
            System.out.println("------------------------------");
            System.out.println("Item Code: " + detailsArray[1]);
            System.out.println("Product Name: " + detailsArray[2]);
            System.out.println("Category: " + detailsArray[3]);
            System.out.println("Supplier Code: " + detailsArray[4]);
            System.out.println("Supplier Name: " + detailsArray[5]);
            System.out.println("Creation Date: " + detailsArray[6]);
            System.out.println("Current Stock: " + detailsArray[7]);
            System.out.println("Created By: " + detailsArray[8]);
            System.out.println("Supplier Contact: " + detailsArray[9]);
            System.out.println("Expected Arrival Days: " + detailsArray[10]);
            System.out.println("PO Status: " + (detailsArray[11].equals("true") ? "Converted to PO" : "Not yet converted to PO"));
            System.out.println("------------------------------");
        }
    }
}