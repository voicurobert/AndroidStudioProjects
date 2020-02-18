package rvo.com.book.android.notification.retrofit;

public class Result {
    public String messageId;

    public Result() {

    }

    public Result(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
