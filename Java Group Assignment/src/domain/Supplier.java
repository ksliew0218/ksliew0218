package domain;

import data.SupplierDAO;
import utility.Utility;
import java.util.Date;

public class Supplier implements Entry {
    private String supplierId;
    private String supplierName;
    private String contactNumber;
    private String email;
    private String address;
    private Date dateOfAssociation;
    private int deliveryDay;

    public Supplier(String supplierId, String supplierName, String contactNumber, String email, String address, Date dateOfAssociation, int deliveryDay) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.dateOfAssociation = dateOfAssociation;
        this.deliveryDay = deliveryDay;
    }

    public Supplier() {}

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfAssociation() {
        return dateOfAssociation;
    }

    public void setDateOfAssociation(Date dateOfAssociation) {
        this.dateOfAssociation = dateOfAssociation;
    }

    public int getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(int deliveryDay) {
        this.deliveryDay = deliveryDay;
    }
// Constructors, getters, and setters
    // ...

    @Override
    public void add() {
        SupplierDAO supplierDAO = new SupplierDAO();
        do {
            System.out.print("Enter Supplier ID: ");
            supplierId = Utility.readString(10);
            if (supplierDAO.checkDuplicateSupplierID(supplierId)) {
                System.out.println("This Supplier ID already exists. Please enter a unique Supplier ID.");
            }
        } while (supplierDAO.checkDuplicateSupplierID(supplierId));

        System.out.print("Enter Supplier Name: ");
        this.supplierName = Utility.readString(50);

        this.contactNumber = Utility.readContactNumber();

        this.email = Utility.readEmail();

        System.out.print("Enter Address: ");
        this.address = Utility.readString(100);

        System.out.print("Enter Date of Association. ");
        this.dateOfAssociation = Utility.readDate();

        System.out.print("Enter Delivery Day: ");
        this.deliveryDay = Utility.readInt();

        System.out.print("Do you want to save this supplier? (Y/N): ");
        char confirm = Utility.readConfirmSelection();
        if (confirm == 'Y' || confirm == 'y') {
            if (supplierDAO.saveSupplier(this)) {
                System.out.println("Supplier successfully saved.");
            } else {
                System.out.println("Failed to save the supplier.");
            }
        } else {
            System.out.println("Supplier not saved.");
        }
    }

    @Override
    public void edit() {
        SupplierDAO supplierDAO = new SupplierDAO();

        // Show all suppliers first to allow the user to choose which one to edit
        supplierDAO.viewAllSuppliers();

        System.out.print("Enter the Supplier ID you want to edit: ");
        String idToEdit = Utility.readString(10);

        if (!supplierDAO.checkExistingSupplierID(idToEdit)) {
            System.out.println("This Supplier ID does not exist.");
            return;
        }

        // Get existing details of the supplier to be edited
        Supplier existingSupplier = supplierDAO.getSupplierById(idToEdit);
        if (existingSupplier == null) {
            System.out.println("Error fetching existing supplier details.");
            return;
        }

        // Edit Supplier Name
        System.out.print("Current Supplier Name is " + existingSupplier.getSupplierName() + ". Press Enter to keep: ");
        String newName = Utility.readString(50, existingSupplier.getSupplierName());
        if (!newName.isEmpty()) {
            existingSupplier.setSupplierName(newName);
        }

        //Edit Contact
        System.out.print("Current Contact Number is " + existingSupplier.getContactNumber() + ". Press Enter to keep: ");
        String newContact = Utility.readContactNumber(existingSupplier.getContactNumber());
        existingSupplier.setContactNumber(newContact);

        // Edit Email
        System.out.print("Current Email is " + existingSupplier.getEmail() + ". Press Enter to keep: ");
        String newEmail = Utility.readEmail(existingSupplier.getEmail());
        existingSupplier.setEmail(newEmail);

        // Edit Date of Association
        System.out.print("Current Date of Association is " + existingSupplier.getDateOfAssociation() + ". Press Enter to keep: ");
        Date newDateOfAssociation = Utility.readDate(existingSupplier.getDateOfAssociation());
        existingSupplier.setDateOfAssociation(newDateOfAssociation);

        // Edit Delivery Day
        System.out.print("Current Delivery Day is " + existingSupplier.getDeliveryDay() + ". Press Enter to keep: ");
        int newDeliveryDay = Utility.readInt(existingSupplier.getDeliveryDay());
        existingSupplier.setDeliveryDay(newDeliveryDay);

        // Ask for confirmation
        System.out.println("\nAre you sure you want to update the supplier? ");
        char confirm = Utility.readConfirmSelection();
        if (confirm == 'Y') {
            if (supplierDAO.updateSupplier(existingSupplier)) {
                System.out.println("Supplier successfully updated.");
            } else {
                System.out.println("Failed to update the supplier.");
            }
        } else {
            System.out.println("Update cancelled.");
        }
    }



    @Override
    public void delete() {
        SupplierDAO supplierDAO = new SupplierDAO();

        System.out.print("Enter the Supplier ID of the supplier you want to delete: ");
        String supplierIDToDelete = Utility.readString(10);  // Assuming Utility.readString reads a string from the user.

        // Confirm deletion
        System.out.println("Are you sure you want to delete this supplier? ");
        char confirm = Utility.readConfirmSelection();
        if (confirm == 'Y' || confirm == 'y') {
            if (supplierDAO.deleteSupplierByID(supplierIDToDelete)) {
                System.out.println("Supplier deleted successfully.");
            } else {
                System.out.println("Failed to delete the supplier.");
            }
        } else {
            System.out.println("Supplier deletion cancelled.");
        }
    }


    @Override
    public void view() {
        SupplierDAO supplierDAO = new SupplierDAO();
        System.out.print("Enter Supplier ID to view details: ");
        String supplierID = Utility.readString(10);
        supplierDAO.viewSupplierDetails(supplierID);
    }

    public void searchSupplierByID() {
        SupplierDAO supplierDAO = new SupplierDAO();
        System.out.print("Enter the Supplier Name you want to search for: ");
        String supplierName = Utility.readString(30);
        supplierDAO.searchSupplierByName(supplierName);

    }
}
