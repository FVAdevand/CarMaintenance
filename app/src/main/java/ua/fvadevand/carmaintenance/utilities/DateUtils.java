package ua.fvadevand.carmaintenance.utilities;

import android.content.Context;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ua.fvadevand.carmaintenance.R;

public class DateUtils {
    private static final String LOG_TAG = DateUtils.class.getSimpleName();

    private DateUtils() {
    }

    public static String formatDate(Context context, long timeInMillis) {
        String formatterStr = context.getString(R.string.formatter_date);
        Format dateFormatter = new SimpleDateFormat(formatterStr, Locale.getDefault());
        return dateFormatter.format(timeInMillis);
    }
}
