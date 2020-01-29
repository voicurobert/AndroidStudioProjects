package rvo.com.book.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class EightDate {
    private Date date;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    private DateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    //private DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.ENGLISH );
    private Calendar calendar;

    public EightDate() {
        calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    public EightDate(Integer dayNumber) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayNumber);
        date = calendar.getTime();
    }

    public EightDate(Integer dayNumber, Integer monthNumber, Integer year) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayNumber);
        calendar.set(year, monthNumber, dayNumber);
        date = calendar.getTime();
    }

    public EightDate(String restDate) {
        try {
            date = dateTimeFormat.parse(restDate.replaceAll(Pattern.quote("+0300"), "Z$"));
            calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM_PM);
            calendar.setTime(date);
            // call a get method so that the calendar computes setTime( date )
            calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setHoursAndMinutes(String hours, String minutes) {
        if (hours == null || hours.equals("")) {
            calendar.getTime();
            date = calendar.getTime();
            return;
        }
        String[] vec = hours.split(":");
        if (!vec[0].equals("")) {
            calendar.set(Calendar.HOUR, Integer.valueOf(hours.split(":")[0]));
        }
        if (!minutes.equals("")) {
            calendar.set(Calendar.MINUTE, Integer.valueOf(minutes));
        }
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.getTime();
        date = calendar.getTime();
    }

    public Integer getDayAsInteger() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String getDayAsString() {
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
    }

    public Integer getMonthAsInteger() {
        return calendar.get(Calendar.MONTH);
    }

    public String getMonthAsString() {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
    }

    public Integer getYearAsInteger() {
        return calendar.get(Calendar.YEAR);
    }

    public String getYearAsString() {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public Integer getHoursAsInteger() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public String getHoursAsString() {
        return "" + getHoursAsInteger();
    }


    public Integer getMinutesAsInteger() {
        return calendar.get(Calendar.MINUTE);
    }

    public String getMinutesAsString() {
        return "" + getMinutesAsInteger();
    }

    public Date getDate() {
        return date;
    }

    public String getDateInRestFormat() {
        return dateTimeFormat.format(date).replaceAll(Pattern.quote("+0300"), "Z");
    }

    public boolean sameDateAs(EightDate another) {
        return this.getYearAsString().equals(another.getYearAsString()) &&
               this.getMonthAsString().equals(another.getMonthAsString()) &&
               this.getDayAsInteger().equals(another.getDayAsInteger());
    }

    public boolean inPastAs(EightDate another) {
        return this.getYearAsInteger() <= another.getYearAsInteger() &&
               this.getMonthAsInteger() <= another.getMonthAsInteger() &&
               this.getDayAsInteger() < another.getDayAsInteger();
    }

    public boolean onSame(EightDate another) {
        return this.getYearAsInteger() <= another.getYearAsInteger() &&
               this.getMonthAsInteger() <= another.getMonthAsInteger() &&
               this.getDayAsInteger() <= another.getDayAsInteger();
    }

    public String timeAsString() {
        String h = getHoursAsString();
        String m = getMinutesAsString();
        if (m.equals("0")) {
            m = m + "0";
        }
        return h + ":" + m;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDayAsString());
        stringBuilder.append(", ");
        stringBuilder.append(getDayAsInteger());
        stringBuilder.append(" ");
        stringBuilder.append(getMonthAsString());
        stringBuilder.append(" ");
        //stringBuilder.append( getYearAsString() );
        //stringBuilder.append( " " );
        return stringBuilder.toString();
    }
}
