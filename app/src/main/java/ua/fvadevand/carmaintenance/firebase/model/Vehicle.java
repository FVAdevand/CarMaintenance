package ua.fvadevand.carmaintenance.firebase.model;

import java.util.Objects;

public class Vehicle {
    private static final String LOG_TAG = Vehicle.class.getSimpleName();

    private String mId;
    private String mManufacturer;
    private String mModel;
    private String mPhotoPath;
    private String mYearManufacture;
    private int mInitialOdometer;
    private long mPhotoTimestamp;

    public Vehicle() {
    }

    public Vehicle(String model) {
        mModel = model;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(String manufacturer) {
        mManufacturer = manufacturer;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        mPhotoPath = photoPath;
    }

    public String getYearManufacture() {
        return mYearManufacture;
    }

    public void setYearManufacture(String yearManufacture) {
        mYearManufacture = yearManufacture;
    }

    public int getInitialOdometer() {
        return mInitialOdometer;
    }

    public void setInitialOdometer(int initialOdometer) {
        mInitialOdometer = initialOdometer;
    }

    public long getPhotoTimestamp() {
        return mPhotoTimestamp;
    }

    public void setPhotoTimestamp(long photoTimestamp) {
        mPhotoTimestamp = photoTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return mInitialOdometer == vehicle.mInitialOdometer &&
                mPhotoTimestamp == vehicle.mPhotoTimestamp &&
                Objects.equals(mId, vehicle.mId) &&
                Objects.equals(mManufacturer, vehicle.mManufacturer) &&
                Objects.equals(mModel, vehicle.mModel) &&
                Objects.equals(mPhotoPath, vehicle.mPhotoPath) &&
                Objects.equals(mYearManufacture, vehicle.mYearManufacture);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mId, mManufacturer, mModel, mPhotoPath, mYearManufacture, mInitialOdometer, mPhotoTimestamp);
    }
}
