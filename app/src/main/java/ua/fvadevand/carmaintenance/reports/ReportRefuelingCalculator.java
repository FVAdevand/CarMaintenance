package ua.fvadevand.carmaintenance.reports;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import ua.fvadevand.carmaintenance.firebase.model.Refueling;

public class ReportRefuelingCalculator {

    private static final String LOG_TAG = ReportRefuelingCalculator.class.getSimpleName();

    private List<Refueling> mRefuelingList;
    private long mTimestampFrom;
    private long mTimestampTo;

    private List<Entry> mFuelVolumeList;
    private List<Entry> mFuelCostList;
    private List<Entry> mFuelPriceUnitList;
    private List<Entry> mFuelRateList;
    private List<PieEntry> mGasStationList;
    private List<PieEntry> mFuelBrandList;


    public ReportRefuelingCalculator(List<Refueling> refuelingList, long timestampFrom, long timestampTo) {
        mRefuelingList = refuelingList;
        mTimestampFrom = timestampFrom;
        mTimestampTo = timestampTo;

        mFuelVolumeList = new ArrayList<>();
        mFuelCostList = new ArrayList<>();
        mFuelPriceUnitList = new ArrayList<>();
        mFuelRateList = new ArrayList<>();
        mGasStationList = new ArrayList<>();
        mFuelBrandList = new ArrayList<>();

        notifyDataSetChanged();
    }

    public List<Refueling> getRefuelingList() {
        return mRefuelingList;
    }

    public long getTimestampFrom() {
        return mTimestampFrom;
    }

    public void setTimestampFrom(long timestampFrom) {
        mTimestampFrom = timestampFrom;
        notifyDataSetChanged();
    }

    public long getTimestampTo() {
        return mTimestampTo;
    }

    public void setTimestampTo(long timestampTo) {
        mTimestampTo = timestampTo;
        notifyDataSetChanged();
    }

    public List<Entry> getFuelVolumeList() {
        return mFuelVolumeList;
    }

    public List<Entry> getFuelCostList() {
        return mFuelCostList;
    }

    public List<Entry> getFuelPriceUnitList() {
        return mFuelPriceUnitList;
    }

    public List<Entry> getFuelRateList() {
        return mFuelRateList;
    }

    public List<PieEntry> getGasStationList() {
        return mGasStationList;
    }

    public List<PieEntry> getFuelBrandList() {
        return mFuelBrandList;
    }

    public void notifyDataSetChanged() {
        for (Refueling refueling : mRefuelingList) {
            if (refueling.getTimestamp() >= mTimestampFrom && refueling.getTimestamp() <= mTimestampTo) {
                calculationFuelVolume(refueling);
                Log.i(LOG_TAG, "notifyDataSetChanged: " + refueling);
            }
        }
    }

    private void calculationFuelVolume(Refueling refueling) {
        mFuelVolumeList.add(new Entry(refueling.getTimestamp(), (float) refueling.getVolume()));
    }
}
