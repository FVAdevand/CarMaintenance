package ua.fvadevand.carmaintenance.ui.report;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.firebase.FirebaseRefueling;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.reports.DateAxisValueFormatter;
import ua.fvadevand.carmaintenance.reports.ReportMarkerView;
import ua.fvadevand.carmaintenance.reports.ReportRefuelingCalculator;
import ua.fvadevand.carmaintenance.utilities.TextFormatUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportRefuelingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportRefuelingFragment extends Fragment
        implements ReportActivity.DateChanging {

    private static final String LOG_TAG = ReportRefuelingFragment.class.getSimpleName();

    private static final String ARG_CURRENT_VEHICLE_ID = "current_vehicle_id";
    private static final String ARG_TIMESTAMP_FROM = "timestamp_from";
    private static final String ARG_TIMESTAMP_TO = "timestamp_to";

    private String mCurrentVehicleId;
    private long mTimestampFrom;
    private long mTimestampTo;

    private LineChart mFuelVolumeChart;
    private LineChart mFuelCostChart;
    private LineChart mFuelPriceUnitChart;
    private LineChart mFuelRateChart;
    private PieChart mGasStationPieChart;
    private PieChart mFuelBrandPieChart;

    private ReportRefuelingCalculator mReportRefuelingCalculator;
    private EditText mTotalDistanceView;
    private EditText mTotalFuelView;
    private EditText mTotalCostView;
    private EditText mAverageFuelRateView;
    private EditText mAverageFuelPriceView;
    private EditText mTotalRefuelingView;

    public ReportRefuelingFragment() {
    }

    public static ReportRefuelingFragment newInstance(String currentVehicleId, long timestampFrom, long timestampTo) {
        ReportRefuelingFragment fragment = new ReportRefuelingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_VEHICLE_ID, currentVehicleId);
        args.putLong(ARG_TIMESTAMP_FROM, timestampFrom);
        args.putLong(ARG_TIMESTAMP_TO, timestampTo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentVehicleId = getArguments().getString(ARG_CURRENT_VEHICLE_ID);
            mTimestampFrom = getArguments().getLong(ARG_TIMESTAMP_FROM);
            mTimestampTo = getArguments().getLong(ARG_TIMESTAMP_TO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_refueling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        fetchRefuelingList();
    }

    private void fetchRefuelingList() {
        FirebaseRefueling.getCurVehRefuelingRef(mCurrentVehicleId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Refueling> refuelingList = new ArrayList<>();
                        for (DataSnapshot refuelingSnapshot : dataSnapshot.getChildren()) {
                            refuelingList.add(refuelingSnapshot.getValue(Refueling.class));
                        }
                        mReportRefuelingCalculator = new ReportRefuelingCalculator(refuelingList,
                                mTimestampFrom, mTimestampTo);

                        displayChart();
                        displayParameters();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initViews(View v) {
        mFuelVolumeChart = v.findViewById(R.id.lch_fuel_volume);
        mFuelCostChart = v.findViewById(R.id.lch_fuel_cost);
        mFuelPriceUnitChart = v.findViewById(R.id.lch_fuel_price_unit);
        mFuelRateChart = v.findViewById(R.id.lch_fuel_rate);
        mGasStationPieChart = v.findViewById(R.id.pch_gas_station);
        mFuelBrandPieChart = v.findViewById(R.id.pch_fuel_brand);

        mTotalDistanceView = v.findViewById(R.id.et_report_refueling_total_distance);
        disableEditText(mTotalDistanceView);

        mTotalFuelView = v.findViewById(R.id.et_report_refueling_total_fuel);
        disableEditText(mTotalFuelView);

        mTotalCostView = v.findViewById(R.id.et_report_refueling_total_cost);
        disableEditText(mTotalCostView);

        mTotalRefuelingView = v.findViewById(R.id.et_report_refueling_total_refueling);
        disableEditText(mTotalRefuelingView);

        mAverageFuelRateView = v.findViewById(R.id.et_report_refueling_average_fuel_rate);
        disableEditText(mAverageFuelRateView);

        mAverageFuelPriceView = v.findViewById(R.id.et_report_refueling_average_fuel_price);
        disableEditText(mAverageFuelPriceView);

    }

    private void disableEditText(EditText editText) {
        editText.setEnabled(false);
        editText.setTextColor(Color.BLACK);
        editText.clearFocus();
    }

    private void showLineChart(LineChart lineChart, List<Entry> entryList, String label, int fillColor) {
        lineChart.setNoDataText("Empty chart");

        if (entryList.size() == 0) {
            lineChart.clear();
            lineChart.invalidate();
            return;
        }

        LineDataSet lineDataSet = new LineDataSet(entryList, label);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(fillColor);
        lineDataSet.setColor(getResources().getColor(R.color.colorLineChart));
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorLineChart));
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return TextFormatUtils.decimalFormat(value);
            }
        });

        LineData lineData = new LineData(lineDataSet);

        Description description = lineChart.getDescription();
        description.setText(label);
        description.setTextSize(12f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new DateAxisValueFormatter("MM/yy"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return TextFormatUtils.decimalFormat(value);
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.setData(lineData);
        lineChart.getLegend().setEnabled(false);
        lineChart.setMarker(new ReportMarkerView(getContext(), R.layout.report_marker));
        lineChart.invalidate();
    }

    private void showPieChart(PieChart pieChart, List<PieEntry> pieEntryList, String label, int[] colors, String centerText) {
        if (pieEntryList.size() == 0) return;

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, label);
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return TextFormatUtils.percentFormat(value);
            }
        });

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(getResources().getColor(android.R.color.primary_text_light));
        pieChart.setCenterText(centerText);
        pieChart.setCenterTextSize(14f);

        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setTextSize(12f);

        pieChart.invalidate();
    }

    @Override
    public void changeDateFrom(long timestamp) {
        mTimestampFrom = timestamp;
        if (getArguments() != null) {
            getArguments().putLong(ARG_TIMESTAMP_FROM, mTimestampFrom);
        }
        mReportRefuelingCalculator.setTimestampFrom(timestamp);
        displayChart();
        displayParameters();
    }

    @Override
    public void changeDateTo(long timestamp) {
        mTimestampTo = timestamp;
        if (getArguments() != null) {
            getArguments().putLong(ARG_TIMESTAMP_TO, mTimestampTo);
        }
        mReportRefuelingCalculator.setTimestampTo(timestamp);
        displayChart();
        displayParameters();
    }

    private void displayChart() {
        showLineChart(mFuelVolumeChart,
                mReportRefuelingCalculator.getFuelVolumeList(),
                "Fuel volume",
                getResources().getColor(R.color.colorChart1));

        showLineChart(mFuelCostChart,
                mReportRefuelingCalculator.getFuelCostList(),
                "Fuel cost",
                getResources().getColor(R.color.colorChart2));

        showLineChart(mFuelPriceUnitChart,
                mReportRefuelingCalculator.getFuelPriceUnitList(),
                "Fuel price unit",
                getResources().getColor(R.color.colorChart3));

        showLineChart(mFuelRateChart,
                mReportRefuelingCalculator.getFuelRateList(),
                "Fuel rate",
                getResources().getColor(R.color.colorChart4));

        showPieChart(mGasStationPieChart,
                mReportRefuelingCalculator.getGasStationList(),
                "Gas station",
                ColorTemplate.JOYFUL_COLORS,
                TextFormatUtils.volumeFormat(mReportRefuelingCalculator.getTotalFuelVolume()));

        showPieChart(mFuelBrandPieChart,
                mReportRefuelingCalculator.getFuelBrandList(),
                "Fuel brand",
                ColorTemplate.MATERIAL_COLORS,
                TextFormatUtils.volumeFormat(mReportRefuelingCalculator.getTotalFuelVolume()));
    }

    private void displayParameters() {
        mTotalFuelView.setText(TextFormatUtils.volumeFormat(mReportRefuelingCalculator.getTotalFuelVolume()));
        mTotalCostView.setText(TextFormatUtils.costFormat(mReportRefuelingCalculator.getTotalFuelCost()));
    }
}
