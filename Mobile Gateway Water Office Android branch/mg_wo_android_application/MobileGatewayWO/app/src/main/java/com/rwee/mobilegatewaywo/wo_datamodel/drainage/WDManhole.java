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
public class WDManhole extends AbstractWaterOfficeObject{
    public static final String COLLECTION_NAME = "wd_manhole";
    public static final String EXTERNAL_NAME = "Manhole";
    public static final String DATASET_NAME = "drainage";
    public static final String SPECIFICATION_NAME = "wd_manhole_spec";

    // fields
    public String waterType = "";
    public String constructionStatus = "";
    public String coverLevel = "";
    public String baseHeight = "";
    public String depth = "";
    public String manholeNumber = "";
    public WOLocation woLocation;

    public WDManhole(){

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
        mapping.put( SmallworldFieldMetadata.WATER_TYPE_FIELD_NAME, waterType );
        mapping.put( SmallworldFieldMetadata.CONSTRUCTION_STATUS_FIELD_NAME, constructionStatus );
        mapping.put( SmallworldFieldMetadata.COVER_LEVEL_FIELD_NAME, coverLevel );
        mapping.put( SmallworldFieldMetadata.BASE_HEIGHT_FIELD_NAME, baseHeight );
      //  mapping.put( SmallworldFieldMetadata.MANHOLE_INSPECTION_FIELD_NAME, manholeInspection );
        mapping.put( SmallworldFieldMetadata.MANHOLE_NUMBER_FIELD_NAME, manholeNumber );
        return mapping;
    }

    public BitmapDescriptor getIco( ){
        // first check that this object is selected
        if( GlobalHandler.isObjectSelected( this ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.manhole_selected );
        }
        return BitmapDescriptorFactory.fromResource( R.drawable.manhole );
    }
}
