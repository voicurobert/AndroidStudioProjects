package com.rwee.mobilegatewaywo.wo_datamodel.water_supply;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.rwee.mobilegatewaywo.GlobalHandler;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
import com.rwee.mobilegatewaywo.wo_datamodel.SmallworldFieldMetadata;
import com.rwee.mobilegatewaywo.wo_datamodel.WOLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 10/02/16.
 */
public class WSTee extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "ws_tee";
    public static final String EXTERNAL_NAME = "Tee";
    public static final String DATASET_NAME = "water_supply";
    public static final String SPECIFICATION_NAME = "ws_tee_spec";
    // fields
    public String network = "";
    public String state = "";
    public String teeDirection = "";
    public WOLocation woLocation;

    public WSTee(){

    }

    @Override
    public String getDatasetName( ){
        return DATASET_NAME;
    }

    @Override
    public String getCollectionName( ){
        return COLLECTION_NAME;
    }

    @Override
    public String getExternalName( ){
        return EXTERNAL_NAME;
    }

    @Override
    public String getSpecificationName( ){
        return SPECIFICATION_NAME;
    }

    @Override
    public Map< String, String > fieldsAndValues( ){
        Map< String, String > mapping = new HashMap<>(  );
        mapping.put( SmallworldFieldMetadata.NETWORK_FIELD_NAME, network );
        mapping.put( SmallworldFieldMetadata.STATE_FIELD_NAME, state );
        mapping.put( SmallworldFieldMetadata.TEE_DIRECTION_FIELD_NAME, teeDirection );
        return mapping;
    }

    public BitmapDescriptor getIco( ){
        // first check that this object is selected
        if( GlobalHandler.isObjectSelected( this ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.tee_selected );
        }
        return BitmapDescriptorFactory.fromResource( R.drawable.tee );
    }
}
