package com.rwee.mobilegatewaywo.wo_datamodel.drainage;

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
public class WDSewerSection extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "wd_sewer_section";
    public static final String EXTERNAL_NAME = "Sewer Section";
    public static final String DATASET_NAME = "drainage";
    public static final String SPECIFICATION_NAME = "wd_sewer_section_spec";
    // fields
    public String waterType = "";
    public String situation = "";
    public String baseHeightStart = "";
    public String baseHeightEnd = "";
    //public String crossSectionalArea = "";
    public String sewerSectionLength = "";
    public WORoute woRoute;


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
        mapping.put( SmallworldFieldMetadata.WATER_TYPE_FIELD_NAME, waterType );
        mapping.put( SmallworldFieldMetadata. SITUATION_FIELD_NAME, situation );
        mapping.put( SmallworldFieldMetadata.BASE_HEIGHT_START_FIELD_NAME, baseHeightStart );
        mapping.put( SmallworldFieldMetadata.BASE_HEIGHT_END_FIELD_NAME, baseHeightEnd );
       // mapping.put( SmallworldFieldMetadata.CROSS_SECTIONAL_AREA_FIELD_NAME, crossSectionalArea );
        mapping.put( SmallworldFieldMetadata.SEWER_SECTION_LENGTH_FIELD_NAME, sewerSectionLength );
        return mapping;
    }

    public int getColorId(){
        // first check that this object is selected
        if( GlobalHandler.isObjectSelected( this ) ){
            return Color.RED;
        }
        if( waterType.equals( "Waste" ) ){
            return Color.rgb( 165, 42, 42 );
        }else if( waterType.equals( "Rain" )){
            return Color.BLUE;
        }else if( waterType.equals( "Combined" ) ){
            return Color.rgb( 128, 0, 128 );
        }
        return 0;
    }

    public int getRouteWidth(){
        if( waterType.equals( "Waste" ) ){
            return 2;
        }else if( waterType.equals( "Rain" ) ){
            return 2;
        }else if( waterType.equals( "Combined" )){
            return 2;
        }
        return 0;
    }
}
