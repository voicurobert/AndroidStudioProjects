package com.rwee.mobilegatewaywo.wo_datamodel.drainage;

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
public class WDServiceConnection extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "wd_service_connection";
    public static final String EXTERNAL_NAME = "Service Connection";
    public static final String DATASET_NAME = "drainage";

    // fields
    public String waterType = "";
    public String connectorHeight = "";
    public String property = "";
    public WOLocation woLocation;

    public WDServiceConnection(){
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
        mapping.put( SmallworldFieldMetadata.WATER_TYPE_FIELD_NAME, waterType );
        mapping.put( SmallworldFieldMetadata.CONNECTOR_HEIGHT_FIELD_NAME, connectorHeight );
        mapping.put( SmallworldFieldMetadata.PROPERTY_FIELD_NAME, property );
        return mapping;
    }

    public BitmapDescriptor getIco( ){
        // first check that this object is selected
        if( GlobalHandler.isObjectSelected( this ) ){
            if( this.waterType.equals( "Waste" ) ){
                return BitmapDescriptorFactory.fromResource( R.drawable.service_connection_waste_selected );
            }else if( this.waterType.equals( "Rain" ) ){
                return BitmapDescriptorFactory.fromResource( R.drawable.service_connection_rain_selected );
            }
        }
        if( this.waterType.equals( "Waste" ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.service_connection_waste );
        }else if( this.waterType.equals( "Rain" ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.service_connection_rain );
        }
        // default return
        return BitmapDescriptorFactory.fromResource( R.drawable.service_connection_rain );
    }
}
