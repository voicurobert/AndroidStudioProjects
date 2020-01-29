package rvo.lipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 27/06/2017.
 */

public class FacebookPermissions{
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String USER_EDUCATION_HISTORY = "user_education_history";
    private static final String USER_FRIENDS = "user_friends";
    private static final String USER_GAMES_ACTIVITY = "user_games_activity";
    private static final String USER_RELATIONSHIPS = "user_relationships";
    private static final String USER_RELATIONSHIP_DETAILS = "user_relationship_details";
    private static final String USER_WORK_HISTORY= "user_work_history";
    private static final String PAGES_SHOW_LIST = "pages_show_list";
    private static final String USER_EVENTS = "user_events";
    private static final String USER_ABOUT_ME = "user_about_me";
    private static final String BOOKS = "user_actions.books";
    private static final String MUSIC = "user_actions.music";
    private static final String VIDEO = "user_actions.video";
    private static final String NEWS = "user_actions.news";
    private static final String FITNESS = "user_actions.fitness";
    private static final String USER_BIRTHDAY = "user_birthday";
    private static final String USER_HOMETOWN = "user_hometown";
    private static final String USER_LIKES = "user_likes";
    private static final String USER_LOCATION = "user_location";
    private static final String USER_PHOTOS = "user_photos";
    private static final String USER_TAGGED_PLACES = "user_tagged_places";
    

    public static String getBOOKS(){
        return BOOKS;
    }

    public static String getMUSIC(){
        return MUSIC;
    }

    public static String getPublicProfile(){
        return PUBLIC_PROFILE;
    }

    public static String getUserAboutMe(){
        return USER_ABOUT_ME;
    }

    public static String getUserBirthday(){
        return USER_BIRTHDAY;
    }

    public static String getUserHometown(){
        return USER_HOMETOWN;
    }

    public static String getUserLikes(){
        return USER_LIKES;
    }

    public static String getUserLocation(){
        return USER_LOCATION;
    }

    public static String getUserPhotos(){
        return USER_PHOTOS;
    }

    public static String getUserTaggedPlaces(){
        return USER_TAGGED_PLACES;
    }

    public static String getVIDEO(){
        return VIDEO;
    }
    
    public static String getFITNESS(){
        return FITNESS;
    }
    
    public static String getNEWS(){
        return NEWS;
    }
    
    public static String getPagesShowList(){
        return PAGES_SHOW_LIST;
    }
    
    public static String getUserEducationHistory(){
        return USER_EDUCATION_HISTORY;
    }
    
    public static String getUserEvents(){
        return USER_EVENTS;
    }
    
    public static String getUserFriends(){
        return USER_FRIENDS;
    }
    
    public static String getUserGamesActivity(){
        return USER_GAMES_ACTIVITY;
    }
    
    public static String getUserRelationshipDetails(){
        return USER_RELATIONSHIP_DETAILS;
    }
    
    public static String getUserRelationships(){
        return USER_RELATIONSHIPS;
    }
    
    public static String getUserWorkHistory(){
        return USER_WORK_HISTORY;
    }
    
    public static List< String > getAllPermissions(){
        List< String > permissions = new ArrayList<>();
        permissions.add( getFITNESS() );
        permissions.add( getNEWS() );
        permissions.add( getPagesShowList() );
        permissions.add( getUserEvents() );
        permissions.add( getUserEducationHistory() );
        permissions.add( getUserWorkHistory() );
        permissions.add( getUserRelationships() );
        permissions.add( getUserRelationshipDetails() );
        permissions.add( getUserGamesActivity() );
        permissions.add( getUserFriends() );
        permissions.add( getBOOKS() );
        permissions.add( getMUSIC() );
        permissions.add( getPublicProfile() );
        permissions.add( getUserAboutMe() );
        permissions.add( getUserBirthday() );
        permissions.add( getUserHometown() );
        permissions.add( getUserLikes() );
        permissions.add( getUserPhotos() );
        permissions.add( getUserTaggedPlaces() );
        permissions.add( getVIDEO() );
        permissions.add( getUserLocation() );
        return permissions;
    }
}
