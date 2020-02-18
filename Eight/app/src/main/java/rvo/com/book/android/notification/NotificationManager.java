package rvo.com.book.android.notification;


import rvo.com.book.android.notification.retrofit.Notification;
import rvo.com.book.android.notification.retrofit.RetrofitAPIService;
import rvo.com.book.android.notification.retrofit.Sender;

public class NotificationManager {
    private static NotificationManager instance;
    private RetrofitAPIService apiService;

    private NotificationManager() {
        apiService = FirebaseProperties.getFCMClient();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void sendNotification(String title, String body, String toSender) {
        Notification notification = new Notification(title, body);
        Sender sender = new Sender();
        sender.setNotification(notification);
        sender.setTo(toSender);
        Thread thread = new Thread(() -> {
            try {
                apiService.sendNotification(sender).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
