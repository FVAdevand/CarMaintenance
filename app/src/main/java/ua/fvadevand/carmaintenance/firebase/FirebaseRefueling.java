package ua.fvadevand.carmaintenance.firebase;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ua.fvadevand.carmaintenance.firebase.model.Refueling;

public class FirebaseRefueling {

    private static final String LOG_TAG = FirebaseRefueling.class.getSimpleName();

    private FirebaseRefueling() {
    }

    public static void setRefueling(Refueling refueling, String currentVehicleId) {
        DatabaseReference curVehRefuelingRef = getCurVehRefuelingRef(currentVehicleId);
        String refuelingId = refueling.getId();
        if (TextUtils.isEmpty(refuelingId)) {
            refuelingId = curVehRefuelingRef.push().getKey();
            refueling.setId(refuelingId);
        }

        curVehRefuelingRef.child(refuelingId).setValue(refueling);
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
