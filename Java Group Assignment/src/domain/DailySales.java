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

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTax(double tax) {
        this.tax = tax;
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
        DailySalesDAO dailySalesDAO = new DailySalesDAO();
        ItemDAO itemDAO = new ItemDAO();

        System.out.print("Enter the date of the sale you want to edit: ");
        Date dateToEdit = Utility.readDate();

        List<DailySales> salesOnDate = dailySalesDAO.getSalesByDate(dateToEdit);

        if (salesOnDate.isEmpty()) {
            System.out.println("No sales entries found for this date.");
            return;
        }

        // Display sales entries on that date
        dailySalesDAO.printSalesByDate(dateToEdit);

        System.out.print("\nEnter the Item Code of the sale you want to edit: ");
        String itemCodeToEdit = Utility.readString(10);

        DailySales dailySaleToEdit = null;
        for (DailySales sale : salesOnDate) {
            if (sale.getItemCode().equals(itemCodeToEdit)) {
                dailySaleToEdit = sale;
                break;
            }
        }

        if (dailySaleToEdit == null) {
            System.out.println("No sales entry found for the given Item Code on this date.");
            return;
        }

        // Re-prompt for new quantity sold
        System.out.print("Current Quantity Sold: " + dailySaleToEdit.getQuantitySold() + ". Press Enter to keep: ");
        int newQuantitySold = Utility.readInt(dailySaleToEdit.getQuantitySold());

        System.out.print("Current Selling Price: " + dailySaleToEdit.getSellingPrice() + ". Press Enter to keep: ");
        double newSellingPrice = Utility.readDouble(dailySaleToEdit.getSellingPrice());
        double newTax = newSellingPrice * 0.06 * newQuantitySold;
        double newTotalPriceOfItem = (newSellingPrice * newQuantitySold) + newTax;

        System.out.print("Are you sure you want to edit this sales entry? ");
        char confirm = Utility.readConfirmSelection();

        if (confirm == 'Y' || confirm == 'y') {
            // Fetch the corresponding Item object to update its stock
            Item itemToUpdate = itemDAO.getItemByItemCode(itemCodeToEdit);
            int stockDifference = newQuantitySold - dailySaleToEdit.getQuantitySold();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() - stockDifference);

            // Update the item's stock and isInStock status in the ItemDAO
            itemDAO.editItem(itemCodeToEdit, itemToUpdate);

            // Update isInStock status
            boolean newIsInStock = itemToUpdate.getQuantity() > itemToUpdate.getMinStockLevel();
            itemDAO.updateIsInStock(itemCodeToEdit, newIsInStock);

            // Update the DailySales entry with new data
            dailySaleToEdit.setQuantitySold(newQuantitySold);
            dailySaleToEdit.setSellingPrice(newSellingPrice); // Updating Selling Price
            dailySaleToEdit.setTax(newTax); // Updating Tax
            dailySaleToEdit.setTotalPriceOfItem(newTotalPriceOfItem);
            dailySaleToEdit.setTotalAmount(newTotalPriceOfItem); // Assuming total amount is same as total price of item

            // Update the DailySales entry in the txt file
            if (dailySalesDAO.editDailySales(dateToEdit, itemCodeToEdit, dailySaleToEdit)) {
                System.out.println("Sales entry successfully edited.");
            } else {
                System.out.println("Failed to edit sales entry.");
            }
        } else {
            System.out.println("Sales entry edit cancelled.");
        }
    }


    @Override
    public void delete() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.print("Enter the date of the sales entry you want to delete: ");
        Date date = Utility.readDate();

        DailySalesDAO dailySalesDAO = new DailySalesDAO();
        List<DailySales> salesList = dailySalesDAO.getSalesByDate(date);

        if (salesList.isEmpty()) {
            System.out.println("No sales entries found for the given date.");
            return;
        }

        // Display the sales entries for the given date
        dailySalesDAO.printSalesByDate(date);

        System.out.print("\n\nEnter the Item Code of the sales entry to delete: ");
        String itemCode = Utility.readString(10);

        DailySales deletedEntry = null;
        for (DailySales ds : salesList) {
            if (ds.getItemCode().equals(itemCode)) {
                deletedEntry = ds;
                break;
            }
        }

        if (deletedEntry == null) {
            System.out.println("Sales entry with the given Item Code not found.");
            return;
        }

        System.out.print("Are you sure you want to delete this entry? ");
        char confirm = Utility.readConfirmSelection();

        if (confirm == 'Y' || confirm == 'y') {
            if (dailySalesDAO.deleteSalesEntry(date, itemCode)) {
                System.out.println("Sales entry successfully deleted.");

                // Update the stock and isInStock status
                ItemDAO itemDAO = new ItemDAO();
                Item item = itemDAO.getItemByItemCode(itemCode);

                item.setQuantity(item.getQuantity() + deletedEntry.getQuantitySold());

                itemDAO.editItem(itemCode, item);

                boolean newIsInStock = item.getQuantity() > item.getMinStockLevel();
                itemDAO.updateIsInStock(itemCode, newIsInStock);

            } else {
                System.out.println("Failed to delete the sales entry.");
            }
        } else {
            System.out.println("Sales entry not deleted.");
        }
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
