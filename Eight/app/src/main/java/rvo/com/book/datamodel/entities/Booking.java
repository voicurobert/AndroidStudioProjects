package rvo.com.book.datamodel.entities;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Locale;

import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.common.EightDate;

@IgnoreExtraProperties
public class Booking extends FirebaseRecord {

    @Exclude
    public static final String DATE = "date";

    @Exclude
    public static final String STATUS = "status";

    @Exclude
    public static final String FIRM = "firm";

    @Exclude
    public static final String CUSTOMER = "customer";

    @Exclude
    public static final String EMPLOYEE = "employee";

    @Exclude
    public static final String FIRM_ID = "firmId";

    @Exclude
    public static final String CUSTOMER_ID = "customerId";

    @Exclude
    public static final String EMPLOYEE_ID = "employeeId";

    @Exclude
    public static final Integer ACTIVE = 1;

    @Exclude
    public static final Integer DECLINE = 0;

    @Exclude
    public static final Integer PENDING = -1;

    @Exclude
    public static final Integer FINISHED = 2;

    private String date;
    private String customerId;
    @Exclude
    private Customer customer;
    private String employeeName;
    private String employeeId;
    @Exclude
    private Employee employee;
    private String productName;
    private String productId;
    @Exclude
    private Product product;
    private String firmId;
    @Exclude
    private Firm firm;
    private Integer status;
    private String customerName;

    public Booking() {

    }


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Exclude
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Exclude
    public Customer getCustomer() {
        return customer;
    }

    @Exclude
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Exclude
    public Employee getEmployee() {
        return employee;
    }

    @Exclude
    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    @Exclude
    public Firm getFirm() {
        return firm;
    }

    @Exclude
    public void setProduct(Product product) {
        this.product = product;
    }

    @Exclude
    public Product getProduct() {
        return product;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Exclude
    public boolean isActive() {
        return status.equals(1);
    }

    @Exclude
    public boolean isPending() {
        return status.equals(-1);
    }

    @Exclude
    public boolean isDeclined() {
        return status.equals(0);
    }

    @Exclude
    public EightDate getEightDate() {
        return new EightDate(date);
    }

    @Exclude
    public String activeString() {
        switch (status) {
            case 1:
                return "Active";
            case 0:
                return "Declined";
            case -1:
                return "Pending";
        }
        return "";
    }

    @Override
    @Exclude
    public String toString() {
        if (product != null) {
            return "" + product.getName();
        }
        return "Booking - " + date;
    }

    @Exclude
    public String getPendingBookingDescriptionForFirmMode() {
        String customerName = (this.customerName != null) ? this.customerName : customer.getName();
        return customerName + " has requested a booking for " + productName + " at " + employeeName + " on " + dateDescription();
    }

    @Exclude
    public String getPendingBookingDescriptionForCustomerMode() {
        return productName + " at " + employeeName + " on " + dateDescription();

    }

    @Exclude
    public String descriptionForFirmMode() {
        if (product != null) {
            return "" + product.getName() + " with " + customerName;
        } else {
            return toString();
        }
    }

    @Exclude
    public String description() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            return descriptionForFirmMode();
        } else {
            return toString();
        }
    }

    @Exclude
    public String dateDescription() {
        SimpleDateFormat df = new SimpleDateFormat("H:mm aa", Locale.ENGLISH);
        EightDate eightDate = new EightDate(date);
        return eightDate.getDayAsString() + ", " +
               eightDate.getDayAsInteger() + " " +
               eightDate.getMonthAsString() + ", " +
               eightDate.getYearAsString() + " - " +
               df.format(eightDate.getDate());
    }
}
