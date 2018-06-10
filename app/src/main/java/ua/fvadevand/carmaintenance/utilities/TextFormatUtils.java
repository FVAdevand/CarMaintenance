package ua.fvadevand.carmaintenance.utilities;

import java.util.Locale;

public class TextFormatUtils {

    private static final String LOG_TAG = TextFormatUtils.class.getSimpleName();

    private TextFormatUtils() {
    }

    public static String costFormat(double cost) {
        String formatterStr = "%,.0f ₴";
        return String.format(Locale.getDefault(), formatterStr, cost);
    }

    public static String priceUnitFormat(double priceUnit) {
        String formatterStr = "%,.2f ₴/L";
        return String.format(Locale.getDefault(), formatterStr, priceUnit);
    }

    public static String volumeFormat(double volume) {
        String formatterStr = "%,.1f L";
        return String.format(Locale.getDefault(), formatterStr, volume);
    }

    public static String odometerFormat(int odometer) {
        String formatterStr = "%,d km";
        return String.format(Locale.getDefault(), formatterStr, odometer);
    }

    public static String distanceFormat(int distance) {
        String formatterStr = "+%,d km";
        return String.format(Locale.getDefault(), formatterStr, distance);
    }

    public static String fuelRateFormat(double fuelRate) {
        String formatterStr = "%,.1f L/100km";
        return String.format(Locale.getDefault(), formatterStr, fuelRate);
    }

    public static String decimalFormatWithDot(double numberDecimal) {
        String numberDecimalStr = String.format(Locale.getDefault(), "%.2f", numberDecimal);
        return numberDecimalStr.replace(",", ".");
    }
}
