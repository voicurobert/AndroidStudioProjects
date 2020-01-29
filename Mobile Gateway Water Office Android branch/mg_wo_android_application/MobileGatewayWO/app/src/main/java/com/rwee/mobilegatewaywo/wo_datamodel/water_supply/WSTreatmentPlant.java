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
public class WSTreatmentPlant extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "ws_treatment_plant";
    public static final String EXTERNAL_NAME = "Treatment Plant";
    public static final String DATASET_NAME = "water_supply";
    public static final String SPECIFICATION_NAME = "ws_treatment_plant_spec";
    // fields
    public String worksName = "";
    public String state = "";
    public WOExtent woExtent;

    public WSTreatmentPlant(){

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
        mapping.put( SmallworldFieldMetadata.WORKS_NAME_FIELD_NAME, worksName );
        mapping.put( SmallworldFieldMetadata.STATE_FIELD_NAME, state );
        return mapping;
    }

    public int getColorId(){
        if( GlobalHandler.isObjectSelected( this ) ){
            return Color.RED;
        }
        return Color.BLACK;
    }
}
