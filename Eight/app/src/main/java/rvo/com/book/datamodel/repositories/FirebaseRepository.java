package rvo.com.book.datamodel.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.OnObjectRetrieved;
import rvo.com.book.datamodel.interfaces.OnObjectsRetrieved;

public abstract class FirebaseRepository {

    private CollectionReference collectionReference;
    private FirebaseRecord objectClass;

    protected CollectionReference getCollectionReference() {
        return collectionReference;
    }

    protected void initializeFirestore(String collectionName) {
        collectionReference = FirebaseFirestore.getInstance().collection(collectionName);
    }

    public void setObjectClass(FirebaseRecord objectClass) {
        this.objectClass = objectClass;
    }

    private String newRecordId() {
        return getCollectionReference().document().getId();
    }

    public Task<Void> insertRecord(FirebaseRecord firebaseRecord) {
        firebaseRecord.setId(newRecordId());
        return getCollectionReference().document(firebaseRecord.getId()).set(firebaseRecord);
    }

    public Task<Void> updateRecord(FirebaseRecord record, String key, Object value, @Nullable Object... keysAndValues) {
        return collectionReference.document(record.getId()).update(key, value, keysAndValues);
    }

    public Task<Void> deleteRecord(FirebaseRecord firebaseRecord) {
        return getCollectionReference().document(firebaseRecord.getId()).delete();
    }

    public void objectFromEmail(String email, OnObjectRetrieved objectRetrieved) {
        collectionReference.whereEqualTo(FieldKeys.EMAIL, email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        objectRetrieved.onObjectRetrieved(document.toObject(objectClass.getClass()));
                    }
                } else {
                    objectRetrieved.onObjectRetrieved(null);
                }
            } else {
                objectRetrieved.onObjectRetrieved(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void objectFromEmailAndPassword(String email, String password, OnObjectRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(FieldKeys.EMAIL, email).whereEqualTo(FieldKeys.PASSWORD, password).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        objectRetrieved.onObjectRetrieved(document.toObject(objectClass.getClass()));
                    }
                } else {
                    objectRetrieved.onObjectRetrieved(null);
                }
            } else {
                objectRetrieved.onObjectRetrieved(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void objectFromId(String id, OnObjectRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot query = task.getResult();
                if (query != null && !query.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : query) {
                        objectRetrieved.onObjectRetrieved(documentSnapshot.toObject(objectClass.getClass()));
                    }
                } else {
                    objectRetrieved.onObjectRetrieved(null);
                }
            }
        });
    }

    public void objectsWithFirmId(Firm firm, OnObjectsRetrieved onObjectsRetrieved) {
        getCollectionReference().whereEqualTo(FieldKeys.FIRM_ID, firm.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseRecord> categories = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        FirebaseRecord record = documentSnapshot.toObject(objectClass.getClass());
                        categories.add(record);
                    }
                    onObjectsRetrieved.onObjectsRetrieved(categories);
                } else {
                    onObjectsRetrieved.onObjectsRetrieved(categories);
                }
            } else {
                onObjectsRetrieved.onObjectsRetrieved(new ArrayList<>());
            }
        });
    }
}
