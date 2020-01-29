package rvo.mobilegateway.bookmarks_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 9/17/2015.
 */
public class BookmarkActivity extends AppCompatActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.bookmarks_activity );
        ListView lv = ( ListView ) findViewById( R.id.listView );
        final BookmarkListAdapter listAdapter = new BookmarkListAdapter(
                BookmarkEngine.instance.bookmarksAsStringList( ),
                getApplicationContext( ),
                R.layout.bookmark_list_adapter_layout );

        lv.setAdapter( listAdapter );

        lv.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView< ? > parent, View view, int position, long id ){
                Bookmark bookmark = BookmarkEngine.instance.getBookmarkByName( listAdapter.getItem( position ) );
                MapEngine.setBookmark( MobileGatewayMapActivity.masterActivity.getMap( ), bookmark );
                finish();
            }
        } );


    }
}
