package unics;

import java.util.*;

import unics.Enum.Faction;


/**
 * Défnit les N factions qui seront dans le booster
 * les Integer dans la liste indiquent le nb de chaque cartes de chaque faction
 * 
 */
public class FactionDistribution {

    private static final Random RNG = new Random();

    public static List<Faction> generate(List<Integer> distribution) {

        if (distribution.isEmpty()) {
            throw new IllegalArgumentException("Distribution vide");
        }

        int factionCount = distribution.size();

        if (factionCount > Faction.values().length) {
            throw new IllegalArgumentException("Pas assez de factions disponibles");
        }

        List<Faction> available = new ArrayList<>(List.of(Faction.values()));
        Collections.shuffle(available, RNG);

        List<Faction> chosen = available.subList(0, factionCount);

        List<Faction> result = new ArrayList<>();

        for (int i = 0; i < distribution.size(); i++) {
            int amount = distribution.get(i);
            Faction faction = chosen.get(i);

            for (int j = 0; j < amount; j++) {
                result.add(faction);
            }
        }

        Collections.shuffle(result, RNG);
        return result;
    }
}
