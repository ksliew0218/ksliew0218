package domain;

import data.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PurchaseRequisition {

    private String PRID;
    private String itemCode;
    private String productName;
    private String category;
    private int stock;
    private int quantity;
    private String supplierCode;
    private String supplierName;
    private String supplierContact;
    private String CreationDate;
    private String salesManagerID;
    private String ExpectedArrivalDays;
    private String poStatus;

    private List<Map<String, String>> prDetailsList = new ArrayList<>();

    public PurchaseRequisition() {
        this.salesManagerID = Login.getLoggedInUsername();
    }

    // 修改后的构造函数
    public PurchaseRequisition(Map<String, String> prDetails) {
        this.PRID = prDetails.get("PRID");
        this.itemCode = prDetails.get("ItemCode");
        this.productName = prDetails.get("ProductName");
        this.category = prDetails.get("Category");
        this.stock = Integer.parseInt(prDetails.get("CurrentStock"));
        this.quantity = Integer.parseInt(prDetails.get("Quantity"));
        this.supplierCode = prDetails.get("SupplierCode");
        this.supplierName = prDetails.get("SupplierName");
        this.supplierContact = prDetails.get("SupplierContact");
        this.salesManagerID = prDetails.get("CreatedBy");
        this.CreationDate = prDetails.get("CreationDate");
        this.ExpectedArrivalDays = prDetails.get("ExpectedArrivalDays");
        this.poStatus = "false";
    }



    // Getter methods for the attributes so DAO can access them
    public String getPRID() {
        return PRID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public String getSalesManagerID() {
        return salesManagerID;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public String getExpectedArrivalDays() {
        return ExpectedArrivalDays;
    }

    public String getPOStatus() {
        return poStatus;
    }

    public List<Map<String, String>> getPrDetailsList() {
        return prDetailsList;
    }

    public String autoGeneratePR(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        String loggedInUsername = Login.getLoggedInUsername();
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
                prDetails.put("Quantity", String.valueOf(20));
                prDetails.put("CreatedBy", loggedInUsername);
                prDetails.put("SupplierContact", supplierDetails[2]);
                prDetails.put("ExpectedArrivalDays", supplierDetails[6]);
                prDetails.put("PRID", prID);  // Use the same PR ID for all items in this batch
                prDetails.put("POStatus", "false");  // Add this line to set the PO status as false
                prDetailsList.add(prDetails);

                // Construct feedback for PM
                String feedback = "Item: " + itemDetails[1] + " (Item Code: " + itemDetails[0] + ")" +
                        " with Supplier: " + supplierDetails[1] + " (Supplier Code: " + supplierDetails[0] + ")" +
                        ", Current Stock: " + prDetails.get("CurrentStock") + ", Recommended quantity: " + prDetails.get("Quantity") +
                        ", Expected Arrival Day: " + prDetails.get("ExpectedArrivalDays");
                feedbackForPM.add(feedback);
            }
        }

        System.out.println("\nCreating Purchase Purchase Requisition...");
        StringBuilder feedbackForPMFormatted = new StringBuilder("\n------ Purchase Requisition Details ------\n");
        feedbackForPMFormatted.append("PR ID: ").append(prID).append("\n");
        feedbackForPMFormatted.append("Creation Date: ").append(creationDate).append("\n");
        feedbackForPMFormatted.append("Created By: ").append(loggedInUsername).append("\n\n");

        feedbackForPMFormatted.append("Items in this PR:\n");
        for (String feedback : feedbackForPM) {
            feedbackForPMFormatted.append("- ").append(feedback).append("\n");
        }
        feedbackForPMFormatted.append("\n");

        for (Map<String, String> prDetails : prDetailsList) {
            prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails));
        }

        return feedbackForPMFormatted.toString();
    }

    private Map<String, List<String>> buildDictionaries(ItemDAO itemDAO, SupplierDAO supplierDAO, Map<String, String> itemDict) {
        List<String> items = itemDAO.getAllItems();
        List<String> suppliers = supplierDAO.getAllSuppliers();
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
        return supplierDict;
    }

    private List<Map<String, String>> addItemToPR(String selectedPRID, String creationDate, String loggedInUsername, ItemDAO itemDAO, SupplierDAO supplierDAO) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Build the dictionaries
        Map<String, String> itemDict = new HashMap<>();
        Map<String, List<String>> supplierDict = buildDictionaries(itemDAO, supplierDAO, itemDict);

        List<Map<String, String>> prDetailsList = new ArrayList<>();
        List<String> items = itemDAO.getAllItems();

        while (true) {
            System.out.println("Available items:\n");
            for (String item : items) {
                String[] details = item.split("\\$");
                String itemCodeDisplay = details[0];
                String itemNameDisplay = details[1];
                String currentStockDisplay = details[2];
                List<String> itemSuppliers = supplierDict.get(details[4]);

                // Convert supplier list to a list of supplier names
                List<String> supplierNames = itemSuppliers.stream().map(s -> s.split("\\$")[1]).collect(Collectors.toList());

                System.out.println(itemCodeDisplay + ": " + itemNameDisplay +
                        " (Current Stock: " + currentStockDisplay + ")" +
                        " - Supplied by: " + String.join(", ", supplierNames));
            }

            System.out.print("\nEnter Item Code (or 'exit' to finish): ");
            String itemCode = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(itemCode)) {
                break;
            }

            if (!itemDict.containsKey(itemCode)) {
                System.out.println("\nInvalid Item Code. Please try again.\n");
                continue;
            }

            int requiredQuantity = 0;
            while (true) {
                try {
                    System.out.print("\nEnter the recommend quantity for item " + itemCode + ": ");
                    requiredQuantity = Integer.parseInt(scanner.nextLine().trim());
                    break;  // 如果转换成功，退出循环
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
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
                prDetails.put("Quantity", String.valueOf(requiredQuantity));
                prDetails.put("CreatedBy", loggedInUsername);
                prDetails.put("SupplierContact", supplierDetails[2]);
                prDetails.put("ExpectedArrivalDays", supplierDetails[6]);
                prDetails.put("PRID", selectedPRID);
                prDetails.put("POStatus", "false");
                prDetailsList.add(prDetails);
            }

            System.out.println("\nItem " + selectedItemDetails[1] + " with recommend quantity " + requiredQuantity + " added to PR.\n");
        }
        return prDetailsList;
    }

    public String manualGeneratePR(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        String loggedInUsername = Login.getLoggedInUsername(); // Assuming you have this method.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String creationDate = sdf.format(new Date());
        String prID = "PR" + String.format("%03d", prDAO.getNextAvailablePRID());

        System.out.println("\n------ Manual Purchase Requisition Generation ------\n");

        // Use the new method to add items to the PR
        List<Map<String, String>> prDetailsList = addItemToPR(prID, creationDate, loggedInUsername, itemDAO, supplierDAO);

        // Check if any items were added
        if (prDetailsList.isEmpty()) {
            return "No items selected. PR generation aborted.";
        }

        // Save the added items to the database
        for (Map<String, String> prDetails : prDetailsList) {
            prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails));
        }

        // Display a summary of the added items
        System.out.println("\n------ Items Added to PR ------");
        for (Map<String, String> prDetails : prDetailsList) {
            System.out.println(prDetails.get("ProductName") +
                    " (Quantity: " + prDetails.get("Quantity") + ")" +
                    " with Supplier: " + prDetails.get("SupplierName"));
        }

        // Construct feedback for the Purchase Manager
        System.out.println("\nCreating a new Purchase Requisition...");
        StringBuilder feedbackForPM = new StringBuilder("\n------ Purchase Requisition Details ------\n");
        feedbackForPM.append("PR ID: ").append(prID).append("\n");
        feedbackForPM.append("Creation Date: ").append(creationDate).append("\n");
        feedbackForPM.append("Created By: ").append(loggedInUsername).append("\n\n");
        feedbackForPM.append("Items in this PR:\n");
        for (Map<String, String> prDetails : prDetailsList) {
            feedbackForPM.append("- Item: ").append(prDetails.get("ProductName"))
                    .append(" (Item Code: ").append(prDetails.get("ItemCode")).append(")")
                    .append(", Quantity Ordered: ").append(prDetails.get("Quantity"))
                    .append(", Supplier: ").append(prDetails.get("SupplierName"))
                    .append(", Expected Arrival Days: ").append(prDetails.get("ExpectedArrivalDays"))
                    .append("\n");
        }
        feedbackForPM.append("\n");

        return feedbackForPM.toString() + "Purchase Requisitions have been manually generated and saved.";
    }

    public void displayPRList(PurchaseRequisitionDAO prDAO) {
        List<String> allPRs = prDAO.getAllPRs();
        Map<String, String> prIDToCreator = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        for (String pr : allPRs) {
            String[] details = pr.split("\\$");
            String prID = details[0];
            String createdBy = details[9];

            if (prID != null && !prID.isEmpty() && !prID.equals("null")) {
                prIDToCreator.putIfAbsent(prID, createdBy);
            }
        }

        while (true) {
            System.out.println("\nAvailable PRs:");
            int counter = 1;
            Map<Integer, String> prMenu = new HashMap<>();

            for (Map.Entry<String, String> entry : prIDToCreator.entrySet()) {
                String prID = entry.getKey();
                String createdBy = entry.getValue();
                System.out.println(counter + ". " + prID + " - CREATED BY " + createdBy);
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


    public void displayPRDetails(String prID, PurchaseRequisitionDAO prDAO) {
        List<String> prDetails = prDAO.getPRDetails(prID);

        // 打印购买申请的标题和公共信息
        System.out.println("\n------ Purchase Requisition Details ------");
        System.out.println("PR ID: " + prID);
        String[] firstDetailArray = prDetails.get(0).split("\\$");
        System.out.println("Creation Date: " + firstDetailArray[10]);
        System.out.println("Created By: " + firstDetailArray[9]);
        System.out.println("PO Status: " + (firstDetailArray[12].equals("true") ? "Converted to PO" : "Not yet converted to PO"));
        System.out.println();

        // 打印每个物品的详细信息
        for (String detail : prDetails) {
            String[] detailsArray = detail.split("\\$");
            System.out.println("------------------------------");
            System.out.println("Item Code: " + detailsArray[1]);
            System.out.println("Product Name: " + detailsArray[2]);
            System.out.println("Category: " + detailsArray[3]);
            System.out.println("Price: RM"+ detailsArray[4]);
            System.out.println("Supplier Code: " + detailsArray[6]);
            System.out.println("Supplier Name: " + detailsArray[7]);
            System.out.println("Current Stock: " + detailsArray[4]);
            System.out.println("Recommended quantity: " + detailsArray[5]);
            System.out.println("Supplier Contact: " + detailsArray[8]);
            System.out.println("Expected Arrival Days: " + detailsArray[11]);
            System.out.println("------------------------------");
        }
    }

    public void addItem(String selectedPRID, ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);

        String loggedInUsername = Login.getLoggedInUsername();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String creationDate = sdf.format(new Date());

        // Use the new method to add items to the PR
        List<Map<String, String>> prDetailsList = addItemToPR(selectedPRID, creationDate, loggedInUsername, itemDAO, supplierDAO);

        if (!prDetailsList.isEmpty()) {
            System.out.println("\n------ Items to be Added to PR ------");
            for (Map<String, String> prDetails : prDetailsList) {
                System.out.println(prDetails.get("ProductName") +
                        " (Quantity: " + prDetails.get("Quantity") + ")" +
                        " with Supplier: " + prDetails.get("SupplierName"));
            }

            System.out.print("\nDo you confirm the additions? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(confirmation)) {
                for (Map<String, String> prDetails : prDetailsList) {
                    prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails));
                }
                System.out.println("Items added successfully to PR " + selectedPRID + "!");
            } else {
                System.out.println("Addition of items to PR cancelled.");
            }
        } else {
            System.out.println("No items were added to the PR.");
        }
    }

    private void editItemQuantity(String selectedPRID, PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Item Code of the item you wish to modify: ");
        String itemCode = scanner.nextLine().trim();

        // Load all PR details from file
        List<String> allPrDetailsFromFile = prDAO.getAllPRs();

        // Check if the item exists in the selected PR
        String matchingDetail = allPrDetailsFromFile.stream()
                .filter(detail -> detail.startsWith(selectedPRID + "$" + itemCode + "$"))
                .findFirst().orElse(null);

        if (matchingDetail == null) {
            System.out.println("Item not found in the selected PR.");
            return;
        }

        System.out.print("Enter the new recommend quantity for the item: ");
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        String[] details = matchingDetail.split("\\$");
        details[5] = String.valueOf(newQuantity);
        String updatedDetail = String.join("$", details);

        // Confirm the changes
        System.out.print("Do you confirm the changes? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (!"yes".equals(confirmation)) {
            System.out.println("Changes discarded.");
            return;
        }

        // Update the item quantity in the PR
        int indexToUpdate = allPrDetailsFromFile.indexOf(matchingDetail);
        allPrDetailsFromFile.set(indexToUpdate, updatedDetail);

        // Save the entire list (with all PR details) back to the file
        prDAO.saveUpdatedPRDetails(allPrDetailsFromFile);
        System.out.println("Quantity for item " + itemCode + " in " + selectedPRID + " updated to " + newQuantity);
    }

    private void deleteItem(String selectedPRID, PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Item Code of the item you wish to delete: ");
        String itemCode = scanner.nextLine().trim();

        // Load all PR details from file, not just the selected PR's details
        List<String> allPrDetailsFromFile = prDAO.getAllPRs();

        // Find the matching detail line
        String matchingDetail = allPrDetailsFromFile.stream()
                .filter(detail -> detail.startsWith(selectedPRID + "$" + itemCode + "$"))
                .findFirst().orElse(null);

        if (matchingDetail == null) {
            System.out.println("Item not found in the selected PR.");
            return;
        }

        System.out.print("Are you sure you want to delete this item? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if ("yes".equals(confirmation)) {
            allPrDetailsFromFile.remove(matchingDetail); // Remove the matching detail from the list
            // Save the entire list (with all PR details) back to the file
            prDAO.saveUpdatedPRDetails(allPrDetailsFromFile);
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Item deletion cancelled.");
        }
    }

    public void editMenu(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\t\t\tEdit Purchase Requisition Menu:");
            System.out.println("\n\t\t\t1. Edit Item Quantity");
            System.out.println("\t\t\t2. Add Item");
            System.out.println("\t\t\t3. Delete Item");
            System.out.println("\t\t\t4. Exit");
            System.out.print("Enter your choice: ");


            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select a number between 1 and 5.");
                continue;
            }

            if (choice >= 1 && choice <= 3) {
                List<String> allPRs = prDAO.getAllPRs();
                List<String> prIDs = new ArrayList<>();
                Map<Integer, String> prMenu = new HashMap<>();

                for (String pr : allPRs) {
                    String[] details = pr.split("\\$");
                    String prID = details[0];
                    String poStatus = details[details.length - 1];
                    if (!"true".equalsIgnoreCase(poStatus) && !prIDs.contains(prID) && !"null".equalsIgnoreCase(prID)) {
                        prIDs.add(prID);
                    }
                }

                if (prIDs.isEmpty()) {
                    System.out.println("No editable PRs available.");
                    continue;
                }

                System.out.println("\nAvailable PRs for editing:");
                for (int i = 0; i < prIDs.size(); i++) {
                    System.out.println((i + 1) + ". " + prIDs.get(i));
                    prMenu.put(i + 1, prIDs.get(i));
                }

                System.out.print("Select the PR number to edit: ");
                int prIndex;
                try {
                    prIndex = Integer.parseInt(scanner.nextLine().trim());
                    if (!prMenu.containsKey(prIndex)) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection. Please select a valid PR number.");
                    continue;
                }

                String selectedPRID = prMenu.get(prIndex);

                // Display the PR details before editing
                displayPRDetails(selectedPRID, prDAO);

                switch (choice) {
                    case 1 -> editItemQuantity(selectedPRID, prDAO);
                    case 2 -> addItem(selectedPRID, itemDAO, supplierDAO, prDAO);
                    case 3 -> deleteItem(selectedPRID, prDAO);
                    default -> {
                        System.out.println("Invalid choice. Please select a number between 1 and 5.");
                        continue;
                    }
                }
            } else if (choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice. Please select a number between 1 and 5.");
            }
        }
    }

    public void deletePR(PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);
        List<String> allPRs = prDAO.getAllPRs();
        List<String> prIDs = new ArrayList<>();
        Map<Integer, String> prMenu = new HashMap<>();

        // Extract PR IDs
        for (String pr : allPRs) {
            String prID = pr.split("\\$")[0];
            if (prID != null && !prID.isEmpty() && !prID.equals("null")) {
                if (!prIDs.contains(prID)) {
                    prIDs.add(prID);
                }
            }
        }

        while (true) {  // Keep running until user chooses to exit
            System.out.println("\nAvailable PRs:");
            int counter = 1;
            for (String prID : prIDs) {
                System.out.println(counter + ". " + prID);
                prMenu.put(counter, prID);
                counter++;
            }

            System.out.print("\nEnter the number of PR to delete (or 'exit' to finish): ");
            String choice = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(choice)) {
                break;
            }

            try {
                int chosenNumber = Integer.parseInt(choice);
                if (prMenu.containsKey(chosenNumber)) {
                    String selectedPRID = prMenu.get(chosenNumber);

                    // Display details of the selected PR (if you have such a method)
                    displayPRDetails(selectedPRID, prDAO);

                    System.out.print("Are you sure you want to delete this PR? (yes/no): ");
                    String confirm = scanner.nextLine().trim();

                    if ("yes".equalsIgnoreCase(confirm)) {
                        // Delete the selected PR
                        boolean success = prDAO.deletePR(selectedPRID);
                        if (success) {
                            System.out.println("PR successfully deleted.");
                            prIDs.remove(selectedPRID);  // Remove the deleted PR ID from the list
                            break;  // Exit the loop
                        } else {
                            System.out.println("An error occurred while deleting the PR.");
                        }
                    } else {
                        System.out.println("PR deletion cancelled.");
                    }
                } else {
                    System.out.println("\nInvalid choice. Please select a valid number.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.\n");
            }
        }
    }

}

