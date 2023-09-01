package data;

import domain.PurchaseRequisition;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRequisitionDAO {
    private static final String FILE_PATH = "PRDetails.txt"; // Path to the text file where PR data is stored
    private static final String DELIMITER = "$"; // Delimiter used in the text file

    public boolean savePurchaseRequisition(PurchaseRequisition pr) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Create a string representation of the PurchaseRequisition object
            String prRecord = String.join(DELIMITER,
                    pr.getPRID(),
                    pr.getItemCode(),
                    String.valueOf(pr.getQuantity()),
                    pr.getRequiredDate(),
                    pr.getSupplierCode(),
                    pr.getSalesManagerID()
            );

            // Write the string representation of the PR to the text file
            writer.write(prRecord);
            writer.newLine(); // Add a new line to separate records
            return true; // Successfully saved the PR
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to save the PR
        }
    }

    public boolean checkDuplicatePRID(String prid) {
        List<String> existingPRIds = readPRIds();
        return existingPRIds.contains(prid);
    }

    private List<String> readPRIds() {
        List<String> prIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 1) {
                    continue; // skip lines with insufficient data
                }
                String existingPRId = fields[0];
                prIds.add(existingPRId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prIds;
    }

    public void displayPRDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.replace(DELIMITER, "\t"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
