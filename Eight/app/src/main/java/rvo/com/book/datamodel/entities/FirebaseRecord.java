package rvo.com.book.datamodel.entities;

import java.io.Serializable;

public class FirebaseRecord implements Serializable {

    private String id;

    public FirebaseRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
