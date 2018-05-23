package ua.fvadevand.carmaintenance.firebase.model;

public class GasStation {
    private static final String LOG_TAG = GasStation.class.getSimpleName();

    private String mName;

    public GasStation() {
    }

    public GasStation(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
