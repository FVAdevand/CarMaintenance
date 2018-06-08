package ua.fvadevand.carmaintenance.utilities;

import ua.fvadevand.carmaintenance.firebase.model.Refueling;

public class CalculationUtils {

    private CalculationUtils() {
    }

    public static double calculationFuelRate(Refueling pastRefueling, Refueling currentRefueling) {
        int distance = currentRefueling.getOdometer() - pastRefueling.getOdometer();
        double pastVolume = pastRefueling.getVolume() + pastRefueling.getFuelBalance();
        double currentVolume = currentRefueling.getFuelBalance();
        double volume = pastVolume - currentVolume;
        return volume / (distance / 100.0);
    }
}
