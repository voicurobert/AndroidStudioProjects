package rvo.com.book.datamodel.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.IObjectRetrieved;

public abstract class FirebaseRepository {

    private CollectionReference collectionReference;
    private Class objectClass = Object.class;

    public FirebaseRepository(){

    }

    protected CollectionReference getCollectionReference() {
        return collectionReference;
    }

    protected void initializeFirestore(String collectionName){
        collectionReference = FirebaseFirestore.getInstance().collection(collectionName);
    }

    protected void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    protected String newRecordId(){
        return getCollectionReference().document().getId();
    }

    public void insertRecord(FirebaseRecord firebaseRecord){
        firebaseRecord.setId(newRecordId());
        getCollectionReference().document(firebaseRecord.getId()).set(firebaseRecord);
    }

    public void objectFromEmail(String email, IObjectRetrieved objectRetrieved) {
        collectionReference.whereEqualTo(FieldKeys.EMAIL, email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        objectRetrieved.onObjectRetrieved(document.toObject(objectClass));
                    }
                } else {
                    objectRetrieved.onObjectRetrieved(null);
                }
            } else {
                objectRetrieved.onObjectRetrieved(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public void objectFromEmailAndPassword(String email, String password, IObjectRetrieved objectRetrieved) {
        getCollectionReference().whereEqualTo(FieldKeys.EMAIL, email).whereEqualTo(FieldKeys.PASSWORD, password).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        objectRetrieved.onObjectRetrieved(document.toObject(objectClass));
                    }
                } else {
                    objectRetrieved.onObjectRetrieved(null);
                }
            } else {
                objectRetrieved.onObjectRetrieved(null);
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }
}
