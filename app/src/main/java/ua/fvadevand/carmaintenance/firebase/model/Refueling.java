package ua.fvadevand.carmaintenance.firebase.model;

public class Refueling {

    private String mId;
    private long mTimeStamp;
    private double mVolume;
    private double mPriceUnit;
    private double mCost;
    private double mFuelBalance;
    private int mOdometer;
    private String mGasStation;
    private String mBrandFuel;
    private double mFuelRate;

    public Refueling() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public double getVolume() {
        return mVolume;
    }

    public void setVolume(double volume) {
        mVolume = volume;
    }

    public double getPriceUnit() {
        return mPriceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        mPriceUnit = priceUnit;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public double getFuelBalance() {
        return mFuelBalance;
    }

    public void setFuelBalance(double fuelBalance) {
        mFuelBalance = fuelBalance;
    }

    public int getOdometer() {
        return mOdometer;
    }

    public void setOdometer(int odometer) {
        mOdometer = odometer;
    }

    public String getGasStation() {
        return mGasStation;
    }

    public void setGasStation(String gasStation) {
        mGasStation = gasStation;
    }

    public String getBrandFuel() {
        return mBrandFuel;
    }

    public void setBrandFuel(String brandFuel) {
        mBrandFuel = brandFuel;
    }

    public double getFuelRate() {
        return mFuelRate;
    }

    public void setFuelRate(double fuelRate) {
        mFuelRate = fuelRate;
    }
}
