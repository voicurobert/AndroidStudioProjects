package rvo.com.book.eight_db;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rvo.com.book.common.Eight;
import rvo.com.book.common.EightDate;

public class FirestoreManager {
    private static FirestoreManager instance = new FirestoreManager();

    public static final String CUSTOMERS = "customers";
    public static final String FIRMS = "firms";
    public static final String BOOKINGS = "bookings";
    public static final String EMPLOYEES = "employees";
    public static final String CATEGORIES = "categories";
    public static final String PRODUCTS = "products";
    public static final String SCHEDULES = "schedules";

    private FirebaseFirestore firestore;
    private CollectionReference customersCollection;
    private CollectionReference firmsCollection;
    private CollectionReference bookingsCollection;
    private CollectionReference employeesCollection;
    private CollectionReference categoriesCollection;
    private CollectionReference productsCollection;
    private CollectionReference schedulesCollection;


    private FirestoreManager() {
        firestore = FirebaseFirestore.getInstance();
        firmsCollection = firestore.collection(FIRMS);
        customersCollection = firestore.collection(CUSTOMERS);
        employeesCollection = firestore.collection(EMPLOYEES);
        categoriesCollection = firestore.collection(CATEGORIES);
        productsCollection = firestore.collection(PRODUCTS);
        bookingsCollection = firestore.collection(BOOKINGS);
        schedulesCollection = firestore.collection(SCHEDULES);
    }

    public static FirestoreManager getInstance() {
        return instance;
    }

    public void firmOwnerFromEmail(String email, IObjectRetrieved objectRetrieved) {
        firmsCollection.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null) {
                    Firm firm = null;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        firm = document.toObject(Firm.class);
                    }
                    objectRetrieved.onObjectRead(firm);
                }
            }
            objectRetrieved.onObjectRead(null);
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void firmOwnerFromEmailAndPassword(String email, String password, IObjectRetrieved objectRetrieved) {
        firmsCollection.whereEqualTo(Firm.EMAIL, email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Firm firm;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Firm currentFirm = document.toObject(Firm.class);
                        if (currentFirm.getPassword().equals(password)) {
                            firm = currentFirm;
                            Eight.dataModel.setFirm(firm);
                        }
                    }
                }
            }
            objectRetrieved.onObjectRead(null);
        }).addOnFailureListener(Throwable::printStackTrace);

    }

    public void insertFirm(Firm firm) {
        String id = firmsCollection.document().getId();
        firm.setId(id);
        firmsCollection.document(id).set(firm);
    }

    public void writeFirmAddress(String id, String address) {
        firmsCollection.document(id).update(Firm.ADDRESS, address);
    }

    public void setFirmFirebaseToken(String firebaseToken) {
        firmsCollection.document(Eight.dataModel.getFirm().getId()).update(Firm.FIREBASE_TOKEN, firebaseToken);
    }

    public void getActiveFirms(IObjectRetrieved objectRetrieved) {
        firmsCollection.whereEqualTo(Firm.ACTIVE, 1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot qs = task.getResult();
                if (qs != null && !qs.isEmpty()) {
                    List<Firm> firms = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : qs) {
                        firms.add(documentSnapshot.toObject(Firm.class));
                    }
                    objectRetrieved.onObjectRead(firms);
                }
            }
            objectRetrieved.onObjectRead(Collections.emptyList());
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void setActiveStatusForFirm(String firmOwnerId, Integer status) {
        firmsCollection.document(firmOwnerId).update(Firm.ACTIVE, status);
    }

    public void updateFirmPassword(Firm firm, String password) {
        firmsCollection.document(firm.getId()).update(Firm.PASSWORD, password);
    }

    public void updateFirm(Firm firm, String firmName, String phoneNumber) {
        firmsCollection.document(firm.getId()).update(Firm.NAME, firmName, Firm.PHONE_NUMBER, phoneNumber);
    }

    public void setFirmLocation(String firmId, Double latitude, Double longitude) {
        firmsCollection.document(firmId).update(Firm.POINT, new GeoPoint(latitude, longitude));
    }

    public void writeCustomer(String customerEmail, String customerName, String customerPassword, String customerPhoneNumber, String customerToken) {
        Customer customer = new Customer();
        customer.setEmail(customerEmail);
        customer.setName(customerName);
        customer.setPassword(customerPassword);
        customer.setPhoneNumber(customerPhoneNumber);
        customer.setFirebaseToken(customerToken);
        String id = customersCollection.document().getId();
        customer.setId(id);
        customersCollection.document(id).set(customer);
    }

    public void customerWithEmailAndPassword(String email, String password, IObjectRetrieved objectRetrieved) {
        customersCollection.whereEqualTo(Customer.EMAIL, email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Customer customer;
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        customer = documentSnapshot.toObject(Customer.class);
                        if (customer.getPassword().equals(password)) {
                            objectRetrieved.onObjectRead(customer);
                        }
                    }
                }
            } else {
                objectRetrieved.onObjectRead(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void customerWithEmail(String email, IObjectRetrieved objectRetrieved) {
        customersCollection.whereEqualTo(Customer.EMAIL, email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        objectRetrieved.onObjectRead(customer);
                    }
                }
            } else {
                objectRetrieved.onObjectRead(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void updateCustomerWithFirebaseToken(String firebaseToken) {
        customersCollection.document(Eight.dataModel.getCustomer().getId()).update(Customer.FIREBASE_TOKEN, firebaseToken);
    }

    public void updateCustomerPassword(Customer customer, String password) {
        customersCollection.document(customer.getId()).update(Customer.PASSWORD, password);
    }

    public void insertCategory(Category category, Firm firm, IObjectModified objectInserted) {
        category.setFirm(firm);
        category.setFirmId(firm.getId());
        String id = categoriesCollection.document().getId();
        category.setId(id);
        categoriesCollection.document(id).set(category);
        objectInserted.objectModified();
        
    }

    public void updateCategory(String id, String name, IObjectModified objectInserted) {
        categoriesCollection.document(id).update(Category.NAME, name).addOnSuccessListener(aVoid -> objectInserted.objectModified());
    }

    public void deleteCategory(Category category, IObjectModified objectInserted) {
        categoriesCollection.document(category.getId()).delete().addOnSuccessListener(command -> objectInserted.objectModified());
    }

    public void categoriesForFirmOwnerId(Firm firm, IObjectRetrieved objectRetrieved) {
        categoriesCollection.whereEqualTo(Category.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Category> categories = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Category category = documentSnapshot.toObject(Category.class);
                    category.setFirm(Eight.dataModel.getFirm());
                    categories.add(category);
                }
                objectRetrieved.onObjectRead(categories);
            }
        });
    }

    public void insertProduct(Product product, IObjectModified objectModified) {
        String id = productsCollection.document().getId();
        product.setId(id);
        productsCollection.document(id).set(product);
        objectModified.objectModified();
    }

    public void updateProduct(Product product, String name, Integer price, String duration, String categoryId, IObjectModified objectModified) {
        productsCollection.document(product.getId())
                          .update(Product.NAME, name, Product.PRICE, price, Product.DURATION, duration, Product.FIRM_CATEGORY_ID, categoryId)
                          .addOnSuccessListener(command -> objectModified.objectModified());
    }

    public void deleteProduct(Product product, IObjectModified objectInserted) {
        productsCollection.document(product.getId()).delete().addOnSuccessListener(command -> objectInserted.objectModified());
    }

    public void productsFromFirmOwnerId(String firmOwnerId, IObjectRetrieved objectRetrieved) {
        productsCollection.whereEqualTo(Product.FIRM_ID, firmOwnerId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Product> products = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Product product = documentSnapshot.toObject(Product.class);
                    product.setCategory(Eight.dataModel.getCategoryFromCategoryId(product.getFirmCategoryId()));
                    product.setFirm(Eight.dataModel.getFirm());
                    products.add(product);
                }
                objectRetrieved.onObjectRead(products);
            }
        });
    }

    public void insertEmployee(Employee employee) {
        String id = employeesCollection.document().getId();
        employee.setId(id);
        employeesCollection.document(id).set(employee);
    }

    public void updateEmployee(Employee employee, String name, List<String> categories) {
        employeesCollection.document(employee.getId()).update(Employee.NAME, name, Employee.CATEGORY_IDS, categories);
        employee.setCategories(categories);
        if (!employee.getName().equals(name)) {
            employee.setName(name);
        }
    }

    public void deleteEmployee(Employee employee) {
        employeesCollection.document(employee.getId()).delete();
    }

    public void updateEmployeeWithScheduleId(String employeeId, String scheduleId) {
        employeesCollection.document(employeeId).update(Employee.SCHEDULE_ID, scheduleId);
    }

    public void employeesForFirmId(String firmId, IObjectRetrieved objectRetrieved) {
        employeesCollection.whereEqualTo(Employee.FIRM_ID, firmId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Employee> employees = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Employee employee = documentSnapshot.toObject(Employee.class);
                    employees.add(employee);
                }
                objectRetrieved.onObjectRead(employees);
            }
        });
    }

    public Schedule insertScheduleBasedOnSchedule(Schedule schedule) {
        Schedule newSchedule = new Schedule();
        newSchedule.setMonday(schedule.getMonday());
        newSchedule.setTuesday(schedule.getTuesday());
        newSchedule.setWednesday(schedule.getWednesday());
        newSchedule.setThursday(schedule.getThursday());
        newSchedule.setFriday(schedule.getFriday());
        newSchedule.setSaturday(schedule.getSaturday());
        newSchedule.setSunday(schedule.getSunday());
        newSchedule.setMondayWorkingHours(schedule.getMondayWorkingHours());
        newSchedule.setTuesdayWorkingHours(schedule.getTuesdayWorkingHours());
        newSchedule.setWednesdayWorkingHours(schedule.getWednesdayWorkingHours());
        newSchedule.setThursdayWorkingHours(schedule.getThursdayWorkingHours());
        newSchedule.setFridayWorkingHours(schedule.getFridayWorkingHours());
        newSchedule.setSaturdayWorkingHours(schedule.getSaturdayWorkingHours());
        newSchedule.setSundayWorkingHours(schedule.getSundayWorkingHours());
        String id = schedulesCollection.document().getId();
        newSchedule.setId(id);
        schedulesCollection.document(id).set(newSchedule);
        return newSchedule;
    }

    public void writeSchedule(String monday, String tuesday, String wednesday, String thursday,
                              String friday, String saturday, String sunday,
                              String mondayWorkingHours, String tuesdayWorkingHours,
                              String wednesdayWorkingHours, String thursdayWorkingHours,
                              String fridayWorkingHours, String saturdayWorkingHours,
                              String sundayWorkingHours, IObjectRetrieved objectRetrieved) {

        Schedule schedule = new Schedule();
        schedule.setMonday(monday);
        schedule.setTuesday(tuesday);
        schedule.setWednesday(wednesday);
        schedule.setThursday(thursday);
        schedule.setFriday(friday);
        schedule.setSaturday(saturday);
        schedule.setSunday(sunday);
        schedule.setMondayWorkingHours(mondayWorkingHours);
        schedule.setTuesdayWorkingHours(tuesdayWorkingHours);
        schedule.setWednesdayWorkingHours(wednesdayWorkingHours);
        schedule.setThursdayWorkingHours(thursdayWorkingHours);
        schedule.setFridayWorkingHours(fridayWorkingHours);
        schedule.setSaturdayWorkingHours(saturdayWorkingHours);
        schedule.setSundayWorkingHours(sundayWorkingHours);
        String id = schedulesCollection.document().getId();
        schedule.setId(id);
        Eight.dataModel.getFirm().setSchedule(schedule);
        schedulesCollection.document(id).set(schedule);
        objectRetrieved.onObjectRead(schedule);
    }

    public void updateSchedule(Schedule schedule, String monday, String tuesday, String wednesday,
                               String thursday, String friday, String saturday, String sunday,
                               String mondayWorkingHours, String tuesdayWorkingHours,
                               String wednesdayWorkingHours, String thursdayWorkingHours,
                               String fridayWorkingHours, String saturdayWorkingHours,
                               String sundayWorkingHours) {
        DocumentReference documentReference = schedulesCollection.document(schedule.getId());
        if (!schedule.getMonday().equals(monday)) {
            documentReference.update(Schedule.MONDAY, monday);
            schedule.setMonday(monday);
        }
        if (!schedule.getTuesday().equals(tuesday)) {
            documentReference.update(Schedule.TUESDAY, tuesday);
            schedule.setTuesday(tuesday);
        }
        if (!schedule.getWednesday().equals(wednesday)) {
            documentReference.update(Schedule.WEDNESDAY, wednesday);
            schedule.setWednesday(wednesday);
        }
        if (!schedule.getThursday().equals(thursday)) {
            documentReference.update(Schedule.THURSDAY, thursday);
            schedule.setThursday(thursday);
        }
        if (!schedule.getFriday().equals(friday)) {
            documentReference.update(Schedule.FRIDAY, friday);
            schedule.setFriday(friday);
        }
        if (!schedule.getSaturday().equals(saturday)) {
            documentReference.update(Schedule.SATURDAY, saturday);
            schedule.setSaturday(saturday);
        }
        if (!schedule.getSunday().equals(sunday)) {
            documentReference.update(Schedule.SUNDAY, sunday);
            schedule.setSunday(sunday);
        }
        if (!schedule.getMondayWorkingHours().equals(mondayWorkingHours)) {
            documentReference.update(Schedule.MONDAY_WORKING_HOURS, mondayWorkingHours);
            schedule.setMondayWorkingHours(mondayWorkingHours);
        }
        if (!schedule.getTuesdayWorkingHours().equals(tuesdayWorkingHours)) {
            documentReference.update(Schedule.TUESDAY_WORKING_HOURS, tuesdayWorkingHours);
            schedule.setTuesdayWorkingHours(tuesdayWorkingHours);
        }
        if (!schedule.getWednesdayWorkingHours().equals(wednesdayWorkingHours)) {
            documentReference.update(Schedule.WEDNESDAY_WORKING_HOURS, wednesdayWorkingHours);
            schedule.setTuesday(wednesdayWorkingHours);
        }
        if (!schedule.getThursdayWorkingHours().equals(thursdayWorkingHours)) {
            documentReference.update(Schedule.THURSDAY_WORKING_HOURS, thursdayWorkingHours);
            schedule.setThursdayWorkingHours(thursdayWorkingHours);
        }
        if (!schedule.getFridayWorkingHours().equals(fridayWorkingHours)) {
            documentReference.update(Schedule.FRIDAY_WORKING_HOURS, fridayWorkingHours);
            schedule.setFridayWorkingHours(fridayWorkingHours);
        }
        if (!schedule.getSaturdayWorkingHours().equals(saturdayWorkingHours)) {
            documentReference.update(Schedule.SATURDAY_WORKING_HOURS, saturdayWorkingHours);
            schedule.setSaturdayWorkingHours(saturdayWorkingHours);
        }
        if (!schedule.getSundayWorkingHours().equals(sundayWorkingHours)) {
            documentReference.update(Schedule.SUNDAY_WORKING_HOURS, sundayWorkingHours);
            schedule.setSaturdayWorkingHours(sundayWorkingHours);
        }
    }

    public void insertBooking(Booking booking) {
        String id = bookingsCollection.document().getId();
        booking.setId(id);
        bookingsCollection.document(id).set(booking);
    }

    public void setBookingActivationStatus(String bookingId, Integer status) {
        bookingsCollection.document(bookingId).update(Booking.STATUS, status);
    }

    public void getPendingBookingsForFirmOwnerId(Firm firm, IObjectRetrieved objectRetrieved) {
        bookingsCollection.whereEqualTo(Booking.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Booking booking = documentSnapshot.toObject(Booking.class);
                    EightDate today = new EightDate();
                    if (booking.isPending() && today.onSame(booking.getEightDate())) {
                        booking.setProduct(Eight.dataModel.getProductFromId(booking.getProductId()));
                        booking.setEmployee(Eight.dataModel.getEmployeeFromId(booking.getEmployeeId()));
                        booking.setFirm(firm);
                        bookings.add(booking);
                    }
                }
                objectRetrieved.onObjectRead(bookings);
            }
        });
    }

    public void getBookingsForEmployeeForDate(Employee employee, EightDate date, IObjectRetrieved objectRetrieved) {
        bookingsCollection.whereEqualTo(Booking.EMPLOYEE_ID, employee.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Booking booking = documentSnapshot.toObject(Booking.class);
                    if (booking.getEightDate().sameDateAs(date)) {
                        booking.setEmployee(employee);
                        booking.setProduct(Eight.dataModel.getProductFromId(booking.getProductId()));
                        booking.setFirm(Eight.dataModel.getFirm());
                        bookings.add(booking);
                    }
                }
                objectRetrieved.onObjectRead(bookings);
            }
        });
    }

    public void getAllBookingsFromCustomerId(Customer customer, IObjectRetrieved objectRetrieved) {
        bookingsCollection.whereEqualTo(Booking.CUSTOMER_ID, customer.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Booking booking = documentSnapshot.toObject(Booking.class);
                    EightDate today = new EightDate();
                    if (today.onSame(booking.getEightDate())) {
                        booking.setEmployee(Eight.dataModel.getEmployeeFromId(booking.getEmployeeId()));
                        booking.setProduct(Eight.dataModel.getProductFromId(booking.getProductId()));
                        booking.setFirm(Eight.dataModel.getFirm());
                        booking.setCustomer(customer);
                        booking.setCustomerId(customer.getId());
                        bookings.add(booking);
                    }
                }
                objectRetrieved.onObjectRead(bookings);
            }
        });
    }

    public void getTodaysBookingsForFirm(Firm firm, IObjectRetrieved objectRetrieved) {
        bookingsCollection.whereEqualTo(Booking.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Booking booking = documentSnapshot.toObject(Booking.class);
                    EightDate today = new EightDate();
                    if (today.sameDateAs(booking.getEightDate()) &&
                        today.getDate().getTime() > booking.getEightDate().getDate().getTime() &&
                        booking.isActive()) {
                        booking.setFirm(Eight.dataModel.getFirm());
                        bookings.add(booking);
                    }
                }
                objectRetrieved.onObjectRead(bookings);
            }
        });
    }

    public void deleteBooking(String bookingId) {
        bookingsCollection.document(bookingId).delete();
    }

    public void getObjectFromId(String databasePath, String id, Class objectClass, IObjectRetrieved objectRetrieved) {
        firestore.collection(databasePath).whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    objectRetrieved.onObjectRead(documentSnapshot.toObject(objectClass));
                }
            }
        });
    }
}
