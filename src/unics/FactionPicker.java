package unics;

import java.util.*;

import unics.Enum.Faction;

public class FactionPicker {

    private static final Random RNG = new Random();

    public static List<Faction> pickTwoDistinct() {
        List<Faction> factions = new ArrayList<>(List.of(Faction.values()));
        Collections.shuffle(factions, RNG);
        return factions.subList(0, 2);
    }
}
