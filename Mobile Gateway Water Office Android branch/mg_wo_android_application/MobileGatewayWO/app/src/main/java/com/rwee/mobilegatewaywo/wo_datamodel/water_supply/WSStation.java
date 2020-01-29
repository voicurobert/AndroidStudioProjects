package com.rwee.mobilegatewaywo.wo_datamodel.water_supply;

import android.graphics.Color;

import com.rwee.mobilegatewaywo.GlobalHandler;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
import com.rwee.mobilegatewaywo.wo_datamodel.SmallworldFieldMetadata;
import com.rwee.mobilegatewaywo.wo_datamodel.WOExtent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 10/02/16.
 */
public class WSStation extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "ws_station";
    public static final String EXTERNAL_NAME = "Station";
    public static final String DATASET_NAME = "water_supply";

    // fields
    public String name = "";
    public String state = "";
    public String capacity = "";
    public WOExtent woExtent;

    public WSStation(){
        hasSpecification = false;
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
        return "";
    }

    @Override
    public Map< String, String > fieldsAndValues( ){
        Map< String, String > mapping = new HashMap<>(  );
        mapping.put( SmallworldFieldMetadata.NAME_FIELD_NAME, name );
        mapping.put( SmallworldFieldMetadata.STATE_FIELD_NAME, state );
        mapping.put( SmallworldFieldMetadata.CAPACITY_FIELD_NAME, capacity );
        return mapping;
    }

    public int getColorId(){
        if( GlobalHandler.isObjectSelected( this ) ){
            return Color.RED;
        }
        return Color.DKGRAY;
    }
}
