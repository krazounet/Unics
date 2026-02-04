package unics;

import java.util.*;

import unics.Enum.ManaCurveProfile;

public class ManaCurveGenerator {

    private static final Random RNG = new Random();

    public static List<Integer> generate(int size, ManaCurveProfile profile) {
        Map<Integer, Integer> weights = ManaCurveDistributions.WEIGHTS.get(profile);

        List<Integer> pool = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : weights.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                pool.add(entry.getKey());
            }
        }

        List<Integer> curve = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            curve.add(pool.get(RNG.nextInt(pool.size())));
        }

        Collections.sort(curve);
        return curve;
    }
}