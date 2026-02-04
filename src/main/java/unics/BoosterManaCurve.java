package unics;

import java.util.List;

import unics.Enum.ManaCurveProfile;

public class BoosterManaCurve {

    private final int size;
    private final List<Integer> curve;

    public BoosterManaCurve(int size, ManaCurveProfile profile) {
        this.size = size;
        this.curve = ManaCurveGenerator.generate(size, profile);
    }

    public BoosterManaCurve(int size) {
        this(size, ManaCurveProfile.random());
    }
    
    public List<Integer> getCurve() {
        return curve;
    }

    public int getSize() {
        return size;
    }


    
}

