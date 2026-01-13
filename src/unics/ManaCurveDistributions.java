package unics;

import java.util.Map;

import unics.Enum.ManaCurveProfile;

public class ManaCurveDistributions {

    public static final Map<ManaCurveProfile, Map<Integer, Integer>> WEIGHTS = Map.of(
        ManaCurveProfile.AGGRO, Map.of(
            1, 20,
            2, 35,
            3, 20,
            4, 10,
            5, 10,
            6, 5
        ),
        ManaCurveProfile.MIDRANGE, Map.of(
            1, 10,
            2, 25,
            3, 25,
            4, 25,
            5, 10,
            6, 5
        ),
        ManaCurveProfile.CONTROL, Map.of(
            1, 5,
            2, 15,
            3, 20,
            4, 25,
            5, 20,
            6, 15
        ),
        ManaCurveProfile.HEAVY, Map.of(
            1, 0,
            2, 10,
            3, 15,
            4, 25,
            5, 25,
            6, 25
        ),
        ManaCurveProfile.BALANCED, Map.of(
            1, 15,
            2, 20,
            3, 20,
            4, 20,
            5, 15,
            6, 10
        )
    );
}
