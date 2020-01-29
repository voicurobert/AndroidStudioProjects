package rvo.com.book.eight_db;

import android.database.DataSetObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import rvo.com.book.common.Eight;
import rvo.com.book.common.EightDate;


public class DataModel extends DataSetObserver {

    private static DataModel instance = new DataModel() ;
    // categories for selected firm, when in Customer mode, and categories for firm in Firm mode
    private List<Category> categories;
    // products for selected firm, when in Customer mode, and products for firm in Firm mode
    private List<Product> products;
    // employees for selected firm, when in Customer mode, and employees for firm in Firm mode
    private List<Employee> employees;

    private List<Firm> activeFirms; // all active firms from database in Customer mode

    private Customer customer;
    private Firm firm;

    private DataModel() {
        activeFirms = new ArrayList<>();
        categories = new ArrayList<>();
        products = new ArrayList<>();
        employees = new ArrayList<>();
    }

    public static DataModel getInstance() {
        return instance;
    }

    public void clear() {
        activeFirms.clear();
        categories.clear();
        products.clear();
        employees.clear();
    }

    // GETTERS AND SETTERS

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Firm> getActiveFirms() {
        return activeFirms;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    // INITIALISATION

    public void initialiseDataStore(Firm firm, IDownloadFinished downloadFinished) {
        final Firm rootFirm;
        if (firm == null) {
            rootFirm = this.firm;
        } else {
            rootFirm = firm;
        }
        initialiseScheduleForFirm(rootFirm, () ->
                initialiseCategories(rootFirm, () ->
                        initialiseProducts(rootFirm.getId(), () ->
                                initialiseEmployees(rootFirm, downloadFinished))));
    }

    public void initialiseDataStoreForSelectedFirm(Firm firm, IDownloadFinished downloadFinished) {
        this.firm = firm;
        initialiseCategories(firm, () ->
                initialiseProducts(firm.getId(), () ->
                        initialiseEmployees(firm, downloadFinished)));
    }

    public void initialiseActiveFirms(IDownloadFinished downloadFinished) {
        activeFirms.clear();
        Eight.firestoreManager.getActiveFirms(object -> {
            if (object != null & object instanceof List) {
                activeFirms.addAll((List<Firm>) object);
                downloadFinished.finished();
            } else {
                downloadFinished.finished();
            }
        });
    }

    // CATEGORIES
    public void initialiseCategories(Firm firm, IDownloadFinished downloadFinished) {
        categories.clear();
        Eight.firestoreManager.categoriesForFirmOwnerId(firm, object -> {
            if (object instanceof ArrayList<?>) {
                ArrayList<Category> categoryList = (ArrayList<Category>) object;
                categories.addAll(categoryList);
                if (downloadFinished != null) {
                    downloadFinished.finished();
                }
            }
        });
    }

    public void setCategories(Firm firm) {
        categories.clear();
        initialiseCategories(firm, null);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<String> getCategoryNamesAsList() {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return names;
    }

    public Category getCategoryIdFromCategoryName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public String getCategoryNamesFromCategoryIds(String categoryIds) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] ids = categoryIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            Category category = getCategoryFromCategoryId(ids[i]);
            if (i == ids.length - 1) {
                stringBuilder.append(category.getName());
            } else {
                stringBuilder.append(category.getName());
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public Category getCategoryFromCategoryId(String idCategory) {
        for (Category category : categories) {
            if (category.getId().equals(idCategory)) {
                return category;
            }
        }
        return null;
    }

    public String getCategoryNameFromCategoryId(String categoryId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Optional<String> name = categories.stream()
                                              .filter(category -> category.getId().equals(categoryId))
                                              .map(Category::getName).findFirst();
            return name.orElse("");
        } else {
            for (Category category : categories) {
                if (category.getId().equals(categoryId)) {
                    return category.getName();
                }
            }
        }
        return "";
    }

    public Category getCategoryFromCategoryName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public List<Category> getCategoriesFromCategoryIds(List<String> categoryIds) {
        List<Category> categories = new ArrayList<>();
        for (String id : categoryIds) {
            categories.add(getCategoryFromCategoryId(id));
        }
        return categories;
    }

    public List<String> getCategoriesNamesFromCategoryIds(List<String> categories) {
        List<String> categoriesString = new ArrayList<>();
        for (String id : categories) {
            categoriesString.add(getCategoryNameFromCategoryId(id));
        }
        return categoriesString;
    }

    // PRODUCTS

    public void initialiseProducts(String firmOwnerId, IDownloadFinished downloadFinished) {
        products.clear();
        Eight.firestoreManager.productsFromFirmOwnerId(firmOwnerId, object -> {
            if (object instanceof ArrayList<?>) {
                ArrayList<Product> productList = (ArrayList<Product>) object;
                products.addAll(productList);
                if (downloadFinished != null) {
                    downloadFinished.finished();
                }
            }
        });
    }

    public Product getProductFromId(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }


    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public ArrayList<Product> getProductsFromCategoryId(Category category) {
        ArrayList<Product> newProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                newProducts.add(product);
            }
        }
        return newProducts;
    }

    public List<String> getProductNamesFromCategory(Category category) {
        List<String> newProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                newProducts.add(product.getName());
            }
        }
        return newProducts;
    }

    public Product getProductFromProductName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    // EMPLOYEES

    public void initialiseEmployees(Firm firm, IDownloadFinished downloadFinished) {
        employees.clear();
        Eight.firestoreManager.employeesForFirmId(firm.getId(), object -> {
            if (object instanceof ArrayList<?>) {
                ArrayList<Employee> employeeList = (ArrayList<Employee>) object;
                employees.addAll(employeeList);
                if (downloadFinished != null) {
                    downloadFinished.finished();
                }
            }
        });

    }


    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public Firm getActiveFirmFromName(String firmName) {
        for (Firm firm : activeFirms) {
            if (firm.getName().equals(firmName)) {
                return firm;
            }
        }
        return null;
    }

    public Employee getEmployeeFromId(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    public List<String> getEmployeeNames() {
        List<String> employees = new ArrayList<>();
        for (Employee employee : this.employees) {
            employees.add(employee.getName());
        }
        return employees;
    }

    public Employee getEmployeeFromName(String name) {
        for (Employee employee : employees) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    // SCHEDULES


    public void initialiseScheduleForFirm(Firm firm, IDownloadFinished downloadFinished) {
        Eight.firestoreManager.getObjectFromId(FirestoreManager.SCHEDULES, firm.getScheduleId(), Schedule.class, object -> {
            if (object != null) {
                Schedule schedule = (Schedule) object;
                firm.setSchedule(schedule);
                if (downloadFinished != null) {
                    downloadFinished.finished();
                }
            }
        });
    }

    public void initialiseSchedulesForEmployee(Employee employee, IDownloadFinished downloadFinished) {
        Eight.firestoreManager.getObjectFromId(FirestoreManager.SCHEDULES, employee.getScheduleId(), Schedule.class, object -> {
            Schedule schedule = (Schedule) object;
            if (schedule != null) {
                employee.setSchedule(schedule);
                employee.setWorkingHours(schedule.getWorkingHoursForDay(new EightDate().getDayAsString()));
                downloadFinished.finished();
            }
        });
    }

    // BOOKINGS

    public void initialiseBookingsForEmployeeOnDate(Employee employee, EightDate date, IDownloadFinished downloadFinished) {
        employee.getBookings().clear();
        Eight.firestoreManager.getBookingsForEmployeeForDate(employee, date, object -> {
            if (object instanceof ArrayList<?>) {
                List<Booking> bookings = (ArrayList<Booking>) object;
                for (Booking booking : bookings) {
                    if (booking.getEightDate().inPastAs(date)) {
                        Eight.firestoreManager.setBookingActivationStatus(booking.getId(), Booking.FINISHED);
                        booking.setStatus(Booking.FINISHED);
                    }
                    employee.addBooking(booking);
                }
                employee.computeBookings();
                downloadFinished.finished();
            }
        });
    }

    public void removeCategory(String categoryId) {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                categories.remove(category);
                return;
            }
        }
    }

    public boolean categoryHasProducts(Category category) {
        for (Product product : products) {
            Category productCategory = product.getCategory();
            if ((productCategory) == null) {
                return false;
            }
            if (productCategory.equals(category)) {
                return true;
            }
        }
        return false;
    }

    public void removeProductsFromCategoryId(Category category) {
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                products.remove(product);
                return;
            }
        }
    }

    public void removeProduct(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                products.remove(product);
                return;
            }
        }
    }

    public void removeEmployee(Employee employee) {
        for (Employee employee1 : employees) {
            if (employee.getId().equals(employee1.getId())) {
                employees.remove(employee);
                return;
            }
        }
    }
}
