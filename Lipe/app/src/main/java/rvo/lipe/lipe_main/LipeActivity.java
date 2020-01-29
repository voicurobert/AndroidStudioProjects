package rvo.lipe.lipe_main;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.appevents.AppEventsLogger;

import rvo.lipe.R;
import rvo.lipe.chat.ChatActivity;
import rvo.lipe.settings.SettingsActivity;


public class LipeActivity extends AppCompatActivity{
    
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        AppEventsLogger.activateApp( getApplication() );
        
        setContentView( R.layout.activity_main );
        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
    }
    
    
    private void startSettingsActivity(){
        Intent settingsActivity = new Intent( getApplicationContext(), SettingsActivity.class );
        settingsActivity.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        getApplicationContext().startActivity( settingsActivity );
        finish();
    }
    
    private void startChatActivity(){
        Intent chatActivity = new Intent( getApplicationContext(), ChatActivity.class );
        chatActivity.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        getApplicationContext().startActivity( chatActivity );
        finish();
    }
}
