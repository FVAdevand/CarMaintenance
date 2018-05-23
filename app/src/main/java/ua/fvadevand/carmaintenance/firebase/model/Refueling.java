package ua.fvadevand.carmaintenance.firebase.model;

public class Refueling {
    private static final String LOG_TAG = Refueling.class.getSimpleName();

    private int mId;
    private long mTimeStamp;
    private double mVolume;
    private double mPriceUnit;
    private double mCoast;
    private double mFuelBalance;
    private int mOdometr;
    private int mDistance;
    private String mGasStation;
    private String mBrandFuel;

    public Refueling() {
    }

    public Refueling(long timeStamp, double volume, double priceUnit,
                     double coast, double fuelBalance, int odometr,
                     int distance, String gasStation, String brandFuel) {
        mTimeStamp = timeStamp;
        mVolume = volume;
        mPriceUnit = priceUnit;
        mCoast = coast;
        mFuelBalance = fuelBalance;
        mOdometr = odometr;
        mDistance = distance;
        mGasStation = gasStation;
        mBrandFuel = brandFuel;
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

    public double getCoast() {
        return mCoast;
    }

    public void setCoast(double coast) {
        mCoast = coast;
    }

    public double getFuelBalance() {
        return mFuelBalance;
    }

    public void setFuelBalance(double fuelBalance) {
        mFuelBalance = fuelBalance;
    }

    public int getOdometr() {
        return mOdometr;
    }

    public void setOdometr(int odometr) {
        mOdometr = odometr;
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

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }
}
