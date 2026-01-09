package unics.Enum;

import java.util.Random;

public enum ManaCurveProfile {

    AGGRO(20),
    MIDRANGE(20),
    BALANCED(20),
    CONTROL(20),
    HEAVY(20);

    private final int weight;
    private static final Random RNG = new Random();

    ManaCurveProfile(int weight) {
        this.weight = weight;
    }

    public static ManaCurveProfile random() {
        int total = 0;
        for (ManaCurveProfile p : values()) {
            total += p.weight;
        }

        int roll = RNG.nextInt(total);

        int current = 0;
        for (ManaCurveProfile p : values()) {
            current += p.weight;
            if (roll < current) {
                return p;
            }
        }

        throw new IllegalStateException("Impossible");
    }
}
