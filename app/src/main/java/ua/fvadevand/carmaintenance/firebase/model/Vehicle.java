package ua.fvadevand.carmaintenance.firebase.model;

public class Vehicle {
    private static final String LOG_TAG = Vehicle.class.getSimpleName();

    private String mId;
    private String mManufacturer;
    private String mModel;
    private String mPhotoPath;
    private String mYearManufacture;
    private int mInitialOdometr;
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

    public int getInitialOdometr() {
        return mInitialOdometr;
    }

    public void setInitialOdometr(int initialOdometr) {
        mInitialOdometr = initialOdometr;
    }

    public long getPhotoTimestamp() {
        return mPhotoTimestamp;
    }

    public void setPhotoTimestamp(long photoTimestamp) {
        mPhotoTimestamp = photoTimestamp;
    }
}
