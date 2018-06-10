package ua.fvadevand.carmaintenance.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.data.Fuel;

public class PrivatbankApiUtils {

    private static final String LOG_TAG = PrivatbankApiUtils.class.getSimpleName();

    private static final String KEY_TYPE = "type";
    private static final String KEY_PRICE = "price";

    private PrivatbankApiUtils() {
    }

    public static Map<String, String> getRegionMap(Context context) {
        Map<String, String> regionMap = new HashMap<>();
        String[] regionArray = context.getResources().getStringArray(R.array.codes_api_region);
        for (String region : regionArray) {
            String[] regionCodeAndName = region.split("\\|", 2);
            regionMap.put(regionCodeAndName[1], regionCodeAndName[0]);
        }
        return regionMap;
    }

    private static Map<String, String> getFuelTypeMap(Context context) {
        Map<String, String> fuelTypeMap = new HashMap<>();
        String[] fuelTypeArray = context.getResources().getStringArray(R.array.codes_api_type_fuel);
        for (String fuelType : fuelTypeArray) {
            String[] fuelArray = fuelType.split("\\|", 2);
            fuelTypeMap.put(fuelArray[0], fuelArray[1]);
        }
        return fuelTypeMap;
    }

    public static List<Fuel> deserializeFuelList(Context context, String json) {
        List<Fuel> fuelList = new ArrayList<>();
        try {
            Map<String, String> fuelTypeMap = getFuelTypeMap(context);
            JSONArray fuelListJson = new JSONArray(json);
            for (int i = 0; i < fuelListJson.length(); i++) {
                JSONObject fuelJson = fuelListJson.getJSONObject(i);
                String typeFromJson = fuelJson.getString(KEY_TYPE);
                String typeFromResources = fuelTypeMap.get(typeFromJson);
                fuelList.add(new Fuel(typeFromResources, fuelJson.getDouble(KEY_PRICE)));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "deserializeFuelList: ", e);
        }

        Collections.sort(fuelList, new Comparator<Fuel>() {
            @Override
            public int compare(Fuel o1, Fuel o2) {
                return o1.getType().compareToIgnoreCase(o2.getType());
            }
        });

        return fuelList;
    }
}