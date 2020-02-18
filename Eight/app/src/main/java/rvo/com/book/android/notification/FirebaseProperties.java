package rvo.com.book.android.notification;

import rvo.com.book.android.notification.retrofit.RetrofitAPIService;
import rvo.com.book.android.notification.retrofit.RetrofitClient;

public class FirebaseProperties {
    private String currentToken;
    private static FirebaseProperties instance;
    private static String baseUrl = "https://fcm.googleapis.com/";

    private FirebaseProperties() {

    }

    public static FirebaseProperties getInstance() {
        if (instance == null) {
            instance = new FirebaseProperties();
        }
        return instance;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;

    }

    public static RetrofitAPIService getFCMClient() {
        return RetrofitClient.getRetrofitClient(baseUrl).create(RetrofitAPIService.class);
    }
}
