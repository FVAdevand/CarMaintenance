package ua.fvadevand.carmaintenance.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {

    public static final String VEHICLE_REF = "vehicles";
    public static final String VEHICLE_PHOTO_REF = "vehicle_photos";
    public static final String REFUELING_REF = "refueling";

    public static final String KEY_VEHICLE_ID = "VEHICLE_ID";

    private Firebase() {
    }

    public static String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            throw new RuntimeException("don't login in firebase");
        }
    }
}
