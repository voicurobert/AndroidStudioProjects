package rvo.com.book.datamodel.interfaces;

import java.util.List;

import rvo.com.book.datamodel.entities.FirebaseRecord;

@FunctionalInterface
public interface OnObjectsRetrieved {
    void onObjectsRetrieved(List<FirebaseRecord> objects);
}
