package domain;

import data.ItemDAO;
import data.PurchaseOrderDAO;
import data.SupplierDAO;
import utility.Utility;

import java.util.Date;
import java.util.List;

public class Item implements Entry {
    private String itemCode;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private String supplierId;
    private String category;
    private Date expiryDate;
    private boolean isInStock;
    private int minStockLevel;

    public Item(String itemCode, String itemName, int quantity, double unitPrice, String supplierId, String category, Date expiryDate, boolean isInStock, int minStockLevel) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplierId = supplierId;
        this.category = category;
        this.expiryDate = expiryDate;
        this.isInStock = isInStock;
        this.minStockLevel = minStockLevel;
    }

    public Item(){}

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setAvailable(boolean available) {
        isInStock = available;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    // Implementing the Entry interface methods
    public void add() {
        ItemDAO itemDAO = new ItemDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

        int itemCount = itemDAO.countItems(); // Assuming countItems() returns the existing number of items
        this.itemCode = String.format("IT%03d", itemCount + 1); // Formats the item code to "IT" followed by a 3-digit number

        System.out.print("Enter Item Name: ");
        this.itemName = Utility.readString(50);

        System.out.print("Enter Quantity: ");
        this.quantity = Utility.readInt();

        System.out.print("Enter Unit Price: ");
        this.unitPrice = Utility.readDouble();

        do {
            System.out.print("Enter Supplier ID: ");
            supplierId = Utility.readString(10);
            if (!supplierDAO.checkExistingSupplierID(supplierId)) {
                System.out.println("This Supplier ID does not exist. You need to add the supplier first. Please try again.");
                return;
            }
        } while (!supplierDAO.checkExistingSupplierID(supplierId));


        System.out.print("Enter Category: ");
        this.category = Utility.readString(20);

        System.out.print("Enter the expiry date: ");
        this.expiryDate = Utility.readDate();

        System.out.print("Enter Minimum Stock Level: ");
        this.minStockLevel = Utility.readInt();

        this.isInStock = this.quantity > this.minStockLevel;

        // Confirm addition
        System.out.println("\nAre you sure you want to save?");
        char confirm = Utility.readConfirmSelection();
        if (confirm == 'Y') {
            if (itemDAO.saveItem(this)) {
                System.out.println("Item successfully saved.");
            } else {
                System.out.println("Failed to save the item.");
            }
        } else {
            System.out.println("Item addition cancelled.");
        }
    }

    public void edit() {
        ItemDAO itemDAO = new ItemDAO();
        SupplierDAO supplierDAO = new SupplierDAO(); // To check for Supplier ID existence

        // Display the items to the user so they can pick which one to edit
        itemDAO.viewAllItems();

        System.out.print("Enter the Item Code of the item you want to edit: ");
        String itemCodeToEdit = Utility.readString(10);

        if (!itemDAO.checkDuplicateItemCode(itemCodeToEdit)) {
            System.out.print("Item Code does not exist.");
            return;
        }

        Item itemToEdit = itemDAO.getItemByItemCode(itemCodeToEdit);

        // Re-prompt for each attribute and show its current value
        System.out.print("Current Item Name: " + itemToEdit.getItemName() + ". Press Enter to keep: ");
        String newItemName = Utility.readString(50, itemToEdit.getItemName());

        System.out.print("Current Quantity: " + itemToEdit.getQuantity() + ". Press Enter to keep: ");
        int newQuantity = Utility.readInt(itemToEdit.getQuantity());

        System.out.print("Current Unit Price: " + itemToEdit.getUnitPrice() + ". Press Enter to keep: ");
        double newUnitPrice = Utility.readDouble(itemToEdit.getUnitPrice());

        // Check if the Supplier ID exists before accepting it
        String newSupplierId;
        do {
            System.out.print("Current Supplier ID: " + itemToEdit.getSupplierId() + ". Press Enter to keep: ");
            newSupplierId = Utility.readString(10, itemToEdit.getSupplierId());
            if (!supplierDAO.checkExistingSupplierID(newSupplierId)) {
                System.out.print("This Supplier ID does not exist. Please try again.");
            }
        } while (!supplierDAO.checkExistingSupplierID(newSupplierId));

        System.out.print("Current Category: " + itemToEdit.getCategory() + ". Press Enter to keep: ");
        String newCategory = Utility.readString(20, itemToEdit.getCategory());

        System.out.print("Current Expiry Date: " + itemToEdit.getExpiryDate() + ". Press Enter to keep: ");
        Date newExpiryDate = Utility.readDate(itemToEdit.getExpiryDate());

        System.out.print("Current Minimum Stock Level: " + itemToEdit.getMinStockLevel() + ". Press Enter to keep: ");
        int newMinStockLevel = Utility.readInt(itemToEdit.getMinStockLevel());

        // Determine newIsInStock based on newMinStockLevel and newQuantity
        boolean newIsInStock = newQuantity >= newMinStockLevel;

        // After collecting all new values, ask for confirmation
        System.out.print("Are you sure you want to update this item? ");
        char confirm = Utility.readConfirmSelection();

        if (confirm == 'Y' || confirm == 'y') {
            // Create a new Item object with the new data
            Item updatedItem = new Item(
                    itemCodeToEdit, newItemName, newQuantity, newUnitPrice,
                    newSupplierId, newCategory, newExpiryDate, newIsInStock,
                    newMinStockLevel
            );

            // Update the item in the data store
            if (itemDAO.editItem(itemCodeToEdit, updatedItem)) {
                System.out.println("Item successfully updated.");
            } else {
                System.out.println("Failed to update the item.");
            }
        } else {
            System.out.println("Item update cancelled.");
        }
    }




    public void delete() {
        ItemDAO itemDAO = new ItemDAO();

        // Display all items
        itemDAO.viewAllItems();

        // Read all existing item codes
        List<String> existingItemCodes = itemDAO.readItemCodes();

        // Ask user to enter itemCode
        String itemCode;
        while (true) {
            System.out.print("Enter the Item Code of the item you wish to delete: ");
            itemCode = Utility.readString(10);
            if (existingItemCodes.contains(itemCode)) {
                break;
            } else {
                System.out.println("Item code not found. Please try again.");
            }
        }

        // Confirm deletion
        System.out.println("\nAre you sure you want to delete and save?");
        char confirm = Utility.readConfirmSelection();
        if (confirm == 'Y') {
            if (itemDAO.deleteItem(itemCode)) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("Failed to delete the item.");
            }
        } else {
            System.out.println("Item deletion cancelled.");
        }
    }


    public void view() {
        System.out.println("\nItems List are printed as below: \n");
        new ItemDAO().viewAllItems();
    }

    public void searchItem_byCategory() {
        System.out.print("Enter the Item Category to search: ");
        String category = Utility.readString(10);
        new ItemDAO().searchItemsByCategory(category);
    }

        public void searchItem_byName() {
        System.out.print("Enter the Item Name to search: ");
        String itemName = Utility.readString(50);
        new ItemDAO().searchItemsByName(itemName);

    }

    public void addItemStockByPOID() {
        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        poDAO.getPendingPOs();  // This should print out the pending POs based on your previous code

        while (true) {  // This loop will allow the user to continue adding until they decide to stop
            System.out.print("Enter the PO ID for which you want to update the item stock: ");
            String poID = Utility.readString(10);  // Assuming Utility.readString(int) reads a string with a length limit

            // Ask for confirmation to update
            System.out.print("Are you sure you want to update the item stock for PO ID " + poID + "? (Y/N): ");
            char confirmUpdate = Utility.readConfirmSelection();  // Assuming Utility.readConfirmSelection() returns 'Y' or 'N'

            if (confirmUpdate == 'Y') {
                ItemDAO itemDAO = new ItemDAO();
                if (itemDAO.updateItemStockByPOID(poID, poDAO)) {
                    System.out.println("Item stock updated successfully!");
                } else {
                    System.out.println("Failed to update item stock.");
                }
            } else {
                System.out.println("Operation cancelled.");
            }

            // Ask if the user wants to continue adding
            System.out.print("Do you want to continue adding more items? ");
            char continueAdding = Utility.readConfirmSelection();  // Assuming Utility.readConfirmSelection() returns 'Y' or 'N'

            if (continueAdding == 'N') {
                break;  // Exit the loop if the user doesn't want to continue
            }
        }
    }
}
