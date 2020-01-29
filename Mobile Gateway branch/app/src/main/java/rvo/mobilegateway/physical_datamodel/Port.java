package rvo.mobilegateway.physical_datamodel;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.engines.ConnectivityEngine;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.logical_datamodel.Facility;
import rvo.mobilegateway.login_activity.LoginAuthentificationHandlerContext;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

public class Port implements Comparable{

    public static String RME_PORT_COLLECTION_NAME = "mit_rme_port";
    public static String UNBALANCED_PORT_COLLECTION_NAME = "int_unbalanced_port";
    public static String BALANCED_PORT_COLLECTION_NAME = "int_balanced_port";

    public int id;
    public String name = null;
    public Connection connection = null;
    public Set< Connection > connectionSet = new LinkedHashSet<>( );
    public String portType;
    public AbstractTelcoObject owner;
    public String description;
    public int portNumber;
    public Facility facility;


    public Port( ) {

    }

    public TextView asTextView( Context context ) {
        TextView portTextView = new TextView( context );
        String portConnectionDescription = "";
        portConnectionDescription = portConnectionDescription + this.description + " ";
        if( connection != null ) {
            portConnectionDescription = portConnectionDescription + connection.connectionDescription( );
        }
        portTextView.setText( portConnectionDescription );
        return portTextView;
    }

    public RadioButton asRadioButton( Context context ) {
        RadioButton rb = new RadioButton( context );
        String portConnectionDescription = "";
        portConnectionDescription = portConnectionDescription + this.name;
        if( connection != null ) {
            portConnectionDescription = portConnectionDescription + connection.connectionDescription( );
        }
        rb.setText( portConnectionDescription );
        List details = ConnectivityEngine.networkConnectionDetailsForId( this.id );

        if( !details.isEmpty( ) && ( boolean ) details.get( 0 ) ) {
            int c = ( int ) details.get( 1 );
            rb.setTextColor( c );
        }

        rb.setTypeface( null, Typeface.BOLD );
        rb.setId( this.id );
        return rb;
    }

    @Override
    public String toString( ) {
        return name;
    }


    @Override
    public int compareTo( Object another ) {
        if( this.portNumber < ( ( Port ) another ).portNumber ) {
            return -1;
        } else if( this.portNumber > ( ( Port ) another ).portNumber ) {
            return 1;
        }
        return 0;
    }

    public List< Object > portConnectedObjects( ) {
        List< Object > connectedObjects = new ArrayList<>( );
        connectedObjects.addAll( connectionSet );
        if( facility != null ) {
            connectedObjects.add( facility );
        }

        return connectedObjects;
    }

    public String getPortCollection( ) {
        if( portType.equals( "mit_rme_port" ) ) {
            return Port.RME_PORT_COLLECTION_NAME;
        } else if( portType.equals( "int_balanced_port" ) ) {
            return Port.BALANCED_PORT_COLLECTION_NAME;
        } else if( portType.equals( "int_unbalanced_port" ) ) {
            return Port.UNBALANCED_PORT_COLLECTION_NAME;
        }
        return "";
    }


}
