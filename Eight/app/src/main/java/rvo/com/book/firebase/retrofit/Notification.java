package rvo.com.book.firebase.retrofit;

import java.util.Map;

public class Notification {
    private String body;
    private String title;
    private Map<String, String> data;

    public Notification() {

    }

    public Notification(String title, String body) {
        this.body = body;
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}

