package rvo.helpme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class HelpMeLoginActivity extends Activity{


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate( Bundle savedInstanceState ){

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_help_me_login );
        // Set up the login form.
        mEmailView = ( AutoCompleteTextView ) findViewById( R.id.email );


        mPasswordView = ( EditText ) findViewById( R.id.password );
        mPasswordView.setOnEditorActionListener( new TextView.OnEditorActionListener( ){
            @Override
            public boolean onEditorAction( TextView textView, int id, KeyEvent keyEvent ){
                if( id == R.id.login || id == EditorInfo.IME_NULL ){
                    attemptLogin( );
                    return true;
                }
                return false;
            }
        } );

        Button mEmailSignInButton = ( Button ) findViewById( R.id.signInButton );
        mEmailSignInButton.setOnClickListener( new OnClickListener( ){
            @Override
            public void onClick( View view ){
                //attemptLogin( );
            }
        } );

        mLoginFormView = findViewById( R.id.login_form );
        mProgressView = findViewById( R.id.login_progress );
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin( ){

        if( mAuthTask != null ){
            return;
        }

        // Reset errors.
        mEmailView.setError( null );
        mPasswordView.setError( null );

        // Store values at the time of the login attempt.
        String email = mEmailView.getText( ).toString( );
        String password = mPasswordView.getText( ).toString( );

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if( !TextUtils.isEmpty( password ) && !isPasswordValid( password ) ){
            mPasswordView.setError( getString( R.string.error_invalid_password ) );
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if( TextUtils.isEmpty( email ) ){
            mEmailView.setError( getString( R.string.error_field_required ) );
            focusView = mEmailView;
            cancel = true;
        }else if( !isEmailValid( email ) ){
            mEmailView.setError( getString( R.string.error_invalid_email ) );
            focusView = mEmailView;
            cancel = true;
        }

        if( cancel ){
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus( );
        }else{
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress( true );
            mAuthTask = new UserLoginTask( email, password );
            mAuthTask.execute( ( Void ) null );
        }
    }

    private boolean isEmailValid( String email ){
        //TODO: Replace this with your own logic
        return email.contains( "@" );
    }

    private boolean isPasswordValid( String password ){
        //TODO: Replace this with your own logic
        return password.length( ) > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi( Build.VERSION_CODES.HONEYCOMB_MR2 )
    private void showProgress( final boolean show ){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 ){
            int shortAnimTime = getResources( ).getInteger( android.R.integer.config_shortAnimTime );

            mLoginFormView.setVisibility( show ? View.GONE : View.VISIBLE );
            mLoginFormView.animate( ).setDuration( shortAnimTime ).alpha(
                    show ? 0 : 1 ).setListener( new AnimatorListenerAdapter( ){
                @Override
                public void onAnimationEnd( Animator animation ){
                    mLoginFormView.setVisibility( show ? View.GONE : View.VISIBLE );
                }
            } );

            mProgressView.setVisibility( show ? View.VISIBLE : View.GONE );
            mProgressView.animate( ).setDuration( shortAnimTime ).alpha(
                    show ? 1 : 0 ).setListener( new AnimatorListenerAdapter( ){
                @Override
                public void onAnimationEnd( Animator animation ){
                    mProgressView.setVisibility( show ? View.VISIBLE : View.GONE );
                }
            } );
        }else{
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility( show ? View.VISIBLE : View.GONE );
            mLoginFormView.setVisibility( show ? View.GONE : View.VISIBLE );
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask< Void, Void, Boolean >{

        private final String mEmail;
        private final String mPassword;

        UserLoginTask( String email, String password ){
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground( Void... params ){
            // TODO: attempt authentication against a network service.

            try{
                // Simulate network access.
                Thread.sleep( 2000 );
            }catch( InterruptedException e ){
                return false;
            }

            // TODO: Check for auth on server

            // TODO: register the new account here

            return true;
        }

        @Override
        protected void onPostExecute( final Boolean success ){
            mAuthTask = null;
            showProgress( false );

            if( success ){
                finish( );
            }else{
                mPasswordView.setError( getString( R.string.error_incorrect_password ) );
                mPasswordView.requestFocus( );
            }
        }

        @Override
        protected void onCancelled( ){
            mAuthTask = null;
            showProgress( false );
        }
    }
}

