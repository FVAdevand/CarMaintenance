package ua.fvadevand.carmaintenance.utilities;

import android.content.Context;

import java.util.Locale;

import ua.fvadevand.carmaintenance.R;

public class TextFormatUtils {

    private static final String LOG_TAG = TextFormatUtils.class.getSimpleName();

    private Context mContext;

    public TextFormatUtils(Context context) {
        mContext = context;
    }

    public String costFormat(double cost) {
        String formatterStr = mContext.getString(R.string.formatter_cost);
        return String.format(Locale.getDefault(), formatterStr, cost);
    }

    public String priceUnitFormat(double priceUnit) {
        String formatterStr = mContext.getString(R.string.formatter_price_unit);
        return String.format(Locale.getDefault(), formatterStr, priceUnit);
    }

    public String volumeFormat(double volume) {
        String formatterStr = mContext.getString(R.string.formatter_volume);
        return String.format(Locale.getDefault(), formatterStr, volume);
    }

    public String odometerFormat(int odometer) {
        String formatterStr = mContext.getString(R.string.formatter_odometer);
        return String.format(Locale.getDefault(), formatterStr, odometer);
    }

    public String fuelRateFormat(double fuelRate) {
        String formatterStr = mContext.getString(R.string.formatter_fuel_rate);
        return String.format(Locale.getDefault(), formatterStr, fuelRate);
    }

    public String decimalFormatWithDot(double numberDecimal) {
        String numberDecimalStr = String.format(Locale.getDefault(), "%.2f", numberDecimal);
        return numberDecimalStr.replace(",", ".");
    }

    public String decimalFormat(double numberDecimal) {
        if (numberDecimal > 100) {
            return String.format(Locale.getDefault(), "%.0f", numberDecimal);
        }
        return String.format(Locale.getDefault(), "%.1f", numberDecimal);
    }

    public String percentFormat(double value) {
        String formatterStr = mContext.getString(R.string.formatter_percent);
        return String.format(Locale.getDefault(), formatterStr, value);
    }

    public String priceDistanceFormat(double price) {
        String formatterStr = mContext.getString(R.string.formatter_price_distance);
        return String.format(Locale.getDefault(), formatterStr, price);
    }
}
