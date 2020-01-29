package rvo.mobilegateway.bookmarks_activity;

/**
 * Created by Robert on 9/17/2015.
 */
public class Bookmark {

    public String bookmarkName;
    public float longitude;
    public float latitude;
    public float zoom;
    public float bearing;

    public void Bookmark( ){

    }

    @Override
    public boolean equals( Object o ){
        if( this.bookmarkName.matches( ( ( Bookmark )o).bookmarkName ) &&
                this.latitude == ( ( Bookmark )o ).latitude &&
                this.longitude == ( ( Bookmark )o ).longitude ){
            return true;
        }else{
            return false;
        }
    }
}
