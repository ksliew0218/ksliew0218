package data;

import domain.PurchaseOrder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PurchaseOrderDAO {
    private static final String FILE_PATH = "PODetails.txt";
    private static final String DELIMITER = "$";

    public boolean savePurchaseOrder(PurchaseOrder po) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Map.Entry<String, Map<String, String>> entry : po.getItems().entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(po.getPOID()).append(DELIMITER)
                        .append(po.getPRID()).append(DELIMITER);
                Map<String, String> itemDetails = entry.getValue();
                sb.append(entry.getKey()).append(DELIMITER)
                        .append(itemDetails.get("ItemName")).append(DELIMITER)
                        .append(itemDetails.get("Category")).append(DELIMITER)
                        .append(itemDetails.get("Price")).append(DELIMITER)
                        .append(itemDetails.get("Quantity")).append(DELIMITER)
                        .append(itemDetails.get("SupplierID")).append(DELIMITER)
                        .append(itemDetails.get("SupplierName")).append(DELIMITER)
                        .append(itemDetails.get("SupplierContact")).append(DELIMITER)
                        .append(itemDetails.get("ExpectedArrivalDays")).append(DELIMITER)
                        .append(po.getCreatedBy()).append(DELIMITER)
                        .append(po.getCreationDate()).append(DELIMITER)
                        .append(po.getTotalAmount()).append(DELIMITER)
                        .append("false");  // StockInStatus

                writer.write(sb.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    public int getNextPOID() {
        int maxPOID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                String currentPOID = fields[0].substring(2);  // 假设 POID 格式为 "PO001"，这将提取 "001"。
                int currentID = Integer.parseInt(currentPOID);
                if (currentID > maxPOID) {
                    maxPOID = currentID;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        if (maxPOID == 0) {
            return 1;
        }

        return maxPOID + 1;
    }

    public List<String> getAllPOs() {
        List<String> allPOs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allPOs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allPOs;
    }

    public List<String> getPODetails(String poID) {
        List<String> poDetails = new ArrayList<>();
        try {
            File file = new File("PODetails.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(poID + "$")) {
                    poDetails.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the PO details.");
            e.printStackTrace();
        }
        return poDetails;
    }

    public boolean updateStockInStatus(String poID) {
        List<String> updatedPODetails = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields[0].equals(poID)) {
                    // Update StockInStatus to true
                    line = line.substring(0, line.lastIndexOf(DELIMITER) + 1) + "true";
                    updated = true;
                }
                updatedPODetails.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String detail : updatedPODetails) {
                writer.write(detail);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return updated;
    }

    public void getPendingPOs() {
        List<String> pendingPOs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                String stockInStatus = fields[fields.length - 1];
                if ("false".equals(stockInStatus)) {
                    pendingPOs.add(fields[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the pending POs
        if (pendingPOs.isEmpty()) {
            System.out.println("No pending POs found.");
        } else {
            System.out.println("Pending POs with StockInStatus 'false': ");
            for (String poID : pendingPOs) {
                System.out.println(poID);
            }
        }
    }
    public boolean isPOIDExist(String poID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields[0].equals(poID)) {
                    return true;  // PO ID found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // An error occurred while reading the file
        }
        return false;  // PO ID not found
    }


    public boolean deletePO(String poID) {
        List<String> allPOs = getAllPOs();
        List<String> updatedPOs = new ArrayList<>();

        for (String po : allPOs) {
            String currentPOID = po.split("\\$")[0];
            if (!currentPOID.equals(poID)) {
                updatedPOs.add(po);
            }
        }

        // 将更新后的 PO 列表写回到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String updatedPO : updatedPOs) {
                writer.write(updatedPO);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveUpdatedPODetails(List<String> updatedPODetails) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PODetails.txt"))) {
            for (String detail : updatedPODetails) {
                bw.write(detail);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
