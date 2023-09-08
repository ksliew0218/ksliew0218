package data;

import domain.PurchaseRequisition;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utility.Utility;

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

    public List<String> getPRDetailsByPRID(String prID) {
        List<String> detailsForPRID = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prID + "$")) {
                    detailsForPRID.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailsForPRID;
    }


    /**
     * 获取所有 poStatus 为 false 的 PR
     * @return List of PRs with poStatus = false
     */
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




    public void updateItemQuantityInPR(String prID, String itemCode, int newQuantity) {
        // Load all PR details from file
        List<String> allPrDetails = getAllPRs();

        // Check if the item exists in the selected PR
        String matchingDetail = allPrDetails.stream()
                .filter(detail -> detail.startsWith(prID + "$" + itemCode + "$"))
                .findFirst().orElse(null);

        if (matchingDetail == null) {
            System.out.println("Item not found in the selected PR.");
            return;
        }

        // Update the item quantity in the detail string
        String[] details = matchingDetail.split("\\$");
        details[5] = String.valueOf(newQuantity);  // Assuming the 6th field is the quantity
        String updatedDetail = String.join("$", details);

        // Replace the old detail string with the updated one
        int indexToUpdate = allPrDetails.indexOf(matchingDetail);
        allPrDetails.set(indexToUpdate, updatedDetail);

        // Save the entire list (with all PR details) back to the file
        saveUpdatedPRDetails(allPrDetails);
    }

    public void updatePRDetails(String selectedPRID, List<String> itemsToBeAddedToPO) {
        // 从文件或其他数据源中读取原有的 PRDetails
        List<String> originalDetails = getPRDetails(selectedPRID);

        // 创建一个新的 List 来存储更新后的 details
        List<String> updatedDetails = new ArrayList<>();

        for (String originalDetail : originalDetails) {
            String[] originalArray = originalDetail.split("\\$");
            String originalItemCode = originalArray[1];

            // 查找是否有对应的新 detail
            String newItemDetail = itemsToBeAddedToPO.stream()
                    .filter(item -> item.split("\\$")[1].equals(originalItemCode))
                    .findFirst().orElse(null);

            if (newItemDetail != null) {
                // 替换为新的 detail
                updatedDetails.add(newItemDetail);
            } else {
                // 保留原有的 detail
                updatedDetails.add(originalDetail);
            }
        }

        // 现在，你可以将 updatedDetails 写回到 PRDetails.txt 文件或其他数据源中
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PRDetails.txt", false))) {  // false 表示不追加，而是覆盖文件
            for (String updatedDetail : updatedDetails) {
                bw.write(updatedDetail);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to PRDetails.txt: " + e.getMessage());
        }
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