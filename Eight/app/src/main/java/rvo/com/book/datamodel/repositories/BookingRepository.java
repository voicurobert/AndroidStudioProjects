package rvo.com.book.datamodel.repositories;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.common.EightDate;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Employee;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.OnObjectsRetrieved;

public class BookingRepository extends FirebaseRepository {
    private static final BookingRepository SINGLETON = new BookingRepository();
    private static final String COLLECTION = "bookings";

    private BookingRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Booking());
    }

    public static BookingRepository getInstance() {
        return SINGLETON;
    }

    public void getPendingBookingsForFirmOwnerId(Firm firm, OnObjectsRetrieved objectsRetrieved) {
        getCollectionReference().whereEqualTo(Booking.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseRecord> bookings = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        EightDate today = new EightDate();
                        if (booking.isPending() && today.onSame(booking.getEightDate())) {
                            booking.setProduct(DataModel.getInstance().getProductFromId(booking.getProductId()));
                            booking.setEmployee(DataModel.getInstance().getEmployeeFromId(booking.getEmployeeId()));
                            booking.setFirm(firm);
                            bookings.add(booking);
                        }
                    }
                    objectsRetrieved.onObjectsRetrieved(bookings);
                } else {
                    objectsRetrieved.onObjectsRetrieved(bookings);
                }
            }
        });
    }

    public void getBookingsForEmployeeForDate(Employee employee, EightDate date, OnObjectsRetrieved objectsRetrieved) {
        getCollectionReference().whereEqualTo(Booking.EMPLOYEE_ID, employee.getId()).get().addOnCompleteListener(task -> {
            List<FirebaseRecord> bookings = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        if (booking.getEightDate().sameDateAs(date)) {
                            booking.setEmployee(employee);
                            booking.setProduct(DataModel.getInstance().getProductFromId(booking.getProductId()));
                            booking.setFirm(DataModel.getInstance().getFirm());
                            bookings.add(booking);
                        }
                    }
                    objectsRetrieved.onObjectsRetrieved(bookings);
                } else {
                    objectsRetrieved.onObjectsRetrieved(bookings);
                }
            } else {
                objectsRetrieved.onObjectsRetrieved(bookings);
            }
        });
    }

    public void getAllBookingsFromCustomerId(Customer customer, OnObjectsRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(Booking.CUSTOMER_ID, customer.getId()).get().addOnCompleteListener(task -> {
            List<FirebaseRecord> bookings = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        EightDate today = new EightDate();
                        if (today.onSame(booking.getEightDate())) {
                            booking.setEmployee(DataModel.getInstance().getEmployeeFromId(booking.getEmployeeId()));
                            booking.setProduct(DataModel.getInstance().getProductFromId(booking.getProductId()));
                            booking.setFirm(DataModel.getInstance().getFirm());
                            booking.setCustomer(customer);
                            booking.setCustomerId(customer.getId());
                            bookings.add(booking);
                        }
                    }
                    objectRetrieved.onObjectsRetrieved(bookings);
                } else {
                    objectRetrieved.onObjectsRetrieved(bookings);
                }
            } else {
                objectRetrieved.onObjectsRetrieved(bookings);
            }
        });
    }

    public void getTodaysBookingsForFirm(Firm firm, OnObjectsRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(Booking.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseRecord> bookings = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        EightDate today = new EightDate();
                        if (today.sameDateAs(booking.getEightDate()) &&
                            today.getDate().getTime() > booking.getEightDate().getDate().getTime() &&
                            booking.isActive()) {
                            booking.setFirm(DataModel.getInstance().getFirm());
                            bookings.add(booking);
                        }
                    }
                } else {
                    objectRetrieved.onObjectsRetrieved(bookings);
                }

            }
        });
    }
}
