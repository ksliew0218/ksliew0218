package data;

import domain.PurchaseOrder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {
    private final String poFilePath = "PODetails.txt";

    public List<String> getAllPOs() {
        return readFile(poFilePath);
    }

    public void savePurchaseOrder(PurchaseOrder po) {
        writeFile(poFilePath, po.toString(), true);
    }

    public void saveUpdatedPODetails(List<String> updatedDetails) {
        StringBuilder sb = new StringBuilder();
        for (String detail : updatedDetails) {
            sb.append(detail).append("\n");
        }
        writeFile(poFilePath, sb.toString().trim(), false);
    }

    private List<String> readFile(String filePath) {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void writeFile(String filePath, String content, boolean appendMode) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, appendMode))) {
            if (appendMode) {
                bw.newLine();
            }
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
