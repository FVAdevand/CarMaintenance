package ua.fvadevand.carmaintenance.reports;


import android.support.v4.util.ArrayMap;

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
    private double mFullFuelVolume;

    private List<Entry> mFuelVolumeList;
    private List<Entry> mFuelCostList;
    private List<Entry> mFuelPriceUnitList;
    private List<Entry> mFuelRateList;
    private List<PieEntry> mGasStationList;
    private List<PieEntry> mFuelBrandList;
    private ArrayMap<String, Double> mGasStationMap;
    private ArrayMap<String, Double> mFuelBrandMap;


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

        mGasStationMap = new ArrayMap<>();
        mFuelBrandMap = new ArrayMap<>();

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

    public double getFullFuelVolume() {
        return mFullFuelVolume;
    }

    public void notifyDataSetChanged() {
        clearData();

        correctLastFuelRate();

        for (Refueling refueling : mRefuelingList) {
            if (refueling.getTimestamp() >= mTimestampFrom && refueling.getTimestamp() <= mTimestampTo) {
                calcFuelVolume(refueling);
                calcFuelCostList(refueling);
                calcFuelPriceUnitList(refueling);
                calcFuelRateList(refueling);
                calcGasStationMap(refueling);
                calcFuelBrandMap(refueling);
                calcFullFuelVolume(refueling);
            }
        }

        calcGasStationList();
        calcFuelBrandList();
    }

    private void correctLastFuelRate() {
        if (mRefuelingList.size() > 1) {
            double previousFuelRate = mRefuelingList.get(mRefuelingList.size() - 2).getFuelRate();
            mRefuelingList.get(mRefuelingList.size() - 1).setFuelRate(previousFuelRate);
        }
    }

    private void clearData() {
        mFuelVolumeList.clear();
        mFuelCostList.clear();
        mFuelPriceUnitList.clear();
        mFuelRateList.clear();
        mGasStationList.clear();
        mFuelBrandList.clear();
        mGasStationMap.clear();
        mFuelBrandMap.clear();
        mFullFuelVolume = 0;
    }

    private void calcFullFuelVolume(Refueling refueling) {
        mFullFuelVolume += refueling.getVolume();
    }

    private void calcFuelVolume(Refueling refueling) {
        mFuelVolumeList.add(new Entry(refueling.getTimestamp(), (float) refueling.getVolume()));
    }

    private void calcFuelCostList(Refueling refueling) {
        mFuelCostList.add(new Entry(refueling.getTimestamp(), (float) refueling.getCost()));
    }

    private void calcFuelPriceUnitList(Refueling refueling) {
        mFuelPriceUnitList.add(new Entry(refueling.getTimestamp(), (float) refueling.getPriceUnit()));
    }

    private void calcFuelRateList(Refueling refueling) {
        mFuelRateList.add(new Entry(refueling.getTimestamp(), (float) refueling.getFuelRate()));
    }

    private void calcGasStationMap(Refueling refueling) {
        String gasStation = refueling.getGasStation();
        if (mGasStationMap.containsKey(gasStation)) {
            double fuelVolume = mGasStationMap.get(gasStation);
            mGasStationMap.put(gasStation, fuelVolume + refueling.getVolume());
        } else {
            mGasStationMap.put(gasStation, refueling.getVolume());
        }
    }

    private void calcFuelBrandMap(Refueling refueling) {
        String fuelBrand = refueling.getBrandFuel();
        if (mFuelBrandMap.containsKey(fuelBrand)) {
            double fuelVolume = mFuelBrandMap.get(fuelBrand);
            mFuelBrandMap.put(fuelBrand, fuelVolume + refueling.getVolume());
        } else {
            mFuelBrandMap.put(fuelBrand, refueling.getVolume());
        }
    }

    private void calcGasStationList() {
        for (int i = 0; i < mGasStationMap.size(); i++) {
            mGasStationList.add(
                    new PieEntry((float) getPercentVolume(mGasStationMap.valueAt(i)), mGasStationMap.keyAt(i)));
        }
    }

    private void calcFuelBrandList() {
        for (int i = 0; i < mFuelBrandMap.size(); i++) {
            mFuelBrandList.add(
                    new PieEntry((float) getPercentVolume(mFuelBrandMap.valueAt(i)), mFuelBrandMap.keyAt(i)));
        }
    }

    private double getPercentVolume(double fuelVolume) {
        return fuelVolume / mFullFuelVolume * 100;
    }
}
