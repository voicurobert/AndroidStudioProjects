package rvo.com.book.billing;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.common.Log;

public class InAppBilling implements PurchasesUpdatedListener, BillingClientStateListener, AcknowledgePurchaseResponseListener {

    private static final String MONTHLY_SUBSCRIPTION = "8_subscription.";
    private BillingClient billingClient;
    private Activity activity;
    private static InAppBilling instance;
    private IBillingResponse billingResponse;
    private static SkuDetailsParams skuDetailsParams;

    static {
        List<String> skuList = new ArrayList<>();
        skuList.add(MONTHLY_SUBSCRIPTION);
        skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.SUBS).build();
    }


    private InAppBilling(Activity activity) {
        this.activity = activity;
        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(this);
    }

    protected void setBillingResponse(IBillingResponse billingResponse) {
        this.billingResponse = billingResponse;
    }

    public static InAppBilling getInstance() {
        return instance;
    }

    public static void setActivity(Activity activity) {
        instance.activity = activity;
    }

    public static void initInstance(Activity activity) {
        instance = new InAppBilling(activity);
    }

    public void isSubscribed(IBillingResponse billingResponse) {
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult, purchaseHistoryRecordList) -> {
            List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
            if (purchases != null && !purchases.isEmpty()) {
                for (Purchase purchase : purchases) {
                    if (purchase.getPurchaseState() ==
                        Purchase.PurchaseState.UNSPECIFIED_STATE) {
                        billingResponse.subscribed(false);
                        return;
                    }
                    billingResponse.subscribed(true);
                }
            } else {
                billingResponse.subscribed(false);
            }
        });
    }

    protected BillingClient getBillingClient() {
        return billingClient;
    }


    protected void subscribe() {
        Eight.isGooglePlayServicesAvailable(activity);
        billingClient.querySkuDetailsAsync(skuDetailsParams, (billingResult, skuDetailsList) -> {
            if (billingResult.getResponseCode() ==
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                EightAlertDialog.showAlertWithMessage(
                        "Are you connected to google play? Please check that you are logged in.",
                        activity);
            }
            if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(
                        skuDetailsList.get(0)).build();
                billingClient.launchBillingFlow(activity, flowParams);
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        int responseCode = billingResult.getResponseCode();
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            }
        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            billingResponse.subscribed(false);
        }
    }


    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
            }
            billingResponse.subscribed(true);
        } else {
            billingResponse.subscribed(false);
        }
    }

    @Override
    public void onBillingSetupFinished(BillingResult billingResult) {
    }

    @Override
    public void onBillingServiceDisconnected() {
        remakeConnection();
    }

    private void remakeConnection() {
        billingClient.startConnection(this);
    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
        Log.log("onAcknowledgePurchaseResponse" + billingResult.getDebugMessage());
    }
}
