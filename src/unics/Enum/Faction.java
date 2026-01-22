/**
 * 
 */
package unics.Enum;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 */
public enum Faction {
    ASTRAL,
    ORGANIC,
    NOMAD,
    MECHANICAL,
    OCCULT;
    
    
	public static Faction randomFaction() {
        Faction[] values = Faction.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
