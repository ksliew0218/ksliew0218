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
        int maxPOID = 0;  // 初始化最大 POID 为 0。

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
            return -1;  // 返回 -1 表示出现错误。
        }

        if (maxPOID == 0) {
            return 1;  // 如果文件是空的或没有 POID，返回 1，意味着下一个 POID 应该是 "PO001"。
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
            File file = new File("PODetails.txt");  // 使用你的文件路径
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

}
