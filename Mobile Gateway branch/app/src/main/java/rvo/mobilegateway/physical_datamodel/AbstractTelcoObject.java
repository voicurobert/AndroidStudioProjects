package rvo.mobilegateway.physical_datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 04/03/16.
 */
public abstract class AbstractTelcoObject extends AbstractSmallWorldObject{
    public List< Cable > passingThroughCables = null;
    public List< Cable > connectedCables = null;
    public List< EquipmentObject > internalEquipments = null;
    public List< Port > ports = null;

    public boolean addPort( Port port ){
        if( ports == null ){
            ports = new ArrayList<>(  );
        }
        if( !ports.contains( port ) ){
            ports.add( port );
            return true;
        }else{
            return false;
        }
    }
    public boolean addPassingThroughCable( Cable cable ){
        if( passingThroughCables == null ){
            passingThroughCables = new ArrayList<>(  );
        }
        if( !passingThroughCables.contains( cable ) ){
            passingThroughCables.add( cable );
            return true;
        }else{
            return false;
        }
    }

    public boolean addConnectedCable( Cable cable ){
        if( connectedCables == null ){
            connectedCables = new ArrayList<>(  );
        }
        if( !connectedCables.contains( cable ) ){
            connectedCables.add( cable );
            return true;
        }else{
            return false;
        }
    }

    public Cable getCableById( int id ){
        for( Cable cable : connectedCables ){
            if( cable.id == id ){
                return cable;
            }
        }
        return null;
    }

    public boolean addInternalEquipment( EquipmentObject internalEquipment ){
        if( internalEquipments == null ){
            internalEquipments = new ArrayList<>(  );
        }
        if( !internalEquipments.contains( internalEquipment ) ){
            internalEquipments.add( internalEquipment );
            return true;
        }else{
            return false;
        }
    }


    public EquipmentObject getInternalEquipmentById( int id, boolean lookForStrwConnectionPoint ){
        if( internalEquipments == null ){
            return null;
        }
        EquipmentObject foundEquipment = null;
        for( EquipmentObject equipment : internalEquipments ){
            if( equipment.id == id ){
                foundEquipment = equipment;
            }
        }
        if( foundEquipment != null ){
            return foundEquipment;
        }else{
            // go deeper
            if( lookForStrwConnectionPoint ){
                for( EquipmentObject equipment : internalEquipments ){
                    if( equipment.strwConnectionPointId == id ){
                        foundEquipment = equipment;
                    }
                }
            }else{
                for( EquipmentObject equipment : internalEquipments ){
                    List< EquipmentObject > deeperEquipments = equipment.internalEquipments;
                    if( deeperEquipments != null ){
                        for( EquipmentObject deeperEquipment : deeperEquipments ){
                            foundEquipment = deeperEquipment.getInternalEquipmentById( id, false );
                        }
                    }else{
                        return foundEquipment;
                    }
                }
            }
        }
        return foundEquipment;
    }
}
