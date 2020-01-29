package rvo.mobilegateway;

/**
 * Created by Robert on 8/12/2015.
 */
public final class Constants {

    public static final Constants instance = new Constants();

    public String[] collectionNames = {
            "mit_hub",
            "uub",
            "pole",
            "underground_route",
            "aerial_route",
            "access_point",
            "mit_terminal_enclosure",
            "building"
    };

    public String serverPort = "";
    public String serverUrl = "";

    public double defaultLatitude = 45.64759563125122;
    public double defaultLongitute = 25.017370842397213;
    public float defaultZoom = 7;
}
