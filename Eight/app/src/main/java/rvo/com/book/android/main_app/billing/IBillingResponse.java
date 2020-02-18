package rvo.com.book.android.main_app.billing;

@FunctionalInterface
public interface IBillingResponse {

    void subscribed(boolean subscribed);
}
