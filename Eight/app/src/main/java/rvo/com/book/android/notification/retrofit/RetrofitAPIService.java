package rvo.com.book.android.notification.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPIService {

    @Headers({"Content-Type:application/json",
              "Authorization:key=AAAAMSNQLJ4:APA91bErsyv3S-BzKFYAUsjpFaqQtphMrv4D6LBTWP7aJW6XubuqyKAyJBmaWxffmOespfpCE06pxu5wmpfPbvO66Wc7scrThMduZ4OWrzN6YQth9JFP4p3pMngmG_bsuQD--gqIxse8"})

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);


}
