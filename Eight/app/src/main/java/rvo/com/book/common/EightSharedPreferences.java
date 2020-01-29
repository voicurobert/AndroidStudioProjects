package rvo.com.book.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class EightSharedPreferences {
    private static EightSharedPreferences instance;
    public static final String CUSTOMER_EMAIL_KEY = "customerEmail";
    public static final String CUSTOMER_PASSWORD_KEY = "customerPassword";
    public static final String FIRM_EMAIL_KEY = "firmEmail";
    public static final String FIRM_PASSWORD_KEY = "firmPassword";
    public static final String MODE = "mode";
    public static final String CUSTOMER_MODE = "customerMode";
    public static final String FIRM_MODE = "firmMode";

    private SharedPreferences sharedPreferences;

    private EightSharedPreferences() {

    }

    public static EightSharedPreferences getInstance() {
        if (instance == null) {
            instance = new EightSharedPreferences();
        }

        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void createSharedPreferencesForActivity(Activity activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value, Activity activity) {
        if (sharedPreferences == null) {
            createSharedPreferencesForActivity(activity);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public boolean isSharedPreferencesSet() {
        return sharedPreferences != null;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean modeEnabled() {
        return getString(MODE) != null;
    }

    public boolean isFirmMode() {
        return getString(MODE).equals(FIRM_MODE);
    }

    public boolean isCustomerMode() {
        return getString(MODE).equals(CUSTOMER_MODE);
    }

    public void signOut(Activity activity) {
        if (isFirmMode()) {
            saveString(FIRM_EMAIL_KEY, null, activity);
            saveString(FIRM_PASSWORD_KEY, null, activity);
        } else {
            saveString(CUSTOMER_EMAIL_KEY, null, activity);
            saveString(CUSTOMER_PASSWORD_KEY, null, activity);
        }
    }
}
