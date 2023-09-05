package data;

import domain.DailySales;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailySalesDAO {

    private static final String FILE_PATH = "DailySales.txt";
    private static final String DELIMITER = "$";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();

    public boolean saveDailySales(DailySales dailySale) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            String line = sdf.format(currentDate) + DELIMITER +
                    dailySale.getItemCode() + DELIMITER +
                    dailySale.getItemName() + DELIMITER +
                    dailySale.getQuantitySold() + DELIMITER +
                    String.format("%.2f", dailySale.getSellingPrice()) + DELIMITER +
                    String.format("%.2f", dailySale.getTax()) + DELIMITER +
                    String.format("%.2f", dailySale.getTotalPriceOfItem()) + "\n";

            writer.write(line);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DailySales> getSalesByDate(Date date) {
        List<DailySales> salesList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String targetDate = sdf.format(date);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);

                if (parts.length < 7) {
                    continue;  // Skip lines with insufficient data
                }

                String saleDate = parts[0];
                if (!saleDate.equals(targetDate)) {
                    continue;  // Skip if the date doesn't match
                }

                String itemCode = parts[1];
                String itemName = parts[2];
                int quantitySold = Integer.parseInt(parts[3]);
                double sellingPrice = Double.parseDouble(parts[4]);
                double tax = Double.parseDouble(parts[5]);
                double totalPriceOfItem = Double.parseDouble(parts[6]);

                DailySales dailySale = new DailySales(
                        sdf.parse(saleDate),
                        itemCode,
                        itemName,
                        quantitySold,
                        sellingPrice,
                        tax,
                        totalPriceOfItem
                );
                salesList.add(dailySale);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return salesList;
    }


    public void printSalesByDate(Date date) {
        List<DailySales> salesList = getSalesByDate(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        double totalAmount = 0.0;

        System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                "Date", "Item Code", "Item Name", "Quantity Sold", "Selling Price", "Tax (6%)", "Total Price of Item (RM)");

        for (DailySales dailySale : salesList) {
            System.out.format("%-15s %-15s %-15s %-15d %-15.2f %-15.2f %-15.2f\n",
                    sdf.format(date),
                    dailySale.getItemCode(),
                    dailySale.getItemName(),
                    dailySale.getQuantitySold(),
                    dailySale.getSellingPrice(),
                    dailySale.getTax(),
                    dailySale.getTotalPriceOfItem());

            totalAmount += dailySale.getTotalPriceOfItem();
        }

        System.out.println("\n\n\n\n\n\n");
        System.out.format("%-100s %-15.2f", "Total Amount (RM):", totalAmount);
    }

    public void viewAllSales() {
        List<DailySales> allSales = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            String prevDate = "";
            System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s\n", "Date", "Item Code", "Item Name", "Quantity Sold", "Selling Price", "Tax", "Total Price of Item");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length < 7) {
                    continue;  // Skip lines with insufficient data
                }

                String saleDate = parts[0];
                if (!saleDate.equals(prevDate)) {
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                }
                prevDate = saleDate;

                String itemCode = parts[1];
                String itemName = parts[2];
                int quantitySold = Integer.parseInt(parts[3]);
                double sellingPrice = Double.parseDouble(parts[4]);
                double tax = Double.parseDouble(parts[5]);
                double totalPriceOfItem = Double.parseDouble(parts[6]);

                DailySales dailySale = new DailySales(/* populate constructor with parsed data */);
                allSales.add(dailySale);

                System.out.format("%-15s %-15s %-15s %-15d %-15.2f %-15.2f %-15.2f\n",
                        saleDate,
                        itemCode,
                        itemName,
                        quantitySold,
                        sellingPrice,
                        tax,
                        totalPriceOfItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteSalesEntry(Date date, String itemCode) {
        List<String> lines = new ArrayList<>();
        boolean entryFound = false;
        String targetDate = sdf.format(date);

        // Read existing file
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length < 7) {
                    continue;
                }

                String saleDate = parts[0];
                String saleItemCode = parts[1];
                if (saleDate.equals(targetDate) && saleItemCode.equals(itemCode)) {
                    entryFound = true;
                    continue;  // Skip this line to remove it
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write updated file
        if (entryFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return entryFound;
    }

    public boolean editDailySales(Date dateToEdit, String itemCodeToEdit, DailySales updatedDailySale) {
        List<String> lines = new ArrayList<>();
        boolean entryFound = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String targetDate = sdf.format(dateToEdit);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue;  // Skip lines with insufficient data
                }

                String saleDate = fields[0];
                String itemCode = fields[1];
                if (saleDate.equals(targetDate) && itemCode.equals(itemCodeToEdit)) {
                    // Update the entry
                    line = targetDate + DELIMITER +
                            updatedDailySale.getItemCode() + DELIMITER +
                            updatedDailySale.getItemName() + DELIMITER +
                            updatedDailySale.getQuantitySold() + DELIMITER +
                            String.format("%.2f", updatedDailySale.getSellingPrice()) + DELIMITER +
                            String.format("%.2f", updatedDailySale.getTax()) + DELIMITER +
                            String.format("%.2f", updatedDailySale.getTotalPriceOfItem());
                    entryFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!entryFound) {
            return false;
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
