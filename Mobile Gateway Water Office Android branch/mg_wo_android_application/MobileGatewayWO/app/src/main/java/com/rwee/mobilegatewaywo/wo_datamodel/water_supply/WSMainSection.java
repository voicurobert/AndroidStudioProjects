package com.rwee.mobilegatewaywo.wo_datamodel.water_supply;

import android.graphics.Color;

import com.rwee.mobilegatewaywo.GlobalHandler;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
import com.rwee.mobilegatewaywo.wo_datamodel.SmallworldFieldMetadata;
import com.rwee.mobilegatewaywo.wo_datamodel.WORoute;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 10/02/16.
 */
public class WSMainSection extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "ws_main_section";
    public static final String EXTERNAL_NAME = "Main Section";
    public static final String DATASET_NAME = "water_supply";
    public static final String SPECIFICATION_NAME = "ws_main_section_spec";
    // fields
    public String network = "";
    public String waterType = "";
    public String length = "";
    public String state = "";
    public WORoute woRoute;

    public WSMainSection(){

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
        mapping.put( SmallworldFieldMetadata.WATER_TYPE_FIELD_NAME, waterType );
        mapping.put( SmallworldFieldMetadata.LENGTH_FIELD_NAME, length );
        mapping.put( SmallworldFieldMetadata.STATE_FIELD_NAME, state );
        return mapping;
    }


    public int getColorId(){
        if( GlobalHandler.isObjectSelected( this ) ){
            return Color.RED;
        }
        return Color.BLUE;
    }

    public int getRouteWidth(){
        return 4;
    }
}
