package com.rwee.mobilegatewaywo.engines;


import android.util.Xml;

import com.rwee.mobilegatewaywo.L;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Robert on 8/5/2015.
 */
public class XMLParser {

    public XMLParser() {

    }

    public List<HashMap<String, String>> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(in, null);
            xmlPullParser.nextTag();
            String name = xmlPullParser.getName();
            if(name.equals("return") | name.equals("LandXML")){
                return readTag(xmlPullParser, name);
            }

        } finally {
            in.close();
        }
        return new ArrayList<>();
    }


    private List<HashMap<String, String>> readTag(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            if (name.equals("service_response") | name.equals("CgPoints")) {
                return readServiceResponse(xmlPullParser, "service_response");
                // add handle to error message: error tag!
            } else {
                skip(xmlPullParser);
            }
        }
        return null;
    }

    private List<HashMap<String, String>> readServiceResponse(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if (name.equals("response")) {
                return readRecordsUrns(xmlPullParser, name);

                //  }else if( name.equals("response")) {
                //      return readResponse( xmlPullParser, name );
            } else {
                skip(xmlPullParser);
            }
        }
        return null;
    }

    private List<HashMap<String, String>> readResponse(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if (name.equals("hash")) {
                return readHash(xmlPullParser, "hash");
            } else {
                skip(xmlPullParser);
            }
        }
        return null;
    }

    private List<HashMap<String, String>> readHash(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if (name.equals("element")) {
                String key = xmlPullParser.getAttributeValue(null, "key");
                xmlPullParser.next();
                String value = xmlPullParser.getText();
                HashMap<String, String> keyValues = new HashMap<>();
                if (value == null) {
                    keyValues.put(key, "");
                } else {
                    keyValues.put(key, value);
                }

                map.add(keyValues);
                return map;
            } else if (name.equals("CgPoint")) {
                    /* if ( name.equals("existing") ){
                    if(name.equals("<"))
                    xmlPullParser.next();
                    }
                    */

                    String key = xmlPullParser.getAttributeValue(null, "name");
                    L.m( "value for name: " + key );
                    xmlPullParser.next();
                    String value = xmlPullParser.getText();
                    HashMap<String, String> keyValuesCoord = new HashMap<>();
                    if (value == null) {
                        keyValuesCoord.put(key, "");
                    } else {
                        keyValuesCoord.put(key, value);
                    }
            }
            skip(xmlPullParser);

        }
        return map;
    }

    private List<HashMap<String, String>> readRecordsUrns(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if (name.equals("collection")) {
                return readCollection(xmlPullParser, "collection");
            } else {
                skip(xmlPullParser);
            }
        }
        return map;
    }

    private List<HashMap<String, String>> readCollection(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();

            if (name.equals("element")) {
                map.add(readElement(xmlPullParser, "element"));
            } else {
                skip(xmlPullParser);
            }
        }
        return map;
    }


    private HashMap<String, String> readElement(XmlPullParser xmlPullParser, String xmlName) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, xmlPullParser.getNamespace(), xmlName);
        HashMap<String, String> keyValues = new HashMap<>();
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlPullParser.getName();
            if (name.equals("element")) {
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

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
