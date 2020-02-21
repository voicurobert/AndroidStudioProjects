package rvo.com.book.datamodel.interfaces;

import rvo.com.book.datamodel.entities.FirebaseRecord;

@FunctionalInterface
public interface OnObjectRetrieved {
    void onObjectRetrieved(FirebaseRecord object);

}
