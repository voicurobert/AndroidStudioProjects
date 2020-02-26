package rvo.com.book.android.main_app.billing;

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

import rvo.com.book.Log;
import rvo.com.book.common.Tools;

public class InAppBilling implements PurchasesUpdatedListener, BillingClientStateListener, AcknowledgePurchaseResponseListener {

    private static final String MONTHLY_SUBSCRIPTION = "8_subscription.";
    private BillingClient billingClient;
    private Activity activity;
    private IBillingResponse billingResponse;
    private static SkuDetailsParams skuDetailsParams;
    private static InAppBilling instance = new InAppBilling();

    static {
        List<String> skuList = new ArrayList<>();
        skuList.add(MONTHLY_SUBSCRIPTION);
        skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.SUBS).build();
    }


    private InAppBilling() {

    }

    public static InAppBilling getInstance() {
        return instance;
    }

    public void start(Activity activity) {
        this.activity = activity;
        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(this);
    }

    protected void setBillingResponse(IBillingResponse billingResponse) {
        this.billingResponse = billingResponse;
    }


    public void isSubscribed(IBillingResponse billingResponse) {
        if (!billingClient.isReady()) {
            billingClient.startConnection(this);
        }
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult, purchaseHistoryRecordList) -> {
            List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
            if (purchases != null && !purchases.isEmpty()) {
                for (Purchase purchase : purchases) {
                    Log.log("STATE: " + purchase.getPurchaseState());
                    if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                        billingResponse.subscribed(false);
                    } else {
                        billingResponse.subscribed(true);
                    }
                }
            } else {
                billingResponse.subscribed(false);
            }
        });
    }

    public BillingClient getBillingClient() {
        return billingClient;
    }


    protected void subscribe(IBillingResponse response) {
        Tools.isGooglePlayServicesAvailable(activity);
        billingClient.querySkuDetailsAsync(skuDetailsParams, (billingResult, skuDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                response.subscribed(false);
            }
            if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetailsList.get(0)).build();
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
        Log.log("Service disconnected!!!");
        remakeConnection();
    }

    protected void remakeConnection() {
        if (!billingClient.isReady()) {
            billingClient.endConnection();
            billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(this);
        }

    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
        Log.log("onAcknowledgePurchaseResponse" + billingResult.getDebugMessage());
    }
}
