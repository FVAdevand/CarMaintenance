package ua.fvadevand.carmaintenance.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class ShPrefManager {
    private static final String LOG_TAG = ShPrefManager.class.getSimpleName();

    private static final String SHARED_PREF_FILE_NAME = "main_sh_pref";
    private static final String KEY_CURRENT_VEHICLE_ID = "CURRENT_VEHICLE_ID";

    private ShPrefManager() {
    }

    public static void setCurrentVehicleId(Context context, String vehicleId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_CURRENT_VEHICLE_ID, vehicleId);
        editor.apply();
    }

    public static String getCurrentVehicleId(Context context) {
        SharedPreferences shPref = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return shPref.getString(KEY_CURRENT_VEHICLE_ID, null);
    }
}
