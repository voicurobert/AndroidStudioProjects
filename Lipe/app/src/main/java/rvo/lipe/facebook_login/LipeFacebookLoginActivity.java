package rvo.lipe.facebook_login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import rvo.lipe.lipe_main.LipeActivity;
import rvo.lipe.lipe_rest.LipeRequestCreator;
import rvo.lipe.lipe_rest.LipeRequestExecutor;
import rvo.lipe.FacebookPermissions;
import rvo.lipe.LipeLog;
import rvo.lipe.R;

/**
 * Created by Robert on 28/06/2017.
 */

public class LipeFacebookLoginActivity extends AppCompatActivity{
    
    private CallbackManager callbackManager;
    private CurrentLocation currentLocation;
    
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.facebook_login_activity_layout );
        callbackManager = CallbackManager.Factory.create();
        currentLocation = new CurrentLocation( this );
        
        AccessToken.refreshCurrentAccessTokenAsync();
        AccessToken.setCurrentAccessToken( null );
        if( isTheUserLoggedWithFacebook() ){
            LipeLog.log( "User already logged in with FACEBOOK" );
            //startLipeMainActivity();
        }
        final LoginButton loginButton = findViewById( R.id.loginButton );
        loginButton.setReadPermissions( FacebookPermissions.getAllPermissions() );
        loginButton.registerCallback( callbackManager, new FacebookCallback< LoginResult >(){
            @Override
            public void onSuccess( LoginResult loginResult ){
                // facebook id
                String facebookId = loginResult.getAccessToken().getUserId();
                saveFacebookIdToPreferences( facebookId );
                // access token
                String accessToken = loginResult.getAccessToken().getToken();
                
                LipeLog.log( accessToken );
                // insert this user to database
                LipeRequestExecutor requestCreator = new LipeRequestExecutor( LipeRequestCreator.computeInsertRequest( facebookId, accessToken ) );
                requestCreator.execute();
                //start lipe activity
                //startLipeMainActivity();
            }
            
            @Override
            public void onCancel(){
                LipeLog.log( "Pressed Cancel button" );
            }
            
            
            @Override
            public void onError( FacebookException error ){
                LipeLog.log( error.getMessage() );
            }
        } );
    }
    
    @Override
    protected void onStart(){
        super.onStart();
        currentLocation.setCurrentLocation();
        Location loc = currentLocation.getCurrentLocation();
       // LipeLog.log( loc.toString() );
    }
    
    @Override
    protected void onStop(){
        super.onStop();
        
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }
    
    private void saveFacebookIdToPreferences( String facebookId ){
        SharedPreferences preferences = this.getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString( "facebookId", facebookId );
        preferencesEditor.apply();
    }
    
    private boolean isTheUserLoggedWithFacebook(){
        
        return AccessToken.getCurrentAccessToken() != null;
    }
    
    private void startLipeMainActivity(){
        Intent lipeMainActivity = new Intent( getApplicationContext(), LipeActivity.class );
        lipeMainActivity.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        getApplicationContext().startActivity( lipeMainActivity );
        finish();
    }
}
