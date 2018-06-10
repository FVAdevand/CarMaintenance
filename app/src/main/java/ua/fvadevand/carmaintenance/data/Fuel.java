package ua.fvadevand.carmaintenance.data;

public class Fuel {

    private String mType;

    private double mPrice;

    public Fuel() {
    }

    public Fuel(String type, double price) {
        this.mType = type;
        this.mPrice = price;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }
}
