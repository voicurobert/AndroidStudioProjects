package rvo.mobilegateway.login_activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Robert on 11/2/2015.
 */
public final class LoginAuthentificationHandlerContext{

    private static LoginAuthentificationHandlerContext instance = new LoginAuthentificationHandlerContext();
    private final String LOGIN_CONTEXT_PREFS_NAME = "LOGIN_CONTEXT";
    private final String SAVE_LOGIN_INFO_KEY = "LOGIN_INFO_CHECKED";
    private Context context;
    private SharedPreferences sharedPreferences;

    private LoginAuthentificationHandlerContext( ){

    }

    public static LoginAuthentificationHandlerContext getInstance( ) {
        return instance;
    }

    public void setContext( Context context ) {
        this.context = context;
        // set shared preferences if it doesn't exist.
        sharedPreferences = this.context.getSharedPreferences( LOGIN_CONTEXT_PREFS_NAME, 0 );
    }

    public Context getContext( ) {
        return context;
    }

    public String getUsername( ) {
        return getDataFromSharedPrefs( "USERNAME" );
    }

    public String getPassword( ) {
        return getDataFromSharedPrefs( "PASSWORD" );
    }

    public boolean setSaveLoginInfoStatus( boolean value ){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean( SAVE_LOGIN_INFO_KEY, value );
        return editor.commit();
    }

    public boolean addStringDataToSharedPrefs( String key, String value ){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString( key, value );
        return editor.commit( );
    }

    public String getDataFromSharedPrefs( String key ){
        if( this.sharedPreferences == null ){
            if( context != null ){
                sharedPreferences = this.context.getSharedPreferences( LOGIN_CONTEXT_PREFS_NAME, 0 );
            }

        }
        return this.sharedPreferences.getString( key, "" );
    }

    public boolean isSaveLoginInfoChecked(){
        return this.sharedPreferences.getBoolean( SAVE_LOGIN_INFO_KEY, false );
    }
}
