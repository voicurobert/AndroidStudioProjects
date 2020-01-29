package com.rwee.mobilegatewaywo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;import android.widget.EditText;
import android.widget.TextView;
import com.rwee.mobilegatewaywo.connectivity_activity.SetOnInitialisationCompleteListener;
import com.rwee.mobilegatewaywo.engines.SmallworldServicesDelegate;
import java.util.HashMap;
import java.util.List;



public class SettingsActivity extends AppCompatActivity implements TextView.OnEditorActionListener{
	
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.settings_activity );
        final EditText serverUrlEditText = (EditText) findViewById( R.id.urlEditTextId );
        final EditText serverPortEditText = (EditText) findViewById( R.id.portEditTextId );
        final AppCompatActivity context = this;
        Button saveButton = (Button) findViewById( R.id.saveButtonId );
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String serverUrl = serverUrlEditText.getText().toString();
                String serverPort = serverPortEditText.getText().toString();
                saveToSharedPreferences( "serverUrl", serverUrl );
                saveToSharedPreferences( "serverPort", serverPort );
                Constants.instance.serverUrl = serverUrl;
                Constants.instance.serverPort = serverPort;

                SmallworldServicesDelegate.instance.callGSSDescribe( context, new SetOnInitialisationCompleteListener( ){
                    @Override
                    public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ){
                        if( complete ){
                            SystemVerificationManager.instance.setGssServer( true );
                            context.finish();
                        }else{
                            L.t( context, "No response from GSS server. Consult your system administrator!" );
                        }
                    }

                    @Override
                    public void onEquipmentsInitialisationComplete( boolean initialised ){

                    }

                    @Override
                    public void onConnectedCablesInitialisationComplete( boolean initialised ){

                    }

                    @Override
                    public void onLogicalObjectsInitialisationComplete( boolean initialised ){

                    }

                    @Override
                    public void onPortsInitialisationComplete( boolean initialised ){

                    }
                } );
               // finish( );
            }
        });

        String savedUrl = getFromSharedPreferences( "url" );
        String savedPort = getFromSharedPreferences("port");
        serverPortEditText.setText( savedPort );
        serverUrlEditText.setText(savedUrl);

        serverUrlEditText.setOnEditorActionListener(this);
	}

    private String getFromSharedPreferences( String whatToGet ){
        SharedPreferences gssSettings = getSharedPreferences("GSS_SETTINGS", 0);
        String whatToReturn = "localhost";
        switch ( whatToGet ){
            case "url" : whatToReturn = gssSettings.getString("serverUrl", "localhost");
                break;
            case "port" : whatToReturn = gssSettings.getString( "serverPort", "8080" );
                break;
        }
        return whatToReturn;
    }

    private void saveToSharedPreferences( String key, String value ){
        SharedPreferences gssSettings = getSharedPreferences("GSS_SETTINGS", 0);
        SharedPreferences.Editor ed = gssSettings.edit();
        ed.putString( key, value );
        ed.commit();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed( ){
        if( SystemVerificationManager.instance.isGssServer() ){
            super.onBackPressed( );
        }else{
            L.t( this, "GSS server is not responding!" );
        }
    }
}
