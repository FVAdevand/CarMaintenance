package ua.fvadevand.carmaintenance.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Retrofit;
import ua.fvadevand.carmaintenance.interfaces.PrivatbankApi;

public class CarMaintenance extends Application {

    private static PrivatbankApi sPrivatbankApi;

    public static PrivatbankApi getPrivatbankApi() {
        return sPrivatbankApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.privatbank.ua/")
                .build();

        sPrivatbankApi = retrofit.create(PrivatbankApi.class);
    }
}
