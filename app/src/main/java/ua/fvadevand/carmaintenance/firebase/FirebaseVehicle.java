package ua.fvadevand.carmaintenance.firebase;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import ua.fvadevand.carmaintenance.firebase.model.Vehicle;

public class FirebaseVehicle {

    private FirebaseVehicle() {
    }

    public static DatabaseReference getCurrentVehicleRef(String vehicleId) {
        return getVehicleRef().child(vehicleId);
    }

    public static void setVehicle(Vehicle vehicle, Uri photoUri) {
        DatabaseReference vehicleRef = getVehicleRef();
        String vehicleId = vehicle.getId();
        if (TextUtils.isEmpty(vehicleId)) {
            vehicleId = vehicleRef.push().getKey();
            vehicle.setId(vehicleId);
        }

        if (photoUri != null) {
            uploadPhotoVehicle(photoUri, vehicleId);
            vehicle.setPhotoTimestamp(Calendar.getInstance().getTimeInMillis());
        }
        vehicleRef.child(vehicleId).setValue(vehicle);
    }

    public static void deleteVehicle(String vehicleId) {
        DatabaseReference vehicleRef = getVehicleRef();
        vehicleRef.child(vehicleId).removeValue();
    }

    public static StorageReference getCurrentPhotoVehicleRef(Vehicle vehicle) {
        return getPhotoVehicleRef().child(vehicle.getId());
    }

    private static void uploadPhotoVehicle(Uri photoUri, String vehicleId) {
        getPhotoVehicleRef().child(vehicleId).putFile(photoUri);
    }

    public static DatabaseReference getVehicleRef() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(Firebase.getUid())
                .child(Firebase.VEHICLE_REF);
    }

    private static StorageReference getPhotoVehicleRef() {
        return FirebaseStorage.getInstance()
                .getReference()
                .child(Firebase.getUid())
                .child(Firebase.VEHICLE_PHOTO_REF);
    }
}
