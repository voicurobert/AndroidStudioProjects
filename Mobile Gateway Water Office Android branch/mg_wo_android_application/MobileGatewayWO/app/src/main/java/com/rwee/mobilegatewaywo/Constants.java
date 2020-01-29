package com.rwee.mobilegatewaywo;

import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDConnectionPipe;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDManhole;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDServiceConnection;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDSewerSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSHydrant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSMainSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSServicePoint;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSStation;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTank;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTee;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTreatmentPlant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSValve;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 8/12/2015.
 */
public final class Constants {

    public static final Constants instance = new Constants();
    public String WS_DATASET_NAME = "water_supply";
    public String WD_DATASET_NAME = "drainage";

    public Map< String, String > collectionMapping = new HashMap<>(  );

    public String[] collectionNames = {
            WDConnectionPipe.COLLECTION_NAME,
            WDManhole.COLLECTION_NAME,
            WDServiceConnection.COLLECTION_NAME,
            WDSewerSection.COLLECTION_NAME,
            WSHydrant.COLLECTION_NAME,
            WSMainSection.COLLECTION_NAME,
            WSServicePoint.COLLECTION_NAME,
            WSStation.COLLECTION_NAME,
            WSTank.COLLECTION_NAME,
            WSTee.COLLECTION_NAME,
            WSTreatmentPlant.COLLECTION_NAME,
            WSValve.COLLECTION_NAME
    };

    public Constants(){
        collectionMapping.put(  WDConnectionPipe.COLLECTION_NAME, WDConnectionPipe.DATASET_NAME );
        collectionMapping.put( WDManhole.COLLECTION_NAME, WDManhole.DATASET_NAME  );
        collectionMapping.put( WDServiceConnection.COLLECTION_NAME , WDServiceConnection.DATASET_NAME );
        collectionMapping.put(  WDSewerSection.COLLECTION_NAME, WDSewerSection.DATASET_NAME );
        collectionMapping.put(  WSHydrant.COLLECTION_NAME, WSHydrant.DATASET_NAME );
        collectionMapping.put(  WSMainSection.COLLECTION_NAME, WSMainSection.DATASET_NAME );
        collectionMapping.put( WSServicePoint.COLLECTION_NAME, WSServicePoint.DATASET_NAME );
        collectionMapping.put( WSStation.COLLECTION_NAME, WSStation.DATASET_NAME );
        collectionMapping.put( WSTank.COLLECTION_NAME, WSTank.DATASET_NAME );
        collectionMapping.put( WSTee.COLLECTION_NAME, WSTee.DATASET_NAME );
        collectionMapping.put( WSTreatmentPlant.COLLECTION_NAME, WSTreatmentPlant.DATASET_NAME );
        collectionMapping.put( WSValve.COLLECTION_NAME, WSValve.DATASET_NAME );

    }

    public String serverPort = "";
    public String serverUrl = "";

    // Ro - Centre
   // public double defaultLatitude = 45.64759563125122;
   // public double defaultLongitute = 25.017370842397213;

    // WO Demo - ratingen
    public double defaultLatitude = 51.299277;
    public double defaultLongitute = 6.844225;
    public float defaultZoom = 18;
}
