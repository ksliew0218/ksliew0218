package data;

import domain.PurchaseRequisition;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseRequisitionDAO {
    private static final String FILE_PATH = "PRDetails.txt"; // Path to the text file where PR data is stored
    private static final String DELIMITER = "$"; // Delimiter used in the text file

    public void savePurchaseRequisition(PurchaseRequisition pr) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Create a string representation of the PurchaseRequisition object
            String prRecord = String.join(DELIMITER,

                    pr.getPRID(),
                    pr.getItemCode(),
                    pr.getProductName(),
                    pr.getCategory(),
                    String.valueOf(pr.getStock()),
                    String.valueOf(pr.getQuantity()),
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextAvailablePRID() {
        List<String> allPRs = getAllPRs();

        // 在此处添加检查：
        if (allPRs == null || allPRs.isEmpty()) {
            return 1;
        }

        int maxID = 0;
        for (String pr : allPRs) {
            String[] details = pr.split("\\$");
            try {
                if(details[0].length() >= 5) {
                    int currentID = Integer.parseInt(details[0].substring(2, 5));
                    if (currentID > maxID) {
                        maxID = currentID;
                    }
                }
            } catch (NumberFormatException e) {
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

    public boolean saveUpdatedPRDetails(List<String> updatedDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (String detail : updatedDetails) {
                writer.write(detail);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String> getAllEditablePRs() {
        List<String> editablePRs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("PRDetails.txt"))) {
            editablePRs = br.lines()
                    .filter(line -> !line.split("\\$")[line.split("\\$").length - 1].equalsIgnoreCase("true"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editablePRs;
    }

    public boolean updatePOStatus(String prID, boolean status) {
        List<String> lines = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Reading the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Update the lines
        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] fields = lines.get(i).split("\\$");
            if (fields[0].equals(prID)) {
                fields[fields.length - 1] = status ? "true" : "false";
                lines.set(i, String.join("$", fields));
                updated = true;
            }
        }

        // Write back to the file if updated
        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return updated;
    }

    public boolean deletePR(String prID) {
        List<String> allPRs = getAllPRs();
        List<String> updatedPRs = new ArrayList<>();

        for (String pr : allPRs) {
            String currentPRID = pr.split("\\$")[0];
            if (!currentPRID.equals(prID)) {
                updatedPRs.add(pr);
            }
        }

        // Write the updated PR list back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String updatedPR : updatedPRs) {
                writer.write(updatedPR);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}