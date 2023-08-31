package domain;

import data.ItemDAO;
import utility.Utility;

import java.util.Date;

public class Item implements Entry {
    private String itemCode;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private String supplierId;
    private String category;
    private Date expiryDate;
    private boolean isAvailable;
    private int minStockLevel;

//    public Item(String itemCode, String itemName, int quantity, double unitPrice, String supplierId, String category, Date expiryDate, boolean isAvailable, int minStockLevel) {
//        this.itemCode = itemCode;
//        this.itemName = itemName;
//        this.quantity = quantity;
//        this.unitPrice = unitPrice;
//        this.supplierId = supplierId;
//        this.category = category;
//        this.expiryDate = expiryDate;
//        this.isAvailable = isAvailable;
//        this.minStockLevel = minStockLevel;
//    }



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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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
        do {
            System.out.print("Enter Item Code: ");
            itemCode = Utility.readString(10);
            if (itemDAO.checkDuplicateItemCode(itemCode)) {
                System.out.println("This Item Code already exists. Please enter a unique Item Code.");
            }
        } while (itemDAO.checkDuplicateItemCode(itemCode));

        this.itemCode = itemCode;

        System.out.print("Enter Item Name: ");
        this.itemName = Utility.readString(50);

        System.out.print("Enter Quantity: ");
        this.quantity = Utility.readInt();

        System.out.print("Enter Unit Price: ");
        this.unitPrice = Utility.readDouble();

        System.out.print("Enter Supplier ID: ");
        this.supplierId = Utility.readString(10);

        System.out.print("Enter Category: ");
        this.category = Utility.readString(20);

        System.out.print("Enter the expiry date. ");
        this.expiryDate = Utility.readDate();

        System.out.print("Is the item available? ");
        this.isAvailable = Utility.readBoolean();

        System.out.print("Enter Minimum Stock Level: ");
        this.minStockLevel = Utility.readInt();


        if (itemDAO.saveItem(this)) {
            System.out.println("Item successfully saved.");
        } else {
            System.out.println("Failed to save the item.");
        }

    }

    public void edit() {
        // Logic to edit item
    }

    public void delete() {
        // Logic to delete item
    }

    public void view() {
        // Logic to view item details
    }

    // Additional methods specific to Item
    // ...
}
