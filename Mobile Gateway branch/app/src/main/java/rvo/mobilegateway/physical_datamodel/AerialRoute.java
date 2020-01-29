package rvo.mobilegateway.physical_datamodel;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.HashMap;
import java.util.Map;
import rvo.mobilegateway.SmallworldFieldMetadata;
import rvo.mobilegateway.engines.MapEngine;

public class AerialRoute extends AbstractTelcoObject implements IMapListener {

    public static final String EXTERNAL_NAME = "Aerial Route";
    public static final String COLLECTION_NAME = "aerial_route";
    public String name;
    public String oroAlias;
    public String owner;
    public String ownerAlias;
    public String length;
    public String constructionStatus = "";
	public Polyline route;
	public PolylineOptions polylineOptions = new PolylineOptions();
    public int defaultColor = Color.rgb( 70, 100, 250);
    public int selectColor = Color.RED;


	public AerialRoute(){
		hasSpec = false;
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
    public Map< String, String > fieldsAndValues( ){
        Map< String, String > mapping = new HashMap<>(  );
        mapping.put( SmallworldFieldMetadata.NAME_FIELD_NAME, name );
        mapping.put( SmallworldFieldMetadata.ORO_ALIAS_FIELD_NAME, oroAlias );
        mapping.put( SmallworldFieldMetadata.OWNER_FIELD_NAME, owner );
        mapping.put( SmallworldFieldMetadata.ORO_OWNER_ALIAS_FIELD_NAME, ownerAlias );
        mapping.put( SmallworldFieldMetadata.LENGTH_FIELD_NAME, length );
        mapping.put( SmallworldFieldMetadata.CONSTRUCTION_STATUS_FIELD_NAME, constructionStatus );
        return mapping;
    }

    @Override
    public String getSpecificationName( ){
        return null;
    }

    @Override
    public LatLng getLocation( ){
        int halfSize = polylineOptions.getPoints().size() / 2;
        if( ( halfSize % 2 ) != 0 ){
            return polylineOptions.getPoints().get( halfSize - 1 );
        }else{
            return polylineOptions.getPoints().get( halfSize - 2 );
        }
    }

    @Override
    public BitmapDescriptor getIco( ){
        return null;
    }

    @Override
    public void prepareTasks( Context context, String collectionName, String id ){

    }

    @Override
    public int getColorId(){
        if( MapEngine.isObjectSelected( this ) ){
            return Color.RED;
        }
        return defaultColor;
    }

    @Override
    public int getRouteWidth(){
        return 2;
    }
}
