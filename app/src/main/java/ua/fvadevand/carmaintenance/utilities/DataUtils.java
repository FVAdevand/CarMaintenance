package ua.fvadevand.carmaintenance.utilities;

import java.util.ArrayList;
import java.util.List;

import ua.fvadevand.carmaintenance.firebase.model.Refueling;

public class DataUtils {
    private static final String LOG_TAG = DataUtils.class.getSimpleName();

    private DataUtils() {
    }

    public static List<Refueling> getRefuelingList() {
        List<Refueling> refuelingList = new ArrayList<>();

        return refuelingList;
    }
}
