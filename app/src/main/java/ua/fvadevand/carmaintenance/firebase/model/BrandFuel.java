package ua.fvadevand.carmaintenance.firebase.model;

public class BrandFuel {
    private static final String LOG_TAG = BrandFuel.class.getSimpleName();

    private String mName;

    public BrandFuel() {
    }

    public BrandFuel(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
