package com.rwee.mobilegatewaywo.wo_datamodel;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.custom_view.CustomEditText;
import com.rwee.mobilegatewaywo.custom_view.CustomSpinner;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Robert on 10/02/16.
 */
public abstract class AbstractWaterOfficeObject{
    public int id;
    public List< Object > internalEquipment;
    public boolean hasSpecification = true; // this defaults to true. if a specific instance of this object does not have specification then set it to false
    public String specificationName = "";


    public abstract String getDatasetName();
    public abstract String getCollectionName();
    public abstract String getExternalName();
    public abstract String getSpecificationName();
    public abstract  Map< String, String > fieldsAndValues();

    public LinearLayout getLayoutForEditor( final Activity activity ) {
        LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        final Map< String, String > fieldsAndValues = fieldsAndValues();
        Set< String > fields = fieldsAndValues.keySet();
        for( final String field : fields ){
            if( SmallworldFieldMetadata.instance.FIELD_ENUMERATION_MAPPING.get( field ) ){
                LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.enumerated_field_value_edit_layout, null );
                TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
                fieldTV.setText( field );
                final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );

                valueSpinner.setEnumeratedValues(
                        activity,
                        this.getDatasetName( ),
                        this.getCollectionName( ),
                        SmallworldFieldMetadata.instance.FIELD_MAPPING.get( field ),
                        fieldsAndValues.get( field ) );
                valueSpinner.setTextView( fieldTV );
                linearLayout.addView( childLinearLayout );

            }else{
                LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater( ).inflate( R.layout.field_value_edit_layout, null );
                TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
                fieldTV.setText( field );
                CustomEditText valueTV = ( CustomEditText ) childLinearLayout.findViewById( R.id.valueTV );
                valueTV.setText( fieldsAndValues.get( field ) );
                valueTV.setLabel( fieldTV );
                linearLayout.addView( childLinearLayout );
            }
        }
        if( hasSpecification ){
            addSpecificationField( linearLayout, activity );
        }
        return linearLayout;
    }


    private void addSpecificationField( LinearLayout linearLayout, Activity activity ){
        LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.enumerated_field_value_edit_layout, null );
        TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
        fieldTV.setText( "Specification" );

        final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );
        valueSpinner.setSpecifications(
                activity,
                this.getDatasetName( ),
                this.getCollectionName( ),
                specificationName );

       // valueSpinner.setOnItemSelectedListener( MobileGatewayWOMapActivity.masterActivity.getEditorFragment( ) );
        valueSpinner.setTextView( fieldTV );
        linearLayout.addView( childLinearLayout );
    }


    @Override
    public boolean equals( Object o ){
        if( this.id == (( AbstractWaterOfficeObject )o).id ){
            return true;
        }
        return false;
    }
}
