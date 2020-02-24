package rvo.com.book.datamodel.entities;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

@IgnoreExtraProperties
public class Category extends FirebaseRecord {

    @Exclude
    public static final String NAME = "name";
    @Exclude
    public static final String FIRM_ID = "firmId";
    @Exclude
    public static final String ID = "id";

    private String firmId;
    private String name;
    @Exclude
    private Firm firm;
    private String id;

    public Category() {

    }

    public Category(String id, String name, Firm firm) {
        this.name = name;
        this.firm = firm;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Exclude
    public Firm getFirm() {
        return firm;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.id.equals(((Category) obj).getId());
    }

    @Override
    @Exclude
    public String toString() {
        return name;
    }

}
