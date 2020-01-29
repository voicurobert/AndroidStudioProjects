package rvo.mobilegateway.physical_datamodel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rvo.mobilegateway.engines.ASynchronousThreadExecutor;
import rvo.mobilegateway.engines.ConnectivityEngine;

/**
 * Created by Robert on 9/5/2015.
 */
public class EquipmentObject extends AbstractTelcoObject implements Comparable{

    public static String RACK_COLLECTION_NAME = "mit_bay";
    public static String SHELF_COLLECTION_NAME = "mit_shelf";
    public static String SLOT_COLLECTION_NAME = "mit_slot";
    public static String CARD_COLLECTION_NAME = "mit_card";
    public static String OPTICAL_SPLITTER_COLLECTION_NAME = "optical_splitter";
    public static String SPLICE_CLOSURE_COLLECTION_NAME = "splice_closure";
    public static String SPECIFICATION_NAME = "rme_spec";
    public int id;
    public String equipmentType = ""; // splice, rack, shelves, slot, card
    public String name = "";
    public String alias = "";
    public List<Cable> inCables;
    public List<Cable> outCables;
    public String[] inCablesAsVector;
    public String[] outCablesAsVector;
    public int strwConnectionPointId;
    public String description = "";
    public String longDescription = "";
    public AbstractTelcoObject owner;

    public EquipmentObject( ){

    }

    public boolean addInCable( Cable inCable ){
        if( inCables == null ){
            inCables = new ArrayList<>(  );
        }
        if( !inCables.contains( inCable ) ){
            inCables.add( inCable );
            return true;
        }else{
            return false;
        }
    }

    public boolean addOutCable( Cable outCable ){
        if( inCables == null ){
            inCables = new ArrayList<>(  );
        }
        if( !inCables.contains( outCable ) ){
            inCables.add( outCable );
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        if( equipmentType.equals("splice_closure") ){
            String string = "";
            String inCables = "" + this.inCables.size();
            String outCables = "" + this.outCables.size();
            if( name == null ){
                string = alias;
            }else{
                string = name;
            }
            return string + " IN cables: " + inCables + " and OUT cables: " + outCables;
        }else if( equipmentType.equals("rack")
                || equipmentType.equals( "shelf" )
                || equipmentType.equals( "slot" )
                || equipmentType.equals( "card" ) ){
            return name;
        }else if( equipmentType.equals( "optical_splitter" )){
          // String string = this.description;
            return this.description;
        }
        return "No name";
    }

    public RadioButton asRadioButton( Context context){
        RadioButton rb = new RadioButton(context);
        rb.setText (this.description );
        List details = ConnectivityEngine.networkConnectionDetailsForId( this.id );

        if( !details.isEmpty() && (boolean)details.get( 0 ) ){
            int c = (int)details.get( 1 );
            rb.setTextColor( c );
        }
        rb.setId(this.id);
        rb.setTextColor( Color.BLACK );
        rb.setTypeface( null, Typeface.BOLD );
        return rb;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if( this.id == ((EquipmentObject )o).id ){
            equal = true;
        }
        return equal;
    }


    @Override
    public int compareTo( Object another ){
        String anotherDesc = ( ( EquipmentObject ) another ).description;
        int x = this.description.compareTo( anotherDesc);
        return x;
    }

    public String getCollectionName(){
        String collName = null;
        switch( equipmentType ){
            case "rack" :
                collName = RACK_COLLECTION_NAME;
                break;
            case "shelf" :
                collName = SHELF_COLLECTION_NAME;
                break;
            case "slot" :
                collName = SLOT_COLLECTION_NAME;
                break;
            case "card" :
                collName = CARD_COLLECTION_NAME;
                break;
            case "splice_closure":
                collName = SPLICE_CLOSURE_COLLECTION_NAME;
                break;
            case "optical_splitter" :
                collName = OPTICAL_SPLITTER_COLLECTION_NAME;
                break;
        }
        return collName;
    }

    @Override
    public String getExternalName( ){
        return "EquipmentObject";
    }

    @Override
    public Map< String, String > fieldsAndValues( ){
        return null;
    }

    @Override
    public String getSpecificationName( ){
        return null;
    }


    @Override
    public void prepareTasks( Context context, String collectionName, String id ){
        ASynchronousThreadExecutor aSynchronousThreadExecutor = new ASynchronousThreadExecutor( 2 );
        aSynchronousThreadExecutor.addTaskToRun( setInternalEquipments( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setLogicalObjects( context, collectionName, id) );
        setaSynchronousThreadExecutor( aSynchronousThreadExecutor );
    }


}
