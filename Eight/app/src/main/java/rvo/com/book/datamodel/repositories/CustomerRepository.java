package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Customer;

public class CustomerRepository extends FirebaseRepository {
    private static final CustomerRepository SINGLETON = new CustomerRepository();
    private static final String COLLECTION = "customer";

    public CustomerRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Customer.class);
    }

    public static CustomerRepository getInstance(){
        return SINGLETON;
    }
}
