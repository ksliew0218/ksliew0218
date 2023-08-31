package data;

import domain.Item;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    item.isAvailable() + DELIMITER +
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

    private List<String> readItemCodes() {
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

}
