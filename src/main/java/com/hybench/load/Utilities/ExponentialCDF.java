package Utilities;

import umontreal.iro.lecuyer.probdist.ExponentialDist;
import umontreal.iro.lecuyer.probdist.NormalDist;
import java.util.Random;


public class ExponentialCDF {
    private ExponentialDist ExponenDist;

    public ExponentialCDF(double lambda) {
        ExponenDist = new ExponentialDist(lambda);
    }

    public ExponentialDist getExponenDist() {
        return ExponenDist;
    }

    public int getValue(Random random) {
        return (int) ExponenDist.inverseF(random.nextDouble());
    }

    public double getDouble(Random random) {
        return ExponenDist.inverseF(random.nextDouble());
    }

    public static void main(String[] args) {
        double lambda = 0.01;
        RandomGenerator RG= new RandomGenerator();
        ExponentialCDF exponen = new ExponentialCDF(lambda);
        for (int i = 1; i <= 10 ; i++) {
            System.out.println(exponen.getValue(RG.rand));
        }
    }
}
