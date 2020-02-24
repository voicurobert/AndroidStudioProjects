package rvo.com.book.datamodel.entities;

import android.database.DataSetObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import rvo.com.book.common.EightDate;
import rvo.com.book.datamodel.interfaces.IDownloadFinished;
import rvo.com.book.datamodel.repositories.BookingRepository;
import rvo.com.book.datamodel.repositories.CategoryRepository;
import rvo.com.book.datamodel.repositories.EmployeeRepository;
import rvo.com.book.datamodel.repositories.FirmRepository;
import rvo.com.book.datamodel.repositories.ProductRepository;
import rvo.com.book.datamodel.repositories.ScheduleRepository;


public class DataModel extends DataSetObserver {

    private static DataModel instance = new DataModel();
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
        Firm rootFirm;
        if (firm == null) {
            rootFirm = this.firm;
        } else {
            this.firm = firm;
            rootFirm = firm;
        }
        initialiseScheduleForFirm(rootFirm, () ->
                initialiseCategories(rootFirm, () ->
                        initialiseProducts(rootFirm, () ->
                                initialiseEmployees(rootFirm, downloadFinished))));
    }

    public void initialiseDataStoreForSelectedFirm(Firm firm, IDownloadFinished downloadFinished) {
        this.firm = firm;
        initialiseCategories(firm, () ->
                initialiseProducts(firm, () ->
                        initialiseEmployees(firm, downloadFinished)));
    }

    public void initialiseActiveFirms(IDownloadFinished downloadFinished) {
        activeFirms.clear();
        FirmRepository.getInstance().getActiveFirms(objects -> {
            if (!objects.isEmpty()) {
                for (FirebaseRecord record : objects) {
                    if (record instanceof Firm) {
                        activeFirms.add((Firm) record);
                    }
                }
                downloadFinished.finished();
            } else {
                downloadFinished.finished();
            }
        });
    }

    private void initialiseCategories(Firm firm, IDownloadFinished downloadFinished) {
        categories.clear();
        CategoryRepository.getInstance().objectsWithFirmId(firm, objects -> {
            if (!objects.isEmpty()) {
                for (FirebaseRecord record : objects) {
                    Category category = (Category) record;
                    category.setFirm(firm);
                    categories.add(category);
                }
            }

            if (downloadFinished != null) {
                downloadFinished.finished();
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

    public Category getCategoryFromCategoryId(String idCategory) {
        for (Category category : categories) {
            if (category.getId().equals(idCategory)) {
                return category;
            }
        }
        return null;
    }

    private String getCategoryNameFromCategoryId(String categoryId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Optional<String> name = categories.stream().filter(category -> category.getId().equals(categoryId)).map(Category::getName).findFirst();
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

    private void initialiseProducts(Firm firm, IDownloadFinished downloadFinished) {
        products.clear();
        ProductRepository.getInstance().objectsWithFirmId(firm, objects -> {
            if (!objects.isEmpty()) {
                for (FirebaseRecord record : objects) {
                    Product product = (Product) record;
                    product.setFirm(firm);
                    product.setCategory(getCategoryFromCategoryId(product.getFirmCategoryId()));
                    products.add(product);
                }
            }
            if (downloadFinished != null) {
                downloadFinished.finished();
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

    private void initialiseEmployees(Firm firm, IDownloadFinished downloadFinished) {
        employees.clear();
        EmployeeRepository.getInstance().objectsWithFirmId(firm, objects -> {
            if (!objects.isEmpty()) {
                for (FirebaseRecord record : objects) {
                    Employee employee = (Employee) record;
                    employees.add(employee);
                }
            }
            if (downloadFinished != null) {
                downloadFinished.finished();
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
        ScheduleRepository.getInstance().objectFromId(firm.getScheduleId(), object -> {
            if (object != null) {
                Schedule schedule = (Schedule) object;
                firm.setSchedule(schedule);
            }
            if (downloadFinished != null) {
                downloadFinished.finished();
            }
        });
    }

    public void initialiseSchedulesForEmployee(Employee employee, IDownloadFinished downloadFinished) {
        ScheduleRepository.getInstance().objectFromId(employee.getScheduleId(), object -> {
            if (object != null) {
                Schedule schedule = (Schedule) object;
                employee.setSchedule(schedule);
                employee.setWorkingHours(schedule.getWorkingHoursForDay(new EightDate().getDayAsString()));
            }
            if (downloadFinished != null) {
                downloadFinished.finished();
            }
        });
    }

    // BOOKINGS

    public void initialiseBookingsForEmployeeOnDate(Employee employee, EightDate date, IDownloadFinished downloadFinished) {
        employee.getBookings().clear();
        BookingRepository.getInstance().getBookingsForEmployeeForDate(employee, date, objects -> {
            if (!objects.isEmpty()) {
                for (FirebaseRecord record : objects) {
                    Booking booking = (Booking) record;
                    if (booking.getEightDate().inPastAs(date)) {
                        BookingRepository.getInstance().updateRecord(booking, Booking.STATUS, Booking.FINISHED);
                        booking.setStatus(Booking.FINISHED);
                    }
                    employee.addBooking(booking);
                }
            }
            employee.computeBookings();
            downloadFinished.finished();
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
