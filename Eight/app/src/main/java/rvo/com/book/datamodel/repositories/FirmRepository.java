package rvo.com.book.datamodel.repositories;


import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.OnObjectsRetrieved;

public class FirmRepository extends FirebaseRepository {

    private static final String COLLECTION = "firms";
    private static FirmRepository SINGLETON = new FirmRepository();

    private FirmRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Firm());
    }

    public static FirmRepository getInstance(){
        return SINGLETON;
    }

    public void getActiveFirms(OnObjectsRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(Firm.STATUS, 1).get().addOnCompleteListener(task -> {
            List<FirebaseRecord> firms = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot qs = task.getResult();
                if (qs != null && !qs.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : qs) {
                        firms.add(documentSnapshot.toObject(Firm.class));
                    }
                    objectRetrieved.onObjectsRetrieved(firms);
                } else {
                    objectRetrieved.onObjectsRetrieved(firms);
                }
            } else {
                objectRetrieved.onObjectsRetrieved(firms);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }
}
