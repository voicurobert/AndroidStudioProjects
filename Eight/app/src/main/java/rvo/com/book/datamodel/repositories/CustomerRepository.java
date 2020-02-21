package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Customer;

public class CustomerRepository extends FirebaseRepository {
    private static final CustomerRepository SINGLETON = new CustomerRepository();
    private static final String COLLECTION = "customers";

    private CustomerRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Customer());
    }

    public static CustomerRepository getInstance(){
        return SINGLETON;
    }
}
