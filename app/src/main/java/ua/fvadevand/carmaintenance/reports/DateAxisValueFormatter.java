package com.example.vladimir.graphexample.utilities;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    private String mFormatterStr;

    public DateAxisValueFormatter(String formatterStr) {
        mFormatterStr = formatterStr;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        SimpleDateFormat formatter = new SimpleDateFormat(mFormatterStr, Locale.getDefault());
        Date date = new Date((long) value);
        return formatter.format(date);
    }
}
