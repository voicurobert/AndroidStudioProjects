package rvo.com.book.datamodel.repositories;


import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.IObjectRetrieved;

public class FirmRepository extends FirebaseRepository {

    private static final String COLLECTION = "firms";
    private static FirmRepository SINGLETON = new FirmRepository();

    private FirmRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Firm.class);
    }

    public static FirmRepository getInstance(){
        return SINGLETON;
    }

    public void getActiveFirms(IObjectRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(Firm.STATUS, 1).get().addOnCompleteListener(task -> {
            List<Firm> firms = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot qs = task.getResult();
                if (qs != null && !qs.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : qs) {
                        firms.add(documentSnapshot.toObject(Firm.class));
                    }
                    objectRetrieved.onObjectRetrieved(firms);
                } else {
                    objectRetrieved.onObjectRetrieved(firms);
                }
            } else {
                objectRetrieved.onObjectRetrieved(firms);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }
}
