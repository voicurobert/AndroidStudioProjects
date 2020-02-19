package rvo.com.book.datamodel.entities;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;
import rvo.com.book.android.EightSharedPreferences;


@IgnoreExtraProperties
public class Product extends FirebaseRecord {

    @Exclude
    public static final String NAME = "name";
    @Exclude
    public static final String DURATION = "duration";
    @Exclude
    public static final String FIRM_CATEGORY_ID = "firmCategoryId";
    @Exclude
    public static final String PRICE = "price";
    @Exclude
    public static final String FIRM_ID = "firmId";

    private String name;
    private Integer price;
    private String duration;
    @Exclude
    private Category category;
    @Exclude
    private Firm firm;
    private String firmCategoryId;
    private String firmId;

    public Product() {

    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    @Exclude
    public Firm getFirm() {
        return firm;
    }

    @Exclude
    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    @Exclude
    public Category getCategory() {
        return category;
    }

    @Exclude
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public void setFirmCategoryId(String firmCategoryId) {
        this.firmCategoryId = firmCategoryId;
    }

    public String getFirmCategoryId() {
        return firmCategoryId;
    }

    @Exclude
    public String getPriceAsString() {
        return price + " LEI";
    }

    public String getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Exclude
    public Integer calculateDurationAsMinutes() {
        String hours;
        Integer minutes = 0;
        if (duration.contains("-")) {
            hours = duration.split("-")[0];
            minutes += Integer.valueOf(duration.split("-")[1]);
        } else {
            hours = duration;
        }
        Integer h = Integer.valueOf(hours);
        if (h > 1) {
            for (int i = 1; i <= h; i++) {
                minutes += 60;
            }
        } else if (h == 1) {
            minutes += 60;
        }
        return minutes;
    }

    @Exclude
    public String getDurationHours() {
        if (duration == null) {
            return null;
        }
        return duration.split("-")[0];
    }

    @Exclude
    public String getDurationMinutes() {
        if (duration == null) {
            return null;
        }
        if (duration.split("-").length == 1) {
            return "0";
        } else {
            return duration.split("-")[1];
        }
    }

    @Exclude
    public String getDurationAsTime() {
        return getDurationHours() + ":" + getDurationMinutes();
    }

    @Exclude
    public String getDurationDescription() {
        String h = getDurationHours();
        String m = getDurationMinutes();
        if (m.equals("0")) {
            return h + " hours ";
        } else {
            return h + " hours " + m + " minutes";
        }
    }

    @Exclude
    public String getDescription() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            return name + ", costs " + getPriceAsString() + " and takes " + getDurationDescription();
        } else {
            return name + ", costs " + getPriceAsString();
        }
    }

    @Exclude
    @Override
    public String toString() {
        return name + " " + getPriceAsString();
    }
}
