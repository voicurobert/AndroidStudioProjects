package rvo.com.book.datamodel.entities;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

@IgnoreExtraProperties
public class Customer extends FirebaseRecord {

    @Exclude
    public static final String NAME = "name";
    @Exclude
    public static final String EMAIL = "email";
    @Exclude
    public static final String PASSWORD = "password";
    @Exclude
    public static final String PHONE_NUMBER = "phoneNumber";
    @Exclude
    public static final String FIREBASE_TOKEN = "firebaseToken";

    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String firebaseToken;

    public Customer() {

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }


}
