package rvo.com.book.android.notification.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitClient;

    private RetrofitClient() {

    }

    public static Retrofit getRetrofitClient(String baseUrl) {
        if (retrofitClient == null) {
            retrofitClient = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitClient;
    }
}
