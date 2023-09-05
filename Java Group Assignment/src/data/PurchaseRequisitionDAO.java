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
                    pr.getProductName(),
                    pr.getCategory(),
                    String.valueOf(pr.getStock()),
                    pr.getSupplierCode(),
                    pr.getSupplierName(),
                    pr.getSupplierContact(),
                    pr.getSalesManagerID(),
                    pr.getCreationDate(),
                    String.valueOf(pr.getExpectedArrivalDays()),
                    pr.getPOStatus()
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

    public int getNextAvailablePRID() {
        List<String> allPRs = getAllPRs();

        // 在此处添加检查：
        if (allPRs == null || allPRs.isEmpty()) {
            return 1;  // 如果allPRs是null或为空，则返回初始ID 1。
        }

        int maxID = 0;
        for (String pr : allPRs) {
            String[] details = pr.split("\\$");
            try {
                if(details[0].length() >= 5) {
                    int currentID = Integer.parseInt(details[0].substring(2, 5));  // 只提取PR后的三位数
                    if (currentID > maxID) {
                        maxID = currentID;
                    }
                }
            } catch (NumberFormatException e) {
                // 如果转换失败，则跳过这个条目并继续下一个
                continue;
            }
        }
        return maxID + 1;
    }

    // Return a list of all PRs
    public List<String> getAllPRs() {
        List<String> allPRs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allPRs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allPRs;
    }

    // Return details of a specific PR based on its ID
    public List<String> getPRDetails(String prID) {
        List<String> prDetails = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prID + DELIMITER)) { // Check if the line starts with the specified PRID
                    prDetails.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prDetails;
    }
}