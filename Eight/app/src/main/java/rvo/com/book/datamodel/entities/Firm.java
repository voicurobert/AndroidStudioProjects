package rvo.com.book.datamodel.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;


@IgnoreExtraProperties
public class Firm implements Serializable {

    @Exclude
    public static final String ID = "id";
    @Exclude
    public static final String EMAIL = "email";
    @Exclude
    public static final String NAME = "name";
    @Exclude
    public static final String PASSWORD = "password";
    @Exclude
    public static final String ADDRESS = "address";
    @Exclude
    public static final String PHONE_NUMBER = "phoneNumber";
    @Exclude
    public static final String FIREBASE_TOKEN = "firebaseToken";
    @Exclude
    public static final String ACTIVE = "active";
    @Exclude
    public static final String POINT = "point";
    @Exclude
    public static final String SCHEDULE_ID = "scheduleId";

    private String email;
    private String id;
    private String name;
    private String password;
    private String address;
    private String phoneNumber;
    private String scheduleId;
    @Exclude
    private Schedule schedule;
    private String firebaseToken;
    private Integer active;
    private GeoPoint point;

    public Firm() {

    }

    public Firm(String id, String firmName, String email, String firmAddress, String phoneNumber, Schedule schedule, Integer active) {
        this.id = id;
        this.name = firmName;
        this.address = firmAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.schedule = schedule;
        this.active = active;
    }

    @Exclude
    public Schedule getSchedule() {
        return schedule;
    }

    @Exclude
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String firmName) {
        this.name = firmName;
    }

    public void setAddress(String firmAddress) {
        this.address = firmAddress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Exclude
    public boolean firmIsActive() {
        return active.equals(1);
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    @Exclude
    public LatLng pointAsLatLng() {
        if (point != null) {
            return new LatLng(point.getLatitude(), point.getLongitude());
        }
        return null;
    }

}
