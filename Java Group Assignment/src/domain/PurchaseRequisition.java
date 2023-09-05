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

    private PurchaseRequisitionDAO dao = new PurchaseRequisitionDAO();

    public PurchaseRequisition() {
        System.out.println("Creating a new Purchase Requisition...");
        this.salesManagerID = Login.getLoggedInUsername(); // 直接从Login类获取当前登录的用户名
        // Save the PR details using DAO
        dao.savePurchaseRequisition(this);
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

    public void setPrDetailsList(List<Map<String, String>> prDetailsList) {
        this.prDetailsList = prDetailsList;
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
                        ", Current Stock: " + prDetails.get("CurrentStock") + ", Quantity Order: " + prDetails.get("Quantity") +
                        ", Expected Arrival Day: " + prDetails.get("ExpectedArrivalDays");
                feedbackForPM.add(feedback);
            }
        }

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

    public String manualGeneratePR(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        String loggedInUsername = Login.getLoggedInUsername();
        List<String> items = itemDAO.getAllItems();
        List<String> suppliers = supplierDAO.getAllSuppliers();
        Map<String, String> itemDict = new HashMap<>();
        Map<String, List<String>> supplierDict = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

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

            System.out.print("\nEnter the required quantity for item " + itemCode + ": ");
            int requiredQuantity = Integer.parseInt(scanner.nextLine().trim());  // 读取并转换为整数

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
                prDetails.put("Quantity", String.valueOf(requiredQuantity));  // 保存用户输入的数量
                prDetails.put("CreatedBy", loggedInUsername);
                prDetails.put("SupplierContact", supplierDetails[2]);
                prDetails.put("ExpectedArrivalDays", supplierDetails[6]);
                prDetails.put("PRID", prID);  // Use the same PR ID for all items in this batch
                prDetails.put("POStatus", "false");  // Add this line to set the PO status as false
                prDetailsList.add(prDetails);
            }

            System.out.println("\nItem " + selectedItemDetails[1] + " with quantity " + requiredQuantity + " added to PR.\n");
        }

        if (prDetailsList.isEmpty()) {
            return "No items selected. PR generation aborted.";
        }

        for (Map<String, String> prDetails : prDetailsList) {
            prDAO.savePurchaseRequisition(new PurchaseRequisition(prDetails));
        }

        System.out.println("\n------ Items Added to PR ------");
        for (Map<String, String> prDetails : prDetailsList) {
            System.out.println(prDetails.get("ProductName") +
                    " (Quantity: " + prDetails.get("Quantity") + ")" +
                    " with Supplier: " + prDetails.get("SupplierName"));
        }

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
        List<String> prIDs = new ArrayList<>();
        Map<Integer, String> prMenu = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        for (String pr : allPRs) {
            String prID = pr.split("\\$")[0];
            if (prID != null && !prID.isEmpty() && !prID.equals("null")) {
                if (!prIDs.contains(prID)) {
                    prIDs.add(prID);
                }
            }
        }

        while (true) {
            System.out.println("\nAvailable PRs:");
            int counter = 1;
            for (String prID : prIDs) {
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

        // 打印购买申请的标题和公共信息
        System.out.println("------ Purchase Requisition Details ------");
        System.out.println("PR ID: " + prID);
        // 假设第一个详细信息包含公共信息，可以根据实际数据格式进行调整
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
            System.out.println("Supplier Code: " + detailsArray[6]);
            System.out.println("Supplier Name: " + detailsArray[7]);
            System.out.println("Current Stock: " + detailsArray[4]);
            System.out.println("Supplier Contact: " + detailsArray[8]);
            System.out.println("Expected Arrival Days: " + detailsArray[11]);
            System.out.println("------------------------------");
        }
    }

    private void addItem(ItemDAO itemDAO, SupplierDAO supplierDAO) {
        Scanner scanner = new Scanner(System.in);
        List<String> items = itemDAO.getAllItems();

        while (true) {
            System.out.println("\nAvailable items:");
            for (String item : items) {
                String[] details = item.split("\\$");
                System.out.println(details[0] + ": " + details[1] + " (Current Stock: " + details[2] + ")");
            }

            System.out.print("\nEnter Item Code (or 'exit' to finish): ");
            String itemCode = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(itemCode)) {
                break;
            }

            if (items.stream().noneMatch(item -> item.split("\\$")[0].equals(itemCode))) {
                System.out.println("Invalid Item Code. Please try again.");
                continue;
            }

            System.out.print("Enter the required quantity for the selected item: ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            // Extract details from the selected item
            String selectedItem = items.stream().filter(item -> item.split("\\$")[0].equals(itemCode)).findFirst().orElse(null);
            String[] selectedItemDetails = selectedItem.split("\\$");

            Map<String, String> prDetails = new HashMap<>();
            prDetails.put("ItemCode", selectedItemDetails[0]);
            prDetails.put("ProductName", selectedItemDetails[1]);
            prDetails.put("Quantity", String.valueOf(quantity));
            // ... [fill other details as required]

            prDetailsList.add(prDetails);
            System.out.println("\nItem " + selectedItemDetails[1] + " added to PR with quantity " + quantity + ".\n");
        }
    }

    private void changeSupplier(ItemDAO itemDAO, SupplierDAO supplierDAO) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Item Code of the item you wish to change the supplier for: ");
        String itemCode = scanner.nextLine().trim();

        // Fetch other suppliers for the item
        List<String> potentialSuppliers = supplierDAO.getSuppliersForItem(itemCode);

        if (potentialSuppliers.isEmpty()) {
            System.out.println("No other suppliers available for this item.");
            return;
        }

        System.out.println("Available suppliers for the item:");
        for (String supplier : potentialSuppliers) {
            System.out.println(supplier);
        }

        System.out.print("Enter the Supplier Code of the new supplier: ");
        String newSupplierCode = scanner.nextLine().trim();

        // Check if the selected supplier is valid for the item
        if (!potentialSuppliers.contains(newSupplierCode)) {
            System.out.println("Invalid Supplier Code.");
            return;
        }

        // Update the supplier in the PR
        for (Map<String, String> detail : prDetailsList) {
            if (detail.get("ItemCode").equals(itemCode)) {
                detail.put("SupplierCode", newSupplierCode);
                System.out.println("Supplier for item " + itemCode + " changed to " + newSupplierCode);
                break;
            }
        }
    }

    private void editItemQuantity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Item Code of the item you wish to modify: ");
        String itemCode = scanner.nextLine().trim();

        // Check if item exists in the PR
        if (!prDetailsList.stream().anyMatch(detail -> detail.get("ItemCode").equals(itemCode))) {
            System.out.println("Item not found in the PR.");
            return;
        }

        System.out.print("Enter the new quantity for the item: ");
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        // Update the item quantity in the PR
        for (Map<String, String> detail : prDetailsList) {
            if (detail.get("ItemCode").equals(itemCode)) {
                detail.put("Quantity", String.valueOf(newQuantity));
                System.out.println("Quantity for item " + itemCode + " updated to " + newQuantity);
                break;
            }
        }
    }

    private void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Item Code of the item you wish to delete: ");
        String itemCode = scanner.nextLine().trim();

        // Remove the item from the PR
        prDetailsList.removeIf(detail -> detail.get("ItemCode").equals(itemCode));
        System.out.println("Item " + itemCode + " removed from the PR.");
    }

    public void editMenu(ItemDAO itemDAO, SupplierDAO supplierDAO, PurchaseRequisitionDAO prDAO) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEdit Purchase Requisition Menu:");
            System.out.println("1. Edit Supplier");
            System.out.println("2. Edit Item Quantity");
            System.out.println("3. Delete Item");
            System.out.println("4. Add Item");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select a number between 1 and 5.");
                continue;
            }

            if (choice >= 1 && choice <= 4) {
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
                    case 1:
                        changeSupplier(itemDAO, supplierDAO);
                        break;
                    case 2:
                        editItemQuantity();
                        break;
                    case 3:
                        deleteItem();
                        break;
                    case 4:
                        addItem(itemDAO, supplierDAO);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 5.");
                        continue;
                }

                // Display the PR details after editing
                displayPRDetails(selectedPRID, prDAO);

                System.out.print("Do you confirm the changes? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if ("yes".equals(confirmation)) {
                    prDAO.savePurchaseRequisition(this);
                    System.out.println("Changes saved successfully!");
                } else {
                    System.out.println("Changes discarded.");
                    continue; // This will take the user back to the "Edit Purchase Requisition Menu"
                }
            } else if (choice == 5) {
                return;
            } else {
                System.out.println("Invalid choice. Please select a number between 1 and 5.");
            }
        }
    }
}

