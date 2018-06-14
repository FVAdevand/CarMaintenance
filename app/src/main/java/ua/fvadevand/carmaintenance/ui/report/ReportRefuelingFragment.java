package ua.fvadevand.carmaintenance.ui.report;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vladimir.graphexample.utilities.DateAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.firebase.FirebaseRefueling;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.reports.ReportRefuelingCalculator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportRefuelingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportRefuelingFragment extends Fragment {

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
        fetchRefuelingList();

        initViews(view);
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
                        showFuelVolumeChart();
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
    }

    private void showFuelVolumeChart() {
        if (mReportRefuelingCalculator.getFuelVolumeList().size() == 0) return;

        Log.i(LOG_TAG, "showFuelVolumeChart: " + mReportRefuelingCalculator.getFuelVolumeList().size());

        LineDataSet lineDataSet = new LineDataSet(mReportRefuelingCalculator.getFuelVolumeList(), "Fuel volume");
        lineDataSet.setValueTextSize(10f);

        LineData lineData = new LineData(lineDataSet);

        Description description = mFuelVolumeChart.getDescription();
        description.setText("Fuel volume");
        description.setTextSize(10f);

        XAxis xAxis = mFuelVolumeChart.getXAxis();
        xAxis.setValueFormatter(new DateAxisValueFormatter("MM/yy"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mFuelVolumeChart.setData(lineData);
        mFuelVolumeChart.getLegend().setEnabled(false);
        mFuelVolumeChart.invalidate();
    }

}
