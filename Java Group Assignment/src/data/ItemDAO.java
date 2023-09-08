package data;

import domain.Item;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ItemDAO {

    private static final String FILE_PATH = "Item.txt";
    private static final String DELIMITER = "$";

    public boolean saveItem(Item item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String expiryDateStr = (item.getExpiryDate() != null) ? sdf.format(item.getExpiryDate()) : "";

            String line = item.getItemCode() + DELIMITER +
                    item.getItemName() + DELIMITER +
                    item.getQuantity() + DELIMITER +
                    item.getUnitPrice() + DELIMITER +
                    item.getSupplierId() + DELIMITER +
                    item.getCategory() + DELIMITER +
                    expiryDateStr + DELIMITER +
                    item.isInStock() + DELIMITER +
                    item.getMinStockLevel() + "\n";

            writer.write(line);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkDuplicateItemCode(String itemCode) {
        List<String> itemCodes = readItemCodes();
        return itemCodes.contains(itemCode);
    }

    public List<String> readItemCodes() {
        List<String> itemCodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 1) {
                    continue; // skip lines with insufficient data
                }

                String existingItemCode = fields[0];
                itemCodes.add(existingItemCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemCodes;
    }

    public void viewAllItems() {
        // Print header
        System.out.printf("%-20s %-20s %-10s %-20s %-15s %-15s %-15s %-15s %-15s%n",
                "Item Code", "Item Name", "Quantity", "Unit Price(RM)", "Supplier ID",
                "Category", "Expiry Date", "Is In Stock", "Min Stock Level");

        // Print separator line
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // skip lines with insufficient data
                }

                // Print each field in a formatted manner
                System.out.printf("%-20s %-20s %-10s %-20s %-15s %-15s %-15s %-15s %-15s%n",
                        fields[0], fields[1], fields[2], fields[3], fields[4],
                        fields[5], fields[6].isEmpty() ? "N/A" : fields[6], fields[7], fields[8]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean deleteItem(String itemCode) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // skip lines with insufficient data
                }
                if (!fields[0].equals(itemCode)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void searchItemsByName(String itemName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean itemFound = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // Skip lines with insufficient data
                }

                String existingItemName = fields[1];

                if (existingItemName.equalsIgnoreCase(itemName)) {
                    // Print item details
                    System.out.println();
                    System.out.println("Item Code:           " + fields[0]);
                    System.out.println("Item Name:           " + fields[1]);
                    System.out.println("Quantity:            " + fields[2]);
                    System.out.println("Unit Price (RM):     " + fields[3]);
                    System.out.println("Supplier ID:         " + fields[4]);
                    System.out.println("Category:            " + fields[5]);
                    System.out.println("Expiry Date:         " + (fields[6].isEmpty() ? "N/A" : fields[6]));
                    System.out.println("Is In Stock :        " + fields[7]);
                    System.out.println("Minimum Stock Level: " + fields[8]);
                    System.out.println("----------------------------------------------------");
                    itemFound = true;
                }
            }
            if (!itemFound) {
                System.out.println("No items found with the provided name.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchItemsByCategory(String category) {
        // Print header
        System.out.printf("%-20s %-20s %-10s %-20s %-15s %-15s %-15s %-15s %-15s%n",
                "Item Code", "Item Name", "Quantity", "Unit Price (RM)", "Supplier ID",
                "Category", "Expiry Date", "Is In Stock", "Min Stock Level");

        // Print separator line
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean itemFound = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // skip lines with insufficient data
                }

                String existingCategory = fields[5]; // Assuming the category is the sixth field

                if (existingCategory.equalsIgnoreCase(category)) {
                    // Print each field in a formatted manner
                    System.out.printf("%-20s %-20s %-10s %-20s %-15s %-15s %-15s %-15s %-15s%n",
                            fields[0], fields[1], fields[2], fields[3], fields[4],
                            fields[5], fields[6].isEmpty() ? "N/A" : fields[6], fields[7], fields[8]);
                    itemFound = true;
                }
            }

            if (!itemFound) {
                System.out.println("No items found with the provided category.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean editItem(String itemCode, Item updatedItem) {
        List<String> lines = new ArrayList<>();
        boolean itemFound = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // skip lines with insufficient data
                }

                if (fields[0].equals(itemCode)) {
                    String expiryDateStr = (updatedItem.getExpiryDate() != null) ? sdf.format(updatedItem.getExpiryDate()) : "";
                    line = updatedItem.getItemCode() + DELIMITER +
                            updatedItem.getItemName() + DELIMITER +
                            updatedItem.getQuantity() + DELIMITER +
                            updatedItem.getUnitPrice() + DELIMITER +
                            updatedItem.getSupplierId() + DELIMITER +
                            updatedItem.getCategory() + DELIMITER +
                            expiryDateStr + DELIMITER +
                            updatedItem.isInStock() + DELIMITER +
                            updatedItem.getMinStockLevel();
                    itemFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!itemFound) {
            return false;
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Item getItemByItemCode(String itemCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 9) {
                    continue; // skip lines with insufficient data
                }
                if (fields[0].equals(itemCode)) {
                    // Found the item, now create an Item object
                    String itemName = fields[1];
                    int quantity = Integer.parseInt(fields[2]);
                    double unitPrice = Double.parseDouble(fields[3]);
                    String supplierId = fields[4];
                    String category = fields[5];
                    Date expiryDate = null;
                    try {
                        expiryDate = (fields[6].isEmpty()) ? null : sdf.parse(fields[6]);
                    } catch (ParseException e) {
                        System.out.println("Error parsing date for item " + itemCode);
                    }
                    boolean isInStock = Boolean.parseBoolean(fields[7]);
                    int minStockLevel = Integer.parseInt(fields[8]);

                    return new Item(itemCode, itemName, quantity, unitPrice, supplierId, category, expiryDate, isInStock, minStockLevel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countItems() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("Item.txt"))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean updateIsInStock(String itemCode, boolean newIsInStock) {
        ArrayList<String> lines = new ArrayList<>();
        boolean itemFound = false;

        // Read existing file
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length < 9) {
                    continue; // skip lines with insufficient data
                }

                if (parts[0].equals(itemCode)) {
                    // Update the isInStock status at index 7
                    parts[7] = String.valueOf(newIsInStock);
                    line = String.join(DELIMITER, parts);
                    itemFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!itemFound) {
            return false;
        }

        // Write updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean updateItemStockByPOID(String poID, PurchaseOrderDAO poDAO) {
        List<String> poDetails = poDAO.getPODetails(poID);
        if (poDetails.isEmpty()) {
            return false;
        }

        // Read all item data into memory
        Map<String, Integer> itemStocks = new HashMap<>();  // Item Code -> Stock quantity map
        List<String> allItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allItems.add(line);
                String[] fields = line.split("\\" + DELIMITER);
                String itemCode = fields[0];  // Assuming item code is at index 0
                int quantity = Integer.parseInt(fields[2]);  // Assuming quantity is at index 2
                itemStocks.put(itemCode, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // For each line in poDetails, find the item and update its stock
        for (String detail : poDetails) {
            String[] fields = detail.split("\\" + DELIMITER);
            String itemCode = fields[2];  // Assuming item code is at index 2 in PO details
            int quantity = Integer.parseInt(fields[6]);  // Assuming quantity is at index 6 in PO details
            itemStocks.put(itemCode, itemStocks.getOrDefault(itemCode, 0) + quantity);
        }

        // Save the updated stocks and "Is In Stock" status back to "item.txt"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String item : allItems) {
                String[] fields = item.split("\\" + DELIMITER);
                String itemCode = fields[0];
                if (itemStocks.containsKey(itemCode)) {
                    fields[2] = String.valueOf(itemStocks.get(itemCode));  // Update the stock quantity

                    int minStockLevel = Integer.parseInt(fields[8]);
                    boolean isInStock = itemStocks.get(itemCode) > minStockLevel;
                    fields[7] = String.valueOf(isInStock);  // Update the "Is In Stock" status
                }
                writer.write(String.join(DELIMITER, fields));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // After successfully updating the item stock and "Is In Stock" status, update PODetails.txt
        // Set the last attribute to true for this PO ID
        return poDAO.updateStockInStatus(poID);
    }


}
