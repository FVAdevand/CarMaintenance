package ua.fvadevand.carmaintenance.utilities;

import java.util.Locale;

public class TextFormatUtils {

    private static final String LOG_TAG = TextFormatUtils.class.getSimpleName();

    private TextFormatUtils() {
    }

    public static String coastFormat(double coast) {
        String formatterStr = "%,.0f ₴";
//        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
//        Format financialFormatter = new DecimalFormat("#,##0.00 ₴");
        return String.format(Locale.getDefault(), formatterStr, coast);
    }

    public static String priceUnitFormat(double priceUnit) {
        String formatterStr = "%,.2f ₴/L";
        return String.format(Locale.getDefault(), formatterStr, priceUnit);
    }

    public static String volumeFormat(double volume) {
        String formatterStr = "%,.1f L";
        return String.format(Locale.getDefault(), formatterStr, volume);
    }

    public static String odometrFormat(int odometr) {
        String formatterStr = "%,d km";
        return String.format(Locale.getDefault(), formatterStr, odometr);
    }

    public static String distanceFormat(int distance) {
        String formatterStr = "+%,d km";
        return String.format(Locale.getDefault(), formatterStr, distance);
    }
}
