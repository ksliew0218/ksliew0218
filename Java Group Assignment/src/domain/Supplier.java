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

        System.out.print("Enter Date of Association (dd/MM/yyyy): ");
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

    }

    @Override
    public void delete() {

    }

    @Override
    public void view() {

    }

    // ... other methods for edit, delete, view, etc.
}
