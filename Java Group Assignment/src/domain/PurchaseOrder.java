package domain;

import java.util.*;

import data.*;
import utility.Utility;


public class PurchaseOrder {
    private String POID;
    private String PRID;  // Related Purchase Requisition ID
    private String creationDate;
    private String createdBy;
    private Map<String, Map<String, String>> items;  // Item Code and its details

    public PurchaseOrder() {
        this.items = new HashMap<>();
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public String getPRID() {
        return PRID;
    }

    public void setPRID(String PRID) {
        this.PRID = PRID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Map<String, String>> getItems() {
        return items;
    }

    public void setItems(Map<String, Map<String, String>> items) {
        this.items = items;
    }

    public void generatePOFromPR(PurchaseRequisitionDAO prDAO, PurchaseOrderDAO poDAO, SupplierDAO supplierDAO, ItemDAO itemDAO) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Map<String, String>> itemsForNewPO = new HashMap<>();
        int totalAmount = 0;

        // Step 1: Display all available PRs and let user select one
        List<String> allEditablePRs = prDAO.getAllEditablePRs();
        Map<Integer, String> prMap = displaySelectablePRs(allEditablePRs);
        int selectedPRIndex = -1;
        while (true) {
            System.out.print("\nPlease select the PR number for which you want to generate a PO: ");
            try {
                selectedPRIndex = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (!prMap.containsKey(selectedPRIndex)) {
                    System.out.println("Invalid Input! Please enter a valid number.");
                    continue;
                }
                break;  // Break the loop if valid input
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Consume invalid token
            }
        }

        String selectedPRID = prMap.get(selectedPRIndex);
        List<String> selectedPRDetails = prDAO.getPRDetails(selectedPRID);

        while (true) {
            // Step 2: Display details of the selected PR
            System.out.println("\n------ Purchase Requisition Details ------: \n");
            PurchaseRequisition pr = new PurchaseRequisition();
            pr.displayPRDetails(selectedPRID, prDAO);

            // Step 3: Let user select an item code and quantity
            System.out.print("Please enter the item code you want to add to the PO (or 'exit' to quit): ");
            String itemCode = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(itemCode)) {
                break;
            }

            String matchingDetail = selectedPRDetails.stream()
                    .filter(detail -> detail.startsWith(selectedPRID + "$" + itemCode + "$"))
                    .findFirst().orElse(null);

            if (matchingDetail == null) {
                System.out.println("Item Code not found");
                continue;
            }

            String[] itemDetails = matchingDetail.split("\\$");
            System.out.println("Recommend Quantity: " + itemDetails[5]);
            System.out.print("Enter Order Quantity (Or press 'Enter' to use recommend quantity): ");
            int newQuantity = Utility.readInt(Integer.parseInt(itemDetails[5]));

            // Add to itemsForNewPO
            Map<String, String> itemInfo = new HashMap<>();
            itemInfo.put("ItemName", itemDetails[2]);
            itemInfo.put("Category", itemDetails[3]);
            itemInfo.put("Price", itemDetails[4]);
            itemInfo.put("Quantity", String.valueOf(newQuantity));
            itemInfo.put("SupplierID", itemDetails[6]);
            itemInfo.put("SupplierName", itemDetails[7]);
            itemInfo.put("SupplierContact", itemDetails[8]);
            itemInfo.put("ExpectedArrivalDays", itemDetails[11]);
            itemsForNewPO.put(itemCode, itemInfo);

            // Update totalAmount
            totalAmount += newQuantity * Integer.parseInt(itemDetails[4]);
        }

// Step 4: Confirm with user and save the PO
        System.out.println("\nYou have added the following items：");
        for (Map.Entry<String, Map<String, String>> entry : itemsForNewPO.entrySet()) {
            String itemCode = entry.getKey();
            Map<String, String> itemDetails = entry.getValue();
            System.out.println("Item Code: " + itemCode + ", Item Name: " + itemDetails.get("ItemName") + ", Quantity: " + itemDetails.get("Quantity"));
        }

        System.out.print("\nAre you sure you want to generate a PO? (yes/no): ");
        String confirmChoice = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirmChoice)) {
            // Create a new PurchaseOrder object
            PurchaseOrder newPO = new PurchaseOrder();
            String newPOID = "PO" + String.format("%03d", poDAO.getNextPOID());  // Assuming you have a method to get the next unique PO ID
            newPO.setPOID(newPOID);
            newPO.setPRID(selectedPRID);
            newPO.setCreatedBy("CurrentUserName");  // Replace with the username of the person who is currently logged in
            newPO.setCreationDate("CurrentDate");  // Replace with the current date
            newPO.setItems(itemsForNewPO);

            // Set the total amount
            newPO.setTotalAmount(totalAmount);

            // Save the new PO
            boolean success = poDAO.savePurchaseOrder(newPO);
            if (success) {
                System.out.println("PO successfully generated!");
                displayPODetails(newPOID, poDAO);

                boolean updateStatusSuccess = prDAO.updatePOStatus(selectedPRID, true);
                if (updateStatusSuccess) {
                    System.out.println("PO status successfully updated!");
                } else {
                    System.out.println("An error occurred while updating the PO status.");
                }
            } else {
                System.out.println("An error occurred while generating the PO.");
            }
        }
    }

    public Map<Integer, String> displaySelectablePRs(List<String> allPRs) {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> prMap = new HashMap<>();

        HashSet<String> uniquePRIDs = new HashSet<>();

        // 提取所有唯一的 PR IDs
        for (String prDetail : allPRs) {
            String[] fields = prDetail.split("\\$");
            uniquePRIDs.add(fields[0]);
        }

        System.out.println("\nAvailable PR list: ");
        int index = 1;
        for (String prID : uniquePRIDs) {
            System.out.println(index + ". " + prID);
            prMap.put(index, prID);
            index++;
        }

        return prMap;
    }

    private int totalAmount;  // 添加这一行来存储总金额。

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void displayPODetails(String poID, PurchaseOrderDAO poDAO) {
        List<String> poDetails = poDAO.getPODetails(poID);

        // Print Purchase Order details
        System.out.println("=======================================");
        System.out.println("========= Purchase Order Details ======");
        System.out.println("=======================================");
        // Assume the first detail contains public information; adjust based on actual data format
        String[] firstDetailArray = poDetails.get(0).split("\\$");
        System.out.println("PO ID: " + poID);
        System.out.println("Related PR ID: " + firstDetailArray[1]);
        System.out.println("Creation Date: " + firstDetailArray[2]);
        System.out.println("Created By: " + firstDetailArray[3]);
        System.out.println("Stock In Status: " + (firstDetailArray[14].equals("true") ? "Done Stock In" : "Not yet Stock In"));
        System.out.println("---------------------------------------");

        // Print each item's detailed information
        for (String detail : poDetails) {
            String[] detailsArray = detail.split("\\$");
            System.out.println("------------------------------");
            System.out.println("Item Code: " + detailsArray[4]);
            System.out.println("Product Name: " + detailsArray[5]);
            System.out.println("Category: " + detailsArray[6]);
            System.out.println("Price: " + detailsArray[7]);
            System.out.println("Quantity: " + detailsArray[8]);
            System.out.println("Supplier Code: " + detailsArray[9]);
            System.out.println("Supplier Name: " + detailsArray[10]);
            System.out.println("Supplier Contact: " + detailsArray[11]);
            System.out.println("Expected Arrival Days: " + detailsArray[12]);
            System.out.println("------------------------------");
        }

        // Print total amount; this can be calculated based on your actual data
        System.out.println("Total Amount: [Calculate based on item details]");
        System.out.println("=======================================");
    }

    public void displayPOList(PurchaseOrderDAO poDAO) {
        List<String> allPOs = poDAO.getAllPOs();
        List<String> poIDs = new ArrayList<>();
        Map<Integer, String> poMenu = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        // 处理空文件或没有可用的POs
        if (allPOs == null || allPOs.isEmpty()) {
            System.out.println("No available POs.");
            return;
        }

        for (String po : allPOs) {
            String poID = po.split("\\$")[0];
            if (poID != null && !poID.isEmpty() && !poID.equals("null")) {
                if (!poIDs.contains(poID)) {
                    poIDs.add(poID);
                }
            }
        }

        // 再次检查以确保poIDs也不是空的
        if (poIDs.isEmpty()) {
            System.out.println("No available POs.");
            return;
        }

        while (true) {
            System.out.println("\nAvailable POs:");
            int counter = 1;
            for (String poID : poIDs) {
                System.out.println(counter + ". " + poID);
                poMenu.put(counter, poID);
                counter++;
            }

            System.out.print("\nEnter the number of PO to view details (or 'exit' to finish): ");
            String choice = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(choice)) {
                break;
            }

            try {
                int chosenNumber = Integer.parseInt(choice);
                if (poMenu.containsKey(chosenNumber)) {
                    displayPODetails(poMenu.get(chosenNumber), poDAO);
                } else {
                    System.out.println("\nInvalid choice. Please select a valid number.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.\n");
            }
        }
    }


    public void deletePO(PurchaseOrderDAO poDAO) {
        Scanner scanner = new Scanner(System.in);
        List<String> allPOs = poDAO.getAllPOs();
        List<String> poIDs = new ArrayList<>();
        Map<Integer, String> poMenu = new HashMap<>();

        for (String po : allPOs) {
            String poID = po.split("\\$")[0];
            if (poID != null && !poID.isEmpty() && !poID.equals("null")) {
                if (!poIDs.contains(poID)) {
                    poIDs.add(poID);
                }
            }
        }

        while (true) {  // This will keep running until user chooses to exit
            System.out.println("\nAvailable POs:");
            int counter = 1;
            for (String poID : poIDs) {
                System.out.println(counter + ". " + poID);
                poMenu.put(counter, poID);
                counter++;
            }

            System.out.print("\nEnter the number of PO to delete (or 'exit' to finish): ");
            String choice = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(choice)) {
                break;
            }

            try {
                int chosenNumber = Integer.parseInt(choice);
                if (poMenu.containsKey(chosenNumber)) {
                    String selectedPOID = poMenu.get(chosenNumber);

                    // Display details of the selected PO
                    displayPODetails(selectedPOID, poDAO);

                    System.out.print("Are you sure you want to delete this PO? (yes/no): ");
                    String confirm = scanner.nextLine().trim();

                    if ("yes".equalsIgnoreCase(confirm)) {
                        // Delete the selected PO
                        boolean success = poDAO.deletePO(selectedPOID);
                        if (success) {
                            System.out.println("PO successfully deleted.");
                            poIDs.remove(selectedPOID);  // Remove the deleted PO ID from the list
                            break;  // Exit the loop
                        } else {
                            System.out.println("An error occurred while deleting the PO.");
                        }
                    } else {
                        System.out.println("PO deletion cancelled.");
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
