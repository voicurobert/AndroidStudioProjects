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
public class WDConnectionPipe extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "wd_connection_pipe";
    public static final String EXTERNAL_NAME = "Connection Pipe";
    public static final String DATASET_NAME = "drainage";
    public static final String SPECIFICATION_NAME = "wd_connection_pipe_spec";
    // fields
    public String connectionType = "";
    public String waterType = "";
    public String calculatedPipeLength = "";
    public String startPipeBaseLevel = "";
    public String endPipeBaseLevel = "";
    public WORoute woRoute;

    public WDConnectionPipe(){

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
        mapping.put( SmallworldFieldMetadata.CONNECTION_TYPE_FIELD_NAME, connectionType );
        mapping.put( SmallworldFieldMetadata.WATER_TYPE_FIELD_NAME, waterType );
        mapping.put( SmallworldFieldMetadata.CALCULATED_PIPE_LENGTH_FIELD_NAME, calculatedPipeLength );
        mapping.put( SmallworldFieldMetadata.START_PIPE_BASE_LEVEL_FIELD_NAME, startPipeBaseLevel );
        mapping.put( SmallworldFieldMetadata.END_PIPE_BASE_LEVEL_FIELD_NAME, endPipeBaseLevel );
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
            return Color.BLUE;
        }
        return 0;
    }

    public int getRouteWidth(){
        if( waterType.equals( "Waste" ) ){
            return 2;
        }else if( waterType.equals( "Rain" ) ){
            return 2;
        }else if( waterType.equals( "Combined" ) ){
            return 2;
        }
        return 0;
    }
}
