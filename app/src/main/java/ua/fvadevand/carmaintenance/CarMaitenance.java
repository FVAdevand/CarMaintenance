package ua.fvadevand.carmaintenance;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class CarMaitenance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
