package data;

import domain.Supplier;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private static final String FILE_PATH = "Suppliers.txt"; // Path to the text file where supplier data is stored
    private static final String DELIMITER = "$"; // Delimiter used in the text file

    public boolean saveSupplier(Supplier supplier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // true for append mode
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateOfAssociation = sdf.format(supplier.getDateOfAssociation());

            // Create a string representation of the supplier object
            String supplierRecord = String.join(DELIMITER,
                    supplier.getSupplierId(),
                    supplier.getSupplierName(),
                    String.valueOf(supplier.getContactNumber()),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    dateOfAssociation,
                    String.valueOf(supplier.getDeliveryDay())
            );

            // Write the string representation of the supplier to the text file
            writer.write(supplierRecord);
            writer.newLine(); // Add a new line to separate records
            return true; // Successfully saved the supplier
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to save the supplier
        }
    }

    public boolean checkDuplicateSupplierID(String supplierId) {
        List<String> existingSupplierIds = readSupplierIds();
        return existingSupplierIds.contains(supplierId);
    }

    public boolean checkExistingSupplierID(String supplierId) {
        List<String> existingSupplierIds = readSupplierIds();
        return existingSupplierIds.contains(supplierId);
    }
    private List<String> readSupplierIds() {
        List<String> supplierIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 1) {
                    continue; // skip lines with insufficient data
                }
                String existingSupplierId = fields[0];
                supplierIds.add(existingSupplierId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supplierIds;
    }


    public void searchSupplierByName(String supplierName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean supplierFound = false;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue; // skip lines with insufficient data
                }

                String existingSupplierName = fields[1]; // Assuming the name is in the second field
                if (existingSupplierName.equalsIgnoreCase(supplierName)) {
                    supplierFound = true;
                    System.out.println("\n--- Supplier Found ---");
                    System.out.println("Supplier ID:           " + fields[0]);
                    System.out.println("Name:                  " + existingSupplierName);
                    System.out.println("Contact Number:        " + fields[2]);
                    System.out.println("Email:                 " + fields[3]);
                    System.out.println("Address:               " + fields[4]);
                    System.out.println("Date of Association:   " + fields[5]);
                    System.out.println("Delivery Day:          " + fields[6] + " days");
                    System.out.println("----------------------");
                }
            }

            if (!supplierFound) {
                System.out.println("No supplier found with the given name.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean deleteSupplierByID(String supplierID) {
        // Check if supplier ID exists before attempting to delete
        if (!checkExistingSupplierID(supplierID)) {
            System.out.println("No supplier found with the given ID.");
            return false;
        }

        List<String> lines = new ArrayList<>();

        // Read all lines from file into a list
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 6) {
                    continue; // skip lines with insufficient data
                }
                if (!fields[0].equals(supplierID)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write the updated list back to the file, excluding the deleted supplier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Supplier successfully deleted.");
        return true;
    }

    public void viewSupplierDetails(String supplierID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue; // skip lines with insufficient data
                }

                String existingSupplierID = fields[0];
                if (existingSupplierID.equals(supplierID)) {
                    System.out.println("\n--- Supplier Details ---");
                    System.out.println("Supplier ID:           " + existingSupplierID);
                    System.out.println("Name:                  " + fields[1]);
                    System.out.println("Contact Number:        " + fields[2]);
                    System.out.println("Email:                 " + fields[3]);
                    System.out.println("Address:               " + fields[4]);
                    System.out.println("Date of Association:   " + fields[5]);
                    System.out.println("Delivery Day:          " + fields[6] + " days");
                    System.out.println("------------------------");
                    return;
                }
            }
            System.out.println("No supplier found with the given ID.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewAllSuppliers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            System.out.println("\n--- All Suppliers ---");
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue; // skip lines with insufficient data
                }
                System.out.println("Supplier ID: " + fields[0] + ", Name: " + fields[1]);
            }
            System.out.println("---------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue; // skip lines with insufficient data
                }

                if (fields[0].equals(updatedSupplier.getSupplierId())) {
                    // Replace with updated details
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dateOfAssociation = sdf.format(updatedSupplier.getDateOfAssociation());

                    String updatedLine = String.join(DELIMITER,
                            updatedSupplier.getSupplierId(),
                            updatedSupplier.getSupplierName(),
                            String.valueOf(updatedSupplier.getContactNumber()),
                            updatedSupplier.getEmail(),
                            updatedSupplier.getAddress(),
                            dateOfAssociation,
                            String.valueOf(updatedSupplier.getDeliveryDay())
                    );

                    lines.add(updatedLine);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write updated lines back to file
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

    public Supplier getSupplierById(String supplierId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\" + DELIMITER);
                if (fields.length < 7) {
                    continue;
                }
                if (fields[0].equals(supplierId)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return new Supplier(
                            fields[0],
                            fields[1],
                            fields[2],
                            fields[3],
                            fields[4],
                            sdf.parse(fields[5]),
                            Integer.parseInt(fields[6])
                    );
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                suppliers.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}

