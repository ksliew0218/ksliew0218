package domain;

import data.DailySalesDAO;
import data.ItemDAO;
import utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailySales implements Entry {

    private Date dateOfSale;
    private String itemCode;
    private String itemName;
    private int quantitySold;
    private double sellingPrice;
    private double tax;
    private double totalPriceOfItem;
    private double totalAmount;

    public DailySales(String itemCode, String itemName, int quantitySold, double sellingPrice, double tax, double totalPriceOfItem) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.sellingPrice = sellingPrice;
        this.tax = tax;
        this.totalPriceOfItem = totalPriceOfItem;
        this.dateOfSale = new Date();
    }

    public DailySales(Date dateOfSale, String itemCode, String itemName, int quantitySold, double sellingPrice, double tax, double totalPriceOfItem) {
        this.dateOfSale = dateOfSale;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.sellingPrice = sellingPrice;
        this.tax = tax;
        this.totalPriceOfItem = totalPriceOfItem;

    }

    public DailySales(){}

    public double calculateSellingPrice(double unitPrice) {
        return unitPrice * 1.2;  // assuming a markup of 20%
    }

    public String getDateOfSale() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateOfSale);
    }


    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getTax() {
        return tax;
    }

    public void setTotalPriceOfItem(double totalPriceOfItem) {
        this.totalPriceOfItem = totalPriceOfItem;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalPriceOfItem() {
        return this.totalPriceOfItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void add() {
        ItemDAO itemDAO = new ItemDAO();
        DailySalesDAO dailySalesDAO = new DailySalesDAO();
        int counter = 0;
        int maxItems = itemDAO.countItems();
        HashSet<String> addedItemCodes = new HashSet<>();

        itemDAO.viewAllItems();

        do {
            System.out.print("Enter the Item Code of the item sold: ");
            String itemCodeSold = Utility.readString(10);

            if (addedItemCodes.contains(itemCodeSold)) {
                System.out.println("This item code has already been added.");
                continue;
            }

            if (!itemDAO.checkDuplicateItemCode(itemCodeSold)) {
                System.out.println("Item Code does not exist.");
                continue;
            }

            Item soldItem = itemDAO.getItemByItemCode(itemCodeSold);

            System.out.print("Enter the Quantity Sold: ");
            int quantitySold = Utility.readInt();

            if (quantitySold > soldItem.getQuantity()) {
                System.out.println("Insufficient stock. Sales entry cancelled.");
                break;
            }

            double unitPrice = soldItem.getUnitPrice();
            double sellingPrice = calculateSellingPrice(unitPrice);
            double tax = sellingPrice * 0.06 * quantitySold;
            double totalPriceOfItem = (sellingPrice * quantitySold) + tax;

            // Show confirmation prompt
            System.out.println("Confirm the following sales entry:");
            System.out.println("Item Code: " + itemCodeSold);
            System.out.println("Item Name: " + soldItem.getItemName());
            System.out.println("Quantity Sold: " + quantitySold);
            System.out.println("Selling Price: " + String.format("%.2f", sellingPrice));
            System.out.println("Tax: " + String.format("%.2f", tax));
            System.out.println("Total Price of Item: " + String.format("%.2f", totalPriceOfItem));
            System.out.print("Would you like to confirm this entry? ");
            char confirmEntry = Utility.readConfirmSelection();

            if (confirmEntry == 'Y' || confirmEntry == 'y') {
                // Save the entry if the user confirms
                DailySales dailySale = new DailySales(itemCodeSold, soldItem.getItemName(), quantitySold, sellingPrice, tax, totalPriceOfItem);

                if (dailySalesDAO.saveDailySales(dailySale)) {
                    System.out.println("Daily sales entry successfully added.");
                } else {
                    System.out.println("Failed to add daily sales entry.");
                }

                // Update the stock
                soldItem.setQuantity(soldItem.getQuantity() - quantitySold);

                // Update the item's stock in the ItemDAO
                itemDAO.editItem(itemCodeSold, soldItem);

                // Update isInStock status
                boolean newIsInStock = soldItem.getQuantity() > soldItem.getMinStockLevel();
                itemDAO.updateIsInStock(itemCodeSold, newIsInStock);

                counter++;
                addedItemCodes.add(itemCodeSold);
            } else {
                System.out.println("Sales entry cancelled.");
            }

            if (counter < maxItems) {
                System.out.print("Would you like to add another item? ");
                char confirm = Utility.readConfirmSelection();
                if (confirm != 'Y' && confirm != 'y') {
                    break;
                }
            }
        } while (counter < maxItems);
    }

    @Override
    public void edit() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void view() {
        new DailySalesDAO().viewAllSales();
    }

    public void searchByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.print("Enter the date to search for sales: ");
        Date date = Utility.readDate();

        DailySalesDAO dailySalesDAO = new DailySalesDAO();

        // Check if any sales exist for the entered date
        List<DailySales> salesList = dailySalesDAO.getSalesByDate(date);

        if (salesList.isEmpty()) {
            System.out.println("No sales records found for the entered date. Exiting to menu.");
            return;
        }

        dailySalesDAO.printSalesByDate(date);
    }

}
