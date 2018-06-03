package ua.fvadevand.carmaintenance.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAdditionalInfo {

    private static final String LOG_TAG = FirebaseAdditionalInfo.class.getSimpleName();

    private FirebaseAdditionalInfo() {
    }

    public static void setGasStation(String gasStationName) {
        getAdditionalInfoRef().child(Firebase.INFO_GAS_STATION_REF)
                .setValue(gasStationName);
    }

    public static void setFuelBrand(String fuelBrand) {
        getAdditionalInfoRef().child(Firebase.INFO_FUEL_BRAND_REF)
                .setValue(fuelBrand);
    }

    private static DatabaseReference getAdditionalInfoRef() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.getUid())
                .child(Firebase.ADDITIONAL_INFO_REF);
    }
}
