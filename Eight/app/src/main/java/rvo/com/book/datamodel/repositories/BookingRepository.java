package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Booking;

public class BookingRepository extends FirebaseRepository {
    private static final BookingRepository SINGLETON = new BookingRepository();
    private static final String COLLECTION = "booking";

    public BookingRepository(){
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Booking.class);
    }

    public static BookingRepository getInstance(){
        return SINGLETON;
    }
}
