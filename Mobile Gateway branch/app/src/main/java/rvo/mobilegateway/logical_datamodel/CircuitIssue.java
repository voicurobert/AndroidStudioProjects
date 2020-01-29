package rvo.mobilegateway.logical_datamodel;

/**
 * Created by Robert on 10/13/2015.
 */
public class CircuitIssue extends LogicalObject {
    public static final String COLLECTION_NAME = "cit_circuit_issue";
    public String name;
    public String transportLevel;
    public String serviceType;
    public Object owner;

    @Override
    public String getCollectionName( ) {
        return COLLECTION_NAME;
    }

    @Override
    public String toString( ) {
        return "[CI] " + name;
    }
}
