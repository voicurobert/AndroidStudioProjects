package rvo.com.book.android.notification.retrofit;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Sender implements Serializable {
    private Notification notification;
    private String to;

    private List<Map<String, String>> data;

    public Sender() {

    }

    public Sender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getTo() {
        return to;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<Map<String, String>> getData() {
        return data;
    }
}
