package ua.fvadevand.carmaintenance.reports;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.utilities.DateUtils;
import ua.fvadevand.carmaintenance.utilities.TextFormatUtils;

public class ReportMarkerView extends MarkerView {

    private TextView mTextView;
    private MPPointF mOffset;

    public ReportMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        mTextView = findViewById(R.id.tv_marker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mTextView.setText(TextFormatUtils.decimalFormat(e.getY()) + "\n" + DateUtils.formatDate(getContext(), (long) e.getX()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight() - 10);
        }
        return mOffset;
    }
}
