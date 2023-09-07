package domain;

import java.util.*;

import data.*;

import utility.Utility;

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

        // 1. 获取所有可编辑的PR并显示PR编号
        List<String> allPRs = prDAO.getAllEditablePRs();
        Map<Integer, String> prMap = displaySelectablePRs(allPRs);

        // 2. 让用户选择PR
        System.out.print("请选择要生成PO的PR编号: ");
        int selectedPRIndex = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (!prMap.containsKey(selectedPRIndex)) {
            System.out.println("选择无效!");
            return;
        }

        String selectedPRID = prMap.get(selectedPRIndex);
        List<String> selectedPRDetails = prDAO.getPRDetails(selectedPRID);
        List<String> itemsToBeAddedToPO = new ArrayList<>();

        while (true) {
            // 3. 显示选择的PR的内容
            System.out.println("\n选中的PR详细信息: \n");
            PurchaseRequisition pr = new PurchaseRequisition();
            pr.displayPRDetails(selectedPRID, prDAO);


            System.out.print("请输入要加入到PO的item code (或 'exit' 退出): ");
            String itemCodeChoice = scanner.nextLine().trim();

            // 6. 询问用户是否要使用推荐的数量还是输入新的数量
            // (此处基于你提供的代码进行了简化)
            String matchingDetail = selectedPRDetails.stream()
                    .filter(detail -> detail.startsWith(selectedPRID + "$" + itemCodeChoice + "$"))
                    .findFirst().orElse(null);

            if (matchingDetail == null) {
                System.out.println("此PR中找不到该item code。");
                continue;
            }

            String[] itemDetails = matchingDetail.split("\\$");
            System.out.println("推荐数量: " + itemDetails[5]);
            System.out.print("请输入新的数量 (或按Enter使用推荐数量): ");
            int newQuantity = Utility.readInt(Integer.parseInt(itemDetails[5]));

            // 将更新的item details添加到PO列表中
            itemsToBeAddedToPO.add(matchingDetail.replace(itemDetails[5], String.valueOf(newQuantity)));
        }

// 7. 显示生成的PO内容
/*
        String poDetails = generatePODetails(selectedPRID, itemsToBeAddedToPO, itemDAO);
        System.out.println("\n生成的PO详细信息: \n" + poDetails);
*/

/*// 8. 确认并保存PO
        System.out.print("\n确认生成PO? (yes/no): ");*/
 /*       String confirmChoice = scanner.nextLine();
        if ("yes".equalsIgnoreCase(confirmChoice)) {
            // 在用户确认生成PO之前，先更新PR中的数量
            updatePRQuantities(selectedPRID, itemsToBeAddedToPO, prDAO);

            // 创建 PurchaseOrder 对象
*//*
            PurchaseOrder newPO = createPOFromDetails(poDetails);
*//*

            poDAO.savePurchaseOrder(newPO);
            System.out.println("PO 已成功生成!");

            // 更新PR的poStatus为true
            prDAO.updatePOStatus(selectedPRID, true);*/
        }
/*    }*/

    private void updatePRQuantities(String prID, List<String> items, PurchaseRequisitionDAO prDAO) {
        List<String> allPrDetails = prDAO.getAllPRs();

        for (String item : items) {
            String[] itemDetails = item.split("\\$");
            String itemCode = itemDetails[1];
            int newQuantity = Integer.parseInt(itemDetails[5]);

            String matchingDetail = allPrDetails.stream()
                    .filter(detail -> detail.startsWith(prID + "$" + itemCode + "$"))
                    .findFirst().orElse(null);

            if (matchingDetail != null) {
                String[] details = matchingDetail.split("\\$");
                details[5] = String.valueOf(newQuantity);  // Update the quantity
                String updatedDetail = String.join("$", details);

                int indexToUpdate = allPrDetails.indexOf(matchingDetail);
                allPrDetails.set(indexToUpdate, updatedDetail);
            }
        }

        prDAO.saveUpdatedPRDetails(allPrDetails);
    }

    private Map<Integer, String> displaySelectablePRs(List<String> allPRs) {
        Map<Integer, String> prMap = new HashMap<>();
        int index = 1;

        for (String prDetail : allPRs) {
            String prID = prDetail.split("\\$")[0];
            if (!prMap.containsValue(prID)) {
                System.out.println(index + ". " + prID);
                prMap.put(index, prID);
                index++;
            }
        }

        return prMap;
    }


    public void editPRBeforeGeneratingPO(String selectedPR, PurchaseRequisitionDAO prDAO, SupplierDAO supplierDAO) {
        // TODO: Implementation needed
    }
}
