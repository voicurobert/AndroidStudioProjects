package com.rwee.mobilegatewaywo.login_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.SettingsActivity;
import com.rwee.mobilegatewaywo.SystemVerificationManager;
import com.rwee.mobilegatewaywo.engines.SetOnResultAvailableListener;
import com.rwee.mobilegatewaywo.engines.SmallworldServicesDelegate;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Robert on 10/30/2015.
 */
public class LoginActivity extends AppCompatActivity{


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_activity );
        final Context context = getApplicationContext( );
        LoginAuthentificationHandlerContext.getInstance().setContext( context );
        SystemVerificationManager.verify( this );
        final LoginActivity loginActivity = this;

        Button loginButton = ( Button ) findViewById( R.id.loginButtonId );

        final EditText usernameED = ( EditText ) findViewById( R.id.usernameEditText );
        final EditText passwordET = ( EditText ) findViewById( R.id.passwordEditText );

        final CheckBox saveLoginInfoCheckBox = ( CheckBox ) findViewById( R.id.saveLoginSettingsCheckBoxId );

        if( LoginAuthentificationHandlerContext.getInstance().isSaveLoginInfoChecked() ){
            // set checked value
            saveLoginInfoCheckBox.setChecked( true );
            // set username and password
            usernameED.setText( LoginAuthentificationHandlerContext.getInstance().getUsername() );
            passwordET.setText( LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        }
        loginButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ) {
                final Editable usernameEditable = usernameED.getText( );
                if( usernameEditable == null || usernameEditable.toString().equals( "" )){
                    L.t( context, "Please enter a username..." );
                }
                final String username = usernameED.getText( ).toString( );
                final String password = passwordET.getText( ).toString( );
                if( LoginAuthentificationHandlerContext.getInstance().isSaveLoginInfoChecked() ){
                    LoginAuthentificationHandlerContext.getInstance().addStringDataToSharedPrefs( "USERNAME", username );
                    LoginAuthentificationHandlerContext.getInstance().addStringDataToSharedPrefs( "PASSWORD", password );
                }
                if( SystemVerificationManager.instance.isGssServer() ){
                    SmallworldServicesDelegate.instance.callLoginService(
                            loginActivity,
                            context,
                            new SetOnResultAvailableListener( ){
                                @Override
                                public void onResultAvailable( List< HashMap< String, String > > result ) {
                                    if( result.get( 0 ).containsKey( "response_error" ) ){
                                        L.t( context, "Login error" );
                                    }else{
                                        L.t( context, "Logged in with user" );
                                        Intent masterActivity = new Intent( getApplicationContext(), MobileGatewayWOMapActivity.class );
                                        masterActivity.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                        startActivity( masterActivity );
                                        finish( );
                                    }
                                }
                            } );
                }else{
                    Intent settingsIntent = new Intent( context, SettingsActivity.class );
                    context.startActivity( settingsIntent );
                }
            }
        } );

        /*
        offlineModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.m( "Am apasat butonul offline" );
                L.t( getApplicationContext(), "Am apasat butonul offline");
                Intent masterActivity = new Intent( getApplicationContext(), MobileGatewayWOMapActivity.class );
                masterActivity.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(masterActivity);
                finish();
            }
        });
        */

        saveLoginInfoCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ){
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                if( isChecked ) {
                    // check box becomes true:
                    // -> save login info status
                    LoginAuthentificationHandlerContext.getInstance( ).setSaveLoginInfoStatus( true );
                }else{
                    LoginAuthentificationHandlerContext.getInstance( ).setSaveLoginInfoStatus( false );
                }
            }
        } );
    }

    @Override
    protected void onResume( ) {
        super.onResume( );
    }
}
