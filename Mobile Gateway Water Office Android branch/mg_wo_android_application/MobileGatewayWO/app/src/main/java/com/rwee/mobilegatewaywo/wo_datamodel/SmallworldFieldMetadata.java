package com.rwee.mobilegatewaywo.wo_datamodel;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by Robert on 27.11.2015.
 */
public class SmallworldFieldMetadata{

    public final Map< String, String > FIELD_MAPPING = new HashMap<>(  );
    public Map< String, Boolean > FIELD_ENUMERATION_MAPPING = new HashMap<>(  );

    public static SmallworldFieldMetadata instance = new SmallworldFieldMetadata();

    public static final String CONNECTION_TYPE_FIELD_NAME = "Connection Type";
    public static final String WATER_TYPE_FIELD_NAME = "Water Type";
    public static final String CALCULATED_PIPE_LENGTH_FIELD_NAME = "Pipe length [m]";
    public static final String START_PIPE_BASE_LEVEL_FIELD_NAME = "Start pipe base lvl [m]";
    public static final String END_PIPE_BASE_LEVEL_FIELD_NAME = "End pipe base lvl [m]";
    public static final String CONSTRUCTION_STATUS_FIELD_NAME = "Construction status";
    public static final String COVER_LEVEL_FIELD_NAME = "Cover level [m]";
    public static final String BASE_HEIGHT_FIELD_NAME = "Base height [m]";
    public static final String DEPTH_FIELD_NAME = "Depth [m]";
    //public static final String MANHOLE_INSPECTION_FIELD_NAME = "Manhole inspection";
    public static final String CONNECTOR_HEIGHT_FIELD_NAME = "Connector height [m]";
    public static final String PROPERTY_FIELD_NAME = "Property";
    public static final String SITUATION_FIELD_NAME = "Situation";
    public static final String BASE_HEIGHT_START_FIELD_NAME = "Base height start [m]";
    public static final String BASE_HEIGHT_END_FIELD_NAME = "Base height end [m]";
    public static final String CROSS_SECTIONAL_AREA_FIELD_NAME = "Cross sectional area [m*m]";
    public static final String SEWER_SECTION_LENGTH_FIELD_NAME = "Sewer section length [m]";
    public static final String NETWORK_FIELD_NAME = "Network";
    public static final String STATE_FIELD_NAME = "State";
    public static final String PERFORMANCE_FIELD_NAME = "Performance";
    public static final String RELATION_TO_MAIN_FIELD_NAME = "Relation to main";
    public static final String LENGTH_FIELD_NAME = "Length [m]";
    public static final String NAME_FIELD_NAME = "Name";
    public static final String CAPACITY_FIELD_NAME = "Capacity";
    public static final String USED_CAPACITY_FIELD_NAME = "Used capacity [m*m*m]";
    public static final String TEE_DIRECTION_FIELD_NAME = "Tee direction";
    public static final String WORKS_NAME_FIELD_NAME = "Works name";
    public static final String TORQUE_FIELD_NAME = "Torque ";
    public static final String MANHOLE_NUMBER_FIELD_NAME = "Manhole number";



    private SmallworldFieldMetadata(){
        computeFieldMapping();
    }

    private void computeFieldMapping(){
        FIELD_MAPPING.put( CONNECTION_TYPE_FIELD_NAME, "connection_type" );
        FIELD_MAPPING.put( WATER_TYPE_FIELD_NAME, "water_type" );
        FIELD_MAPPING.put( CONSTRUCTION_STATUS_FIELD_NAME, "construction_status" );
        FIELD_MAPPING.put( CALCULATED_PIPE_LENGTH_FIELD_NAME, "calculated_pipe_length" );
        FIELD_MAPPING.put( START_PIPE_BASE_LEVEL_FIELD_NAME, "start_pipe_base_level" );
        FIELD_MAPPING.put( END_PIPE_BASE_LEVEL_FIELD_NAME, "end_pipe_base_level" );
        FIELD_MAPPING.put( COVER_LEVEL_FIELD_NAME, "cover_level" );
        FIELD_MAPPING.put( BASE_HEIGHT_FIELD_NAME, "base_height" );
        FIELD_MAPPING.put( DEPTH_FIELD_NAME, "depth" );
       // FIELD_MAPPING.put( MANHOLE_INSPECTION_FIELD_NAME, "manhole_inspection" );
        FIELD_MAPPING.put( CONNECTOR_HEIGHT_FIELD_NAME, "connector_height" );
        FIELD_MAPPING.put( PROPERTY_FIELD_NAME, "property" );
        FIELD_MAPPING.put( SITUATION_FIELD_NAME, "situation" );
        FIELD_MAPPING.put( BASE_HEIGHT_START_FIELD_NAME, "base_height_start" );
        FIELD_MAPPING.put( BASE_HEIGHT_END_FIELD_NAME, "base_height_end" );
        FIELD_MAPPING.put( CROSS_SECTIONAL_AREA_FIELD_NAME, "cross_sectional_area" );
        FIELD_MAPPING.put( SEWER_SECTION_LENGTH_FIELD_NAME, "sewer_section_calc_length" );
        FIELD_MAPPING.put( NETWORK_FIELD_NAME, "network" );
        FIELD_MAPPING.put( STATE_FIELD_NAME, "state" );
        FIELD_MAPPING.put( PERFORMANCE_FIELD_NAME, "performance" );
        FIELD_MAPPING.put( RELATION_TO_MAIN_FIELD_NAME, "relation_to_main" );
        FIELD_MAPPING.put( LENGTH_FIELD_NAME, "length" );
        FIELD_MAPPING.put( CAPACITY_FIELD_NAME, "capacity" );
        FIELD_MAPPING.put( USED_CAPACITY_FIELD_NAME, "used_capacity" );
        FIELD_MAPPING.put( TEE_DIRECTION_FIELD_NAME, "tee_direction" );
        FIELD_MAPPING.put( WORKS_NAME_FIELD_NAME, "works_name" );
        FIELD_MAPPING.put( TORQUE_FIELD_NAME, "torque" );
        FIELD_MAPPING.put( NAME_FIELD_NAME, "name" );
        FIELD_MAPPING.put( MANHOLE_NUMBER_FIELD_NAME, "manhole_number" );

        FIELD_ENUMERATION_MAPPING.put( CONNECTION_TYPE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( WATER_TYPE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( CONSTRUCTION_STATUS_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( CALCULATED_PIPE_LENGTH_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( START_PIPE_BASE_LEVEL_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( END_PIPE_BASE_LEVEL_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( COVER_LEVEL_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( BASE_HEIGHT_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( DEPTH_FIELD_NAME, false );
      //  FIELD_ENUMERATION_MAPPING.put( MANHOLE_INSPECTION_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( CONNECTOR_HEIGHT_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( PROPERTY_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( SITUATION_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( BASE_HEIGHT_START_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( BASE_HEIGHT_END_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( CROSS_SECTIONAL_AREA_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( SEWER_SECTION_LENGTH_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( NETWORK_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( STATE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( PERFORMANCE_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( RELATION_TO_MAIN_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( LENGTH_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( CAPACITY_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( USED_CAPACITY_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( TEE_DIRECTION_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( WORKS_NAME_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( TORQUE_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( NAME_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( MANHOLE_NUMBER_FIELD_NAME, false );

    }


}
