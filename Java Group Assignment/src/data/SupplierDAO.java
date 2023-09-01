package data;

import domain.Supplier;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private static final String FILE_PATH = "Suppliers.txt"; // Path to the text file where supplier data is stored
    private static final String DELIMITER = "$"; // Delimiter used in the text file

    public boolean saveSupplier(Supplier supplier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // true for append mode
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateOfAssociation = sdf.format(supplier.getDateOfAssociation());

            // Create a string representation of the supplier object
            String supplierRecord = String.join(DELIMITER,
                    supplier.getSupplierId(),
                    supplier.getSupplierName(),
                    String.valueOf(supplier.getContactNumber()),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    dateOfAssociation,
                    String.valueOf(supplier.getDeliveryDay())
            );

            // Write the string representation of the supplier to the text file
            writer.write(supplierRecord);
            writer.newLine(); // Add a new line to separate records
            return true; // Successfully saved the supplier
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to save the supplier
        }
    }

    public boolean checkDuplicateSupplierID(String supplierId) {
        List<String> existingSupplierIds = readSupplierIds();
        return existingSupplierIds.contains(supplierId);
    }

    public boolean checkExistingSupplierID(String supplierId) {
        List<String> existingSupplierIds = readSupplierIds();
        return existingSupplierIds.contains(supplierId);
    }
    private List<String> readSupplierIds() {
        List<String> supplierIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 1) {
                    continue; // skip lines with insufficient data
                }
                String existingSupplierId = fields[0];
                supplierIds.add(existingSupplierId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supplierIds;
    }

}
