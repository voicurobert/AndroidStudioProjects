package rvo.com.book.firebase;


import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthManager {
    private FirebaseAuth firebaseAuth;

    private FirebaseAuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private static class FirebaseAuthManagerHolder {
        public static final FirebaseAuthManager instance = new FirebaseAuthManager();
    }

    public static FirebaseAuthManager getInstance() {
        return FirebaseAuthManagerHolder.instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public boolean isUserAuthenticated() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
