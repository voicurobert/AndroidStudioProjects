package rvo.mobilegateway.engines;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rvo.mobilegateway.L;


/**
 * Created by Robert on 8/5/2015.
 */
public class XMLParser {

    public XMLParser(){

    }

    public List<HashMap<String, String>> parse( InputStream in ) throws XmlPullParserException, IOException {
        try{
            XmlPullParser xmlPullParser = Xml.newPullParser();
            List<HashMap<String, String>> errorResponseList = new ArrayList< HashMap<String, String>>(  );
            xmlPullParser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );
            xmlPullParser.setInput(in, null );
            xmlPullParser.nextTag();
            String tagName = xmlPullParser.getName();
            if( tagName.equals( "return" ) ){
                return readTag(xmlPullParser, "return");
            }else if( tagName.equals( "root" ) ){
                HashMap< String, String > response = new HashMap<>(  );
                response.put( "response_error", "Connector error [ request failed in EIS ]" );
                errorResponseList.add( response );
                return errorResponseList;
            }
        }finally {
            in.close();
        }
       return new ArrayList< HashMap<String, String> >(  );
    }



    private List<HashMap<String, String>>  readTag( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        xmlPullParser.require( XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName );
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "service_response" ) ) {
                return readServiceResponse(xmlPullParser, "service_response");
            } else {
                skip(xmlPullParser);
            }

        }
        return map;
    }

    private List<HashMap<String, String>> readServiceResponse( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "response" ) ){
                return readRecordsUrns( xmlPullParser, name );
            }else if( name.equals( "image_layers" )){
                return readImageLayers( xmlPullParser, name );

            } else {
                skip(xmlPullParser);
            }
        }
        return null;
    }

    private List<HashMap<String, String>> readImageLayers( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        while (xmlPullParser.next( ) != XmlPullParser.END_TAG){
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            if( name.equals( "map_layer_response" )){
                return readMapLayerResponse( xmlPullParser, name );
            }
        }
        return map;
    }


    private List<HashMap<String, String>> readMapLayerResponse( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        String value = "";
        while (xmlPullParser.next( ) != XmlPullParser.END_TAG){
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            if( name.equals( "urls" )){

                while( xmlPullParser.next( ) != XmlPullParser.END_TAG ){
                    if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String elName = xmlPullParser.getName();
                    if( elName.equals( "element" )){
                        xmlPullParser.next();
                        value = xmlPullParser.getText();
                        L.m( "value" + value );
                        HashMap< String, String > map1 = new HashMap<>(  );
                        map1.put( "image_url", value );
                        map.add( map1 );
                        return map;
                    }
                }
            }else{
                skip( xmlPullParser );
            }
        }
        return map;
    }

    private List<HashMap<String, String>> readResponse( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "hash" ) ) {
                return readHash( xmlPullParser, "hash");
            } else {
                skip(xmlPullParser);
            }
        }
        return null;
    }

    private List<HashMap<String, String>> readHash( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "element" ) ) {
                String key = xmlPullParser.getAttributeValue(null, "key");
                xmlPullParser.next();
                String value = xmlPullParser.getText();
                HashMap<String, String > keyValues = new HashMap<>();
                if( value == null ){
                    keyValues.put(key, "");
                }else{
                    keyValues.put(key, value);
                }

                map.add( keyValues );
                return  map;
            } else {
                skip(xmlPullParser);
            }
        }
        return map;
    }

    private List<HashMap<String, String>> readRecordsUrns( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "collection" ) ) {
                return readCollection( xmlPullParser, "collection" );
            } else {
                skip(xmlPullParser);
            }
        }
        return map;
    }

    private List<HashMap<String, String>> readCollection( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String> >();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if ( name.equals( "element" ) ) {
                map.add( readElement(xmlPullParser, "element") );
            } else {
                skip(xmlPullParser);
            }
        }
        return map;
    }


    private HashMap<String, String > readElement( XmlPullParser xmlPullParser, String xmlName ) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        HashMap<String, String > keyValues = new HashMap<>();
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            if ( name.equals( "element" ) ) {
                String key = xmlPullParser.getAttributeValue(null, "key");
                xmlPullParser.next();
                String value = xmlPullParser.getText();
                keyValues.put(key, value);
                xmlPullParser.next();
            } else {
                continue;
            }
        }
        return keyValues;
    }

    private void skip( XmlPullParser parser ) throws XmlPullParserException, IOException{
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
