package domain;

import java.util.*;

import data.*;

public class PurchaseOrder {

    private String POID;
    private String creationDate;
    private String createdBy;
    private Map<String, Integer> items;

    // 添加默认构造器
    public PurchaseOrder() {}

    public PurchaseOrder(String POID, String creationDate, String createdBy) {
        this.POID = POID;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.items = new HashMap<>();
    }

    public PurchaseOrder(String record) {
        String[] details = record.split("\\$");
        this.POID = details[0];
        this.creationDate = details[1];
        this.createdBy = details[2];
        this.items = new HashMap<>();

        for (int i = 3; i < details.length - 1; i += 2) {
            this.items.put(details[i], Integer.parseInt(details[i + 1]));
        }
    }

    public void addItem(String itemCode, int quantity) {
        items.put(itemCode, quantity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(POID).append("$")
                .append(creationDate).append("$")
                .append(createdBy);

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            sb.append("$").append(entry.getKey())
                    .append("$").append(entry.getValue());
        }
        return sb.toString();
    }

    public void generatePOFromPR(PurchaseRequisitionDAO prDAO, PurchaseOrderDAO poDAO, SupplierDAO supplierDAO, ItemDAO itemDAO) {
        Scanner scanner = new Scanner(System.in);

        List<String> allPRs = prDAO.getAllEditablePRs();
        List<String> prIDs = new ArrayList<>();
        Map<Integer, String> prMenu = new HashMap<>();

        for (String pr : allPRs) {
            String prID = pr.split("\\$")[0];
            if (prID != null && !prID.equals("null") && !prIDs.contains(prID)) {
                prIDs.add(prID);
            }
        }

        if (prIDs.isEmpty()) {
            System.out.println("没有可用的 PR 生成 PO.");
            return;
        }

        // 显示所有的PR编号
        System.out.println("\nAvailable PRs:");
        for (int i = 0; i < prIDs.size(); i++) {
            System.out.println((i + 1) + ". " + prIDs.get(i));
            prMenu.put(i + 1, prIDs.get(i));
        }

        System.out.print("请选择要生成PO的PR编号: ");
        int selectedPRIndex = scanner.nextInt();

        if (selectedPRIndex > 0 && selectedPRIndex <= prIDs.size()) {
            String selectedPR = prMenu.get(selectedPRIndex);

            PurchaseRequisition pr = new PurchaseRequisition();
            // 显示选择的PR的内容
            System.out.println("\n选中的PR详细信息: \n");
            pr.displayPRDetails(selectedPR, prDAO);

            // 如果用户选择编辑PR
            System.out.print("\n是否在生成PO前编辑该PR? (yes/no): ");
            String editChoice = scanner.next();

            if ("yes".equalsIgnoreCase(editChoice)) {
                editPRBeforeGeneratingPO(selectedPR, prDAO, supplierDAO);
            }

            // 确认并生成PO
            System.out.print("\n确认生成PO? (yes/no): ");
            String confirmChoice = scanner.next();
            if ("yes".equalsIgnoreCase(confirmChoice)) {
                // TODO: 生成PO的相关代码
                System.out.println("PO 已成功生成!");

                // 更新PR的poStatus为true
                prDAO.updatePOStatus(selectedPR, true);
            }
        } else {
            System.out.println("选择无效!");
        }
    }


    public void editPRBeforeGeneratingPO(String selectedPR, PurchaseRequisitionDAO prDAO, SupplierDAO supplierDAO) {
        // TODO: Implementation needed
    }
}
