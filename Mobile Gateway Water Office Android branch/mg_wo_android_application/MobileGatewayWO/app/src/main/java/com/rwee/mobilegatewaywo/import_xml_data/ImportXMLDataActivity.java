package com.rwee.mobilegatewaywo.import_xml_data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.engines.XMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Created by Vali on 2/24/2016.
 */
public class ImportXMLDataActivity extends AppCompatActivity {
    private Button openFileButton;
    private TextView xmlFileNameTextView;
    private InputStream inputStream;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_xml_data_layout);

        openFileButton = (Button) findViewById(R.id.openXmlButton);
        xmlFileNameTextView = (TextView) findViewById(R.id.xmlFileNameTextViewId);
        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        Button parseButton = ( Button ) findViewById( R.id.parseXmlButtonid );
        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if( inputStream == null ){
                        L.t( getApplicationContext(), "Please load a xml file to import!" );
                    }else{
                        new XMLParser().parse( inputStream );
                    }

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // add layout xml

    }

    //open file chooser
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent chooseIntent = null;
        try {
            chooseIntent = Intent.createChooser(intent, "Select a File to Upload");
            startActivityForResult(chooseIntent, FILE_SELECT_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

        L.m(chooseIntent.toString());
        //xmlFileNameTextView.setText(intent.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    L.m(uri.getPath());
                    try {
                        inputStream  = getContentResolver().openInputStream(uri);
                        String[] name = uri.getLastPathSegment().split(Pattern.quote("/"));
                        xmlFileNameTextView.setText(name[name.length - 1]);

                     /*   StringBuilder sb=new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String read;

                        while((read=br.readLine()) != null) {
                            //System.out.println(read);
                            sb.append(read);
                        }

                        br.close();
                        L.m(sb.toString());
                        // ceva = Parser.parse( is );
*/
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                       // L.t(R.);
                        e.printStackTrace();
                    }
                    L.m("File Uri: " + uri.toString());

                    // Get the path
                    String path = null;
                    try {
                        path = this.getPath(getApplicationContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    L.m("File Path: " + path);
                    // Get the file instance
                    //File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    String s = cursor.getString(column_index);
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
