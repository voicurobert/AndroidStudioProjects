package com.rwee.mobilegatewaywo;


import com.google.android.gms.maps.model.Marker;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
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

/**
 * Created by Robert on 8/24/2015.
 */
public class GlobalHandler {
    public static boolean isObjectSelected = false;
    public static AbstractWaterOfficeObject selectedObject = null;
    public static int recordId;
    public static Marker markerToInsert;



    public static void setSelectedObject( AbstractWaterOfficeObject o ){
        // deselect current object
        AbstractWaterOfficeObject oldSelection = selectedObject;
        selectedObject = o;
        setSelection( oldSelection );
        recordId = o.id;
        setSelection( o );
        isObjectSelected = true;
    }

    /*
    public static String createTitle(){
        String name = null;
        if( GlobalHandler.uub != null ){
            name = uub.toString();
        }else if( hub != null ){
            name = Hub.EXTERNAL_NAME;
            name = name + " - ";
            String recordName = hub.name;
            if( recordName != null){
                name = name + recordName;
            }else{
                name = name + hub.type;
            }
        }else if( building != null ){
            name = Building.EXTERNAL_NAME;
            name = name + " - ";
            String recordName = building.name;
            if( recordName != null){
                name = name + recordName;
            }else{
                name = name + building.oroAlias;
            }
        }else if( terminalEnclosure != null ){
            name = TerminalEnclosure.EXTERNAL_NAME;
            name = name + " - ";
            String recordName = terminalEnclosure.name;
            if( recordName != null){
                name = name + recordName;
            }else{
                name = name + terminalEnclosure.oroAlias;
            }
        }else if( pole != null ){
            name = Pole.EXTERNAL_NAME;
            name = name + " - ";
            String recordName = pole.name;
            if( recordName != null){
                name = name + recordName;
            }else{
                name = name + pole.usage;
            }
        }else if( accessPoint != null ){
            name = AccessPoint.EXTERNAL_NAME;
            name = name + " - ";
            String recordName = accessPoint.type;
            if( recordName != null){
                name = name + recordName;
            }
        }
        return name;
    }
    */

    /*
    public static List<Object> getSelectedObjectInfo(){
        List< Object > info = new ArrayList<>(  );
        if( GlobalHandler.uub != null ){
            info.add( UndergroundUtilityBox.COLLECTION_NAME );
            info.add( uub.id );
        }else if( hub != null ){
            info.add( Hub.COLLECTION_NAME );
            info.add( hub.id );
        }else if( building != null ){
            info.add( Building.COLLECTION_NAME );
            info.add( building.id );
        }else if( terminalEnclosure != null ){
            info.add( TerminalEnclosure.COLLECTION_NAME );
            info.add( terminalEnclosure.id );
        }else if( pole != null ){
            info.add( Pole.COLLECTION_NAME );
            info.add( pole.id );
        }else if( accessPoint != null ){
            info.add( AccessPoint.COLLECTION_NAME );
            info.add( accessPoint.id );
        }
        return info;
    }
    */
    public static int getRecordId(){
        return recordId;
    }

    public static boolean isObjectSelected( AbstractWaterOfficeObject o ){
        if( selectedObject != null && selectedObject.equals( o ) ){
            return true;
        }
        return false;
    }

    public static void setSelection( AbstractWaterOfficeObject o ){
        if( o == null ){
            return;
        }
        switch( o.getExternalName( ) ){
            case WDConnectionPipe.EXTERNAL_NAME :
                ( (WDConnectionPipe) o ).woRoute.line.setColor( ( ( WDConnectionPipe ) o ).getColorId( ) );
                break;
            case WDManhole.EXTERNAL_NAME :
                ( (WDManhole) o ).woLocation.marker.setIcon( ( ( WDManhole ) o ).getIco( ) );
                break;
            case WDServiceConnection.EXTERNAL_NAME :
                ( (WDServiceConnection) o ).woLocation.marker.setIcon( ( ( WDServiceConnection ) o ).getIco( ) );
                break;
            case WDSewerSection.EXTERNAL_NAME :
                ( (WDSewerSection) o ).woRoute.line.setColor( ( ( WDSewerSection ) o ).getColorId( ) );
                break;
            case WSHydrant.EXTERNAL_NAME :
                ( (WSHydrant) o ).woLocation.marker.setIcon( ( ( WSHydrant ) o ).getIco( ) );
                break;
            case WSMainSection.EXTERNAL_NAME :
                ( (WSMainSection) o ).woRoute.line.setColor( ( ( WSMainSection ) o ).getColorId( ) );
                break;
            case WSServicePoint.EXTERNAL_NAME :
                ( (WSServicePoint) o ).woLocation.marker.setIcon( ( ( WSServicePoint ) o ).getIco( ) );
                break;
            case WSStation.EXTERNAL_NAME :
                ( (WSStation) o ).woExtent.polygon.setStrokeColor( ( ( WSStation ) o ).getColorId( ) );
                break;
            case WSTank.EXTERNAL_NAME :
                ( (WSTank) o ).woExtent.polygon.setStrokeColor( ( ( WSTank ) o ).getColorId( ) );
                break;
            case WSTee.EXTERNAL_NAME :
                ( (WSTee) o ).woLocation.marker.setIcon( ( ( WSTee ) o ).getIco( ) );
                break;
            case WSTreatmentPlant.EXTERNAL_NAME :
                ( (WSTreatmentPlant) o ).woExtent.polygon.setStrokeColor( ( ( WSTreatmentPlant ) o ).getColorId( ) );
                break;
            case WSValve.EXTERNAL_NAME :
                ( (WSValve) o ).woLocation.marker.setIcon( ( ( WSValve ) o ).getIco( ) );
                break;
        }
    }

    public static void deselectObject(  ){
        AbstractWaterOfficeObject waterOfficeObject = selectedObject;
        resetSLots();
        isObjectSelected = false;
        setSelection( waterOfficeObject );
    }

    public static void resetSLots(){
        recordId = 0;
        selectedObject = null;
    }


}
