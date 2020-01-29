package rvo.mobilegateway.bookmarks_activity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Robert on 9/17/2015.
 */
public final class BookmarkEngine {

    public static BookmarkEngine instance = new BookmarkEngine();
    public final String SHARED_PREFERENCES_NAME = "BOOKMARKS";
    private Context context;
    public List< Bookmark > bookmarkList = new ArrayList<>(  );
    public GoogleMap map;

    private BookmarkEngine(){

    }

    public void setContext( Context context ){
        this.context = context;
        this.resetBookmarkList();
    }

    public Context getContext( ){
        return context;
    }

    public void resetBookmarkList(){
        // get all bookmarks from shared prefs
        SharedPreferences sharedPrefs = this.context.getSharedPreferences( SHARED_PREFERENCES_NAME, 0 );
        Set< String > bookmarkKeys = sharedPrefs.getStringSet( "BOOKMARK_NAMES", null );
        // for every bookmarkKey in bookmarkKeys get shared preferences with name bookmark key name
        if( bookmarkKeys != null ){
            for( String bookmarkKeyName : bookmarkKeys ){
                SharedPreferences bookmarkSP = this.context.getSharedPreferences( bookmarkKeyName, 0 );
                Bookmark bookmark = new Bookmark();
                bookmark.bookmarkName = bookmarkKeyName;
                bookmark.latitude = bookmarkSP.getFloat( "latitude", 0 );
                bookmark.longitude = bookmarkSP.getFloat( "longitude", 0 );
                bookmark.zoom = bookmarkSP.getFloat( "zoom", 0 );
                bookmark.bearing = bookmarkSP.getFloat( "bearing", 0 );
                if( !bookmarkList.contains( bookmark ) ){
                    bookmarkList.add( bookmark );
                }

            }
        }
    }

    public void addBookmarkToSharedPrefs( String bookmarkName, float latitude, float longitude, float zoom, float bearing ){
        // get BookmarkEngine.SHARED_PREFERENCES_NAME
        SharedPreferences sharedPrefs = this.context.getSharedPreferences( SHARED_PREFERENCES_NAME, 0 );
        // add bookmarkName as a value to BOOKMARK_NAMES value
        SharedPreferences.Editor ed = sharedPrefs.edit( );
        // check for bookmark names set, if it doesn't exist add a new set to ed
        Set< String > bookmarkNamesSet = sharedPrefs.getStringSet( "BOOKMARK_NAMES", new LinkedHashSet< String >( ) );

        bookmarkNamesSet.add( bookmarkName );
        ed.putStringSet( "BOOKMARK_NAMES", bookmarkNamesSet );
        ed.commit( );


        SharedPreferences bookmarksSP = this.context.getSharedPreferences( bookmarkName, 0 );
        SharedPreferences.Editor bookmarkEditor = bookmarksSP.edit( );
        bookmarkEditor.putFloat( "latitude", latitude );
        bookmarkEditor.putFloat( "longitude", longitude );
        bookmarkEditor.putFloat( "zoom", zoom );
        bookmarkEditor.putFloat( "bearing", bearing );
        bookmarkEditor.commit();

    }

    public boolean bookmarkExist( String bookmarkName ){
        for( Bookmark bookmark : instance.bookmarkList ){
            if( bookmark.bookmarkName.matches( bookmarkName )){
                return true;
            }
        }
        return false;
    }

    public Bookmark getBookmarkByName( String name ){
        Bookmark bookmark = null;
        for( Bookmark b : bookmarkList ){
            if( b.bookmarkName.matches( name )){
                bookmark = b;
            }
        }
        return bookmark;
    }

    public List< String > bookmarksAsStringList(){
        List< String > bookStringList = new ArrayList<>(  );
        for( Bookmark b : bookmarkList ){
            bookStringList.add( b.bookmarkName );
        }
        return bookStringList;
    }
}
