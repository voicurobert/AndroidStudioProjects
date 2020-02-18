package rvo.com.book.datamodel.repositories;


import rvo.com.book.datamodel.entities.Firm;

public class FirmRepository extends FirebaseRepository {

    private static final String COLLECTION = "firms";

    public FirmRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Firm.class);
    }

}
