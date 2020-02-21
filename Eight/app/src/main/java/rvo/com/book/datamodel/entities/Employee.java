package rvo.com.book.datamodel.entities;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rvo.com.book.common.EightDate;
import rvo.com.book.common.Tools;

@IgnoreExtraProperties
public class Employee extends FirebaseRecord {

    @Exclude
    public static final String NAME = "name";
    @Exclude
    public static final String CATEGORY_IDS = "categories";
    @Exclude
    public static final String FIRM_ID = "firmId";
    @Exclude
    public static final String SCHEDULE_ID = "scheduleId";

    private String name;
    private List<String> categories;
    private String firmId;
    private String scheduleId;
    private Schedule schedule;

    @Exclude
    private List<Booking> bookings;
    @Exclude
    private List<String> workingHours;
    @Exclude
    private Map<String, Booking> bookingsOnHour;

    public Employee() {
        bookings = new ArrayList<>();
    }

    public String getFirmId() {
        return firmId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Exclude
    public List<Booking> getBookings() {
        return bookings;
    }

    @Exclude
    public void setWorkingHours(List<String> workingHours) {
        this.workingHours = workingHours;
    }

    @Exclude
    public Schedule getSchedule() {
        return schedule;
    }

    @Exclude
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    @Exclude
    public List<String> getWorkingHours() {
        return workingHours;
    }

    @Exclude
    public Booking getBookingOnTime(String time) {
        return bookingsOnHour.get(time);
    }

    @Exclude
    public Booking getActiveBookingOnTime(String time) {
        Booking booking = bookingsOnHour.get(time);
        if (booking != null && (booking.isActive() || booking.isPending())) {
            return booking;
        }
        return null;
    }

    @Exclude
    public List<Booking> getBookingsOnHour(String hour) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking : this.bookings) {
            if (booking.getEightDate().getHoursAsInteger().equals(Integer.valueOf(hour))) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    @Exclude
    public List<Booking> getActiveAndPendingBookingsOnHour(String hour) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking : bookingsOnHour.values()) {
            if (!booking.isDeclined()) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    @Exclude
    public Map<String, Booking> getBookingsOnHour() {
        return bookingsOnHour;
    }

    @Exclude
    public List<Map<String, Booking>> getBookingsOnHourAsList() {
        List<Map<String, Booking>> list = new ArrayList<>();
        for (String time : bookingsOnHour.keySet()) {
            Map<String, Booking> bookingMap = new HashMap<>();
            Booking b = bookingsOnHour.get(time);
            bookingMap.put(time, b);
            list.add(bookingMap);
        }
        Comparator<Map<String, Booking>> mapComparator = (m1, m2) -> {
            String time1 = ((String) m1.keySet().toArray()[0]);
            String time2 = ((String) m2.keySet().toArray()[0]);
            time1 = time1.split(":")[0] + time1.split(":")[1];
            time2 = time2.split(":")[0] + time2.split(":")[1];
            Integer t1 = Integer.valueOf(time1);
            Integer t2 = Integer.valueOf(time2);
            if (t1 > t2) {
                return 1;
            } else {
                return -1;
            }
        };
        Collections.sort(list, mapComparator);
        return list;
    }

    @Exclude
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    @Exclude
    public void computeBookings() {
        bookingsOnHour = new HashMap<>();
        if (bookings == null) {
            return;
        }
        for (Booking booking : bookings) {
            String time = booking.getEightDate().timeAsString();
            bookingsOnHour.put(time, booking);
        }
    }

    @Exclude
    public String bookingCanBeAddedOnHoursAndMinutes(String proposedHour, String proposedMinutes, Product proposedProduct) {
        String proposedTime = proposedHour + ":" + proposedMinutes;
        Booking activeBooking = getActiveBookingOnTime(proposedTime);
        if (activeBooking == null) {
            List<Booking> bookings = getActiveAndPendingBookingsOnHour(proposedHour);
            for (Booking existingBooking : bookings) {
                String overlapsString = proposedBookingOverlapsWithBooking(proposedTime, proposedProduct, existingBooking);
                if (overlapsString != null) {
                    return overlapsString;
                }
            }
        } else {
            return "Booking exists at " + proposedTime;
        }
        return null;
    }

    @Exclude
    private String proposedBookingOverlapsWithBooking(String proposedTime, Product proposedProduct,
                                                      Booking existingBooking) {
        // PROPOSED BOOKING
        Integer proposedHour = Integer.valueOf(proposedTime.split(":")[0]);
        Integer proposedMinutes = Integer.valueOf(proposedTime.split(":")[1]);
        Integer proposedProductDuration = proposedProduct.calculateDurationAsMinutes();
        String proposedBookingEndTime = calculateBookEndTime(proposedTime, proposedProductDuration);
        Integer proposedBookingEndHour = Integer.valueOf(proposedBookingEndTime.split(":")[0]);
        Integer proposedBookingEndMinutes = Integer.valueOf(proposedBookingEndTime.split(":")[1]);

        // EXISTING BOOKING
        Product existingProduct = DataModel.getInstance().getProductFromId(existingBooking.getProduct().getId());
        //Product existingProduct = null;
        String existingBookingTime = existingBooking.getEightDate().timeAsString();
        Integer existingBookingHour = Integer.valueOf(existingBookingTime.split(":")[0]);
        Integer existingBookingMinutes = Integer.valueOf(existingBookingTime.split(":")[1]);
        String existingBookingEndTime = calculateBookEndTime(existingBookingTime, existingProduct.calculateDurationAsMinutes());
        Integer existingBookingEndHour = Integer.valueOf(existingBookingEndTime.split(":")[0]);
        Integer existingBookingEndMinutes = Integer.valueOf(existingBookingEndTime.split(":")[1]);

        if (existingBookingHour.equals(proposedHour)) {
            if (existingBookingEndHour > proposedHour) {
                return "Your booking overlaps with another booking. It will end at around " +
                       existingBookingEndTime + ".";
            } else if (existingBookingEndMinutes > proposedMinutes) {
                return "Your booking overlaps with another booking. It will end at around " +
                       existingBookingEndTime + ".";
            }

        } else if (existingBookingHour < proposedHour) {
            // check existing booking end hour
            if (existingBookingEndHour.equals(proposedHour)) {
                if (existingBookingEndMinutes > proposedMinutes) {
                    return "Your booking overlaps with another booking. It will end at around " +
                           existingBookingEndTime + ".";
                }
            } else if (existingBookingEndHour > proposedHour) {
                return "Your booking overlaps with another booking. It will end at around " +
                       existingBookingEndTime + ".";
            }
        } else {
            if (proposedBookingEndHour.equals(existingBookingHour)) {
                if (proposedBookingEndMinutes > existingBookingMinutes) {
                    return "Your booking overlaps with another booking. It will start at " +
                           existingBookingTime + ".";
                }
            } else if (proposedBookingEndHour > existingBookingHour) {
                return "Your booking overlaps with another booking. It will start at " +
                       existingBookingTime + ".";
            }
        }
        return null;
    }

    @Exclude
    private String proposedBookingTakesLongerThan(String proposedTime, Product proposedProduct, Booking existingBooking) {
        Integer existingProductDuration = proposedProduct.calculateDurationAsMinutes();
        String proposedBookingEndTime = calculateBookEndTime(proposedTime, existingProductDuration);
        Integer proposedEndHours = Integer.valueOf(proposedBookingEndTime.split(":")[0]);
        Integer proposedEndMinutes = Integer.valueOf(proposedBookingEndTime.split(":")[1]);
        Integer existingBookHour = Integer.valueOf(existingBooking.getEightDate().timeAsString().split(":")[0]);
        Integer existingBookMinutes = Integer.valueOf(existingBooking.getEightDate().timeAsString().split(":")[1]);
        if (proposedEndHours >= existingBookHour) {
            if (proposedEndMinutes > existingBookMinutes) {
                return "There is another booking at " +
                       existingBooking.getEightDate().timeAsString() + ". Your booking ends at " +
                       proposedBookingEndTime +
                       " minutes! Please choose another time for your booking.";
            }
        } else {
            int hDiff = proposedEndHours - existingBookHour;
            int mDiff = proposedEndMinutes - existingBookMinutes;
            if (proposedEndMinutes < existingBookMinutes) {
                mDiff = mDiff + 60;
                hDiff = hDiff - 1;
            }
            Integer diff = Tools.calculateDurationAsMinutes("" + hDiff + "-" + mDiff);
            if (existingProductDuration > diff) {
                return "There is another booking at " +
                       existingBooking.getEightDate().timeAsString() + ". Your booking ends at " +
                       proposedBookingEndTime +
                       " minutes! Please choose another time for your booking.";
            }
        }
        return null;
    }

    @Exclude
    public String calculateBookEndTime(String startTime, Integer duration) {
        int h = Integer.valueOf(startTime.split(":")[0]);
        int m = Integer.valueOf(startTime.split(":")[1]);
        if (duration >= 60) {
            int c = 0;
            int l = duration / 60;
            for (int i = 1; i <= l; i++) {
                c++;
                duration -= 60;
            }
            h = h + c;
            m = m + duration;
            if (m >= 60) {
                h += 1;
                m = m - 60;
            }
        } else {
            m = m + duration;
            if (m >= 60) {
                h += 1;
                m = m - 60;
            }
        }
        String minutes;
        if (m == 0) {
            minutes = m + "0";
        } else {
            minutes = "" + m;
        }
        return h + ":" + minutes;
    }

    @Exclude
    public boolean isWorkingToday(EightDate date) {
        String dayOfWeek = date.getDayAsString();
        switch (dayOfWeek) {
            case "Monday":
                return schedule.isMonday();
            case "Tuesday":
                return schedule.isTuesday();
            case "Wednesday":
                return schedule.isWednesday();
            case "Thursday":
                return schedule.isThursday();
            case "Friday":
                return schedule.isFriday();
            case "Saturday":
                return schedule.isSaturday();
            case "Sunday":
                return schedule.isSunday();
        }

        return false;
    }
}
