package rvo.mobilegateway;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by Robert on 27.11.2015.
 */
public class SmallworldFieldMetadata{

    public final Map< String, String > FIELD_MAPPING = new HashMap<>(  );
    public Map< String, Boolean > FIELD_ENUMERATION_MAPPING = new HashMap<>(  );

    public static SmallworldFieldMetadata instance = new SmallworldFieldMetadata();

    public static final String TYPE_FIELD_NAME = "Type";
    public static final String NAME_FIELD_NAME = "Name";
    public static final String ORO_ALIAS_FIELD_NAME = "Oro Alias";
    public static final String CONSTRUCTION_STATUS_FIELD_NAME = "Construction Status";
    public static final String OWNER_FIELD_NAME = "Owner";
    public static final String USAGE_FIELD_NAME = "Usage";
    public static final String MATERIAL_TYPE_FIELD_NAME = "Material Type";
    public static final String SPECIFICATION_FIELD_NAME = "Specification";
    public static final String ORO_OWNER_FIELD_NAME = "Oro Owner";
    public static final String ORO_OWNER_ALIAS_FIELD_NAME = "Owner Alias";
    public static final String LENGTH_FIELD_NAME = "Length [m]";

    private SmallworldFieldMetadata(){
        computeFieldMapping();
    }

    private void computeFieldMapping(){
        FIELD_MAPPING.put( NAME_FIELD_NAME, "name" );
        FIELD_MAPPING.put( ORO_ALIAS_FIELD_NAME, "oro_alias" );
        FIELD_MAPPING.put( CONSTRUCTION_STATUS_FIELD_NAME, "construction_status" );
        FIELD_MAPPING.put( SPECIFICATION_FIELD_NAME, "spec_id" );
        FIELD_MAPPING.put( OWNER_FIELD_NAME, "owner" );
        FIELD_MAPPING.put( TYPE_FIELD_NAME, "type" );
        FIELD_MAPPING.put( USAGE_FIELD_NAME, "usage" );
        FIELD_MAPPING.put( MATERIAL_TYPE_FIELD_NAME, "material_type" );
        FIELD_MAPPING.put( ORO_OWNER_ALIAS_FIELD_NAME, "oro_owner_alias" );
        FIELD_MAPPING.put( ORO_OWNER_FIELD_NAME, "oro_owner" );
        FIELD_MAPPING.put( LENGTH_FIELD_NAME, "length" );


        FIELD_ENUMERATION_MAPPING.put( TYPE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( NAME_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( ORO_ALIAS_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( CONSTRUCTION_STATUS_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( OWNER_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( USAGE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( MATERIAL_TYPE_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( SPECIFICATION_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( ORO_OWNER_FIELD_NAME, true );
        FIELD_ENUMERATION_MAPPING.put( ORO_OWNER_ALIAS_FIELD_NAME, false );
        FIELD_ENUMERATION_MAPPING.put( LENGTH_FIELD_NAME, false );
    }


}
