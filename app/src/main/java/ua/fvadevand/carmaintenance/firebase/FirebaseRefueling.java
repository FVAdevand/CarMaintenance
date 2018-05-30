package ua.fvadevand.carmaintenance.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRefueling {

    private static final String LOG_TAG = FirebaseRefueling.class.getSimpleName();

    private FirebaseRefueling() {
    }

    public static DatabaseReference getCurVehRefuelingRef(String currentVehicleId) {
        return getRefuelingRef().child(currentVehicleId);
    }

    private static DatabaseReference getRefuelingRef() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.getUid())
                .child(Firebase.REFUELING_REF);
    }
}
