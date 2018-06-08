package ua.fvadevand.carmaintenance.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAdditionalInfo {

    private static final String LOG_TAG = FirebaseAdditionalInfo.class.getSimpleName();

    private FirebaseAdditionalInfo() {
    }

    public static void setGasStation(String gasStationName) {
        getGasStationRef().push().setValue(gasStationName);
    }

    public static void setFuelBrand(String fuelBrand) {
        getFuelBrandRef().push().setValue(fuelBrand);
    }

    public static DatabaseReference getGasStationRef() {
        return getAdditionalInfoRef().child(Firebase.INFO_GAS_STATION_REF);
    }

    public static DatabaseReference getFuelBrandRef() {
        return getAdditionalInfoRef().child(Firebase.INFO_FUEL_BRAND_REF);
    }

    private static DatabaseReference getAdditionalInfoRef() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.getUid())
                .child(Firebase.ADDITIONAL_INFO_REF);
    }
}
