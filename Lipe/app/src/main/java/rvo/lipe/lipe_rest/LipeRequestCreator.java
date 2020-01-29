package rvo.lipe.lipe_rest;

/**
 * Created by Robert on 28/06/2017.
 */

public class LipeRequestCreator{
    
    private static final LipeRequestCreator instance = new LipeRequestCreator();
    // url to use from android emulator
    private static final String SERVER_URL = "http://10.0.2.2:8080/";
    // url to use from device
    //private static final String SERVER_URL = "http://localhost:8080/";
    private static final String INSERT_REQUEST = "insertUser";
    private static final String EQUAL = "=";
    private static final String AND = "&";
    private static final String QUESTION_MARK = "?";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String FACEBOOK_ID = "facebookId";
    
    static LipeRequestCreator getInstance(){
        return instance;
    }
    
    private LipeRequestCreator(){
    }
    
    public static String computeInsertRequest( String facebookId, String accessToken ){
        return SERVER_URL + INSERT_REQUEST + QUESTION_MARK + FACEBOOK_ID + EQUAL + facebookId + AND + ACCESS_TOKEN + EQUAL + accessToken;
    }
    
    
}
