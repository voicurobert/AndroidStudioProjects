package rvo.com.book.datamodel.entities;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Schedule extends FirebaseRecord {

    @Exclude
    public static final String MONDAY = "monday";
    @Exclude
    public static final String TUESDAY = "tuesday";
    @Exclude
    public static final String WEDNESDAY = "wednesday";
    @Exclude
    public static final String THURSDAY = "thursday";
    @Exclude
    public static final String FRIDAY = "friday";
    @Exclude
    public static final String SATURDAY = "saturday";
    @Exclude
    public static final String SUNDAY = "sunday";

    @Exclude
    public static final String MONDAY_WORKING_HOURS = "mondayWorkingHours";
    @Exclude
    public static final String TUESDAY_WORKING_HOURS = "tuesdayWorkingHours";
    @Exclude
    public static final String WEDNESDAY_WORKING_HOURS = "wednesdayWorkingHours";
    @Exclude
    public static final String THURSDAY_WORKING_HOURS = "thursdayWorkingHours";
    @Exclude
    public static final String FRIDAY_WORKING_HOURS = "fridayWorkingHours";
    @Exclude
    public static final String SATURDAY_WORKING_HOURS = "saturdayWorkingHours";
    @Exclude
    public static final String SUNDAY_WORKING_HOURS = "sundayWorkingHours";

    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private String mondayWorkingHours;
    private String tuesdayWorkingHours;
    private String wednesdayWorkingHours;
    private String thursdayWorkingHours;
    private String fridayWorkingHours;
    private String saturdayWorkingHours;
    private String sundayWorkingHours;

    public Schedule() {

    }

    public void setFridayWorkingHours(String fridayWorkingHours) {
        this.fridayWorkingHours = fridayWorkingHours;
    }

    public void setMondayWorkingHours(String mondayWorkingHours) {
        this.mondayWorkingHours = mondayWorkingHours;
    }

    public void setSaturdayWorkingHours(String saturdayWorkingHours) {
        this.saturdayWorkingHours = saturdayWorkingHours;
    }

    public void setSundayWorkingHours(String sundayWorkingHours) {
        this.sundayWorkingHours = sundayWorkingHours;
    }

    public void setThursdayWorkingHours(String thursdayWorkingHours) {
        this.thursdayWorkingHours = thursdayWorkingHours;
    }

    public void setTuesdayWorkingHours(String tuesdayWorkingHours) {
        this.tuesdayWorkingHours = tuesdayWorkingHours;
    }

    public void setWednesdayWorkingHours(String wednesdayWorkingHours) {
        this.wednesdayWorkingHours = wednesdayWorkingHours;
    }

    public String getFridayWorkingHours() {
        return fridayWorkingHours;
    }

    public String getMondayWorkingHours() {
        return mondayWorkingHours;
    }

    public String getSaturdayWorkingHours() {
        return saturdayWorkingHours;
    }

    public String getSundayWorkingHours() {
        return sundayWorkingHours;
    }

    public String getThursdayWorkingHours() {
        return thursdayWorkingHours;
    }

    public String getTuesdayWorkingHours() {
        return tuesdayWorkingHours;
    }

    public String getWednesdayWorkingHours() {
        return wednesdayWorkingHours;
    }

    public String getFriday() {
        return friday;
    }

    public String getMonday() {
        return monday;
    }

    public String getSaturday() {
        return saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    @Exclude
    public boolean isMonday() {
        if (monday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isTuesday() {
        if (tuesday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isWednesday() {
        if (wednesday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isThursday() {
        if (thursday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isFriday() {
        if (friday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isSaturday() {
        if (saturday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public boolean isSunday() {
        if (sunday.equals("1")) {
            return true;
        }
        return false;
    }

    @Exclude
    public List<String> getWorkingHoursForDay(String day) {
        List<String> workingHours = new ArrayList<>();
        String wh = "";
        try {
            for (Method method : Schedule.class.getMethods()) {
                if (method.getName().contains("WorkingHours") && method.getName().contains(day)) {
                    wh = (String) method.invoke(this);
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {

        }
        String[] vec = wh.split("-");
        int start = Integer.valueOf(vec[0]);
        int end = Integer.valueOf(vec[1]) - 1;
        for (int i = start; i <= end; i++) {
            String hour = String.valueOf(i);
            workingHours.add(hour);
        }
        return workingHours;
    }

    @Exclude
    public static List<String> getMinutes() {
        List<String> minutes = new ArrayList<>();
        minutes.add("0");
        minutes.add("15");
        minutes.add("30");
        minutes.add("45");
        return minutes;
    }

    @Override
    @Exclude
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mon - ");
        if (isMonday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Tue - ");
        if (isTuesday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Wed - ");
        if (isWednesday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Thu - ");
        if (isThursday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Fri - ");
        if (isFriday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Sat - ");
        if (isSaturday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Sun - ");
        if (isSunday()) {
            stringBuilder.append(getMondayWorkingHours());
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("Closed");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

