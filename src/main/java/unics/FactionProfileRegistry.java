package unics;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import unics.Enum.AbilityType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.Enum.TriggerType;

/**
 * Registre central des profils de factions.
 * Définit les biais de génération (stats, effets, keywords)
 * sans imposer de règles strictes.
 */
public final class FactionProfileRegistry {

    private static final Map<Faction, FactionProfile> PROFILES =
            new EnumMap<>(Faction.class);

    static {

        // ───────────────────────────
        // 🕯 OCCULT — risque / sacrifice
        // ───────────────────────────
        PROFILES.put(Faction.OCCULT,
            new FactionProfile(
                Faction.OCCULT,
                //-5,   // statBias
                +20,  // effectBias
                +10,  // keywordBias
                Set.of(
                    Keyword.INSTABLE,
                    Keyword.UNIQUE
                ),
                Set.of(), // forbidden keywords
                Set.of(),
                Set.of(),
                Set.of(
                    AbilityType.DISCARD_SELF,
                    AbilityType.ENERGY_LOSS_SELF,
                    AbilityType.DAMAGE_PC_SELF
                ),
                Set.of(AbilityType.MOVE_SELF)  // forbidden abilities
            )
        );

        // ───────────────────────────
        // 🔧 MECHANICAL — robustesse & stats
        // ───────────────────────────
        PROFILES.put(Faction.MECHANICAL,
            new FactionProfile(
                Faction.MECHANICAL,
                //+15,
                -5,
                -5,
                Set.of(
                    Keyword.BOUCLIER,
                    Keyword.PERSISTANT,
                    Keyword.DEFENSIF
                ),
                Set.of(
                    Keyword.FURTIF,
                    Keyword.INSAISISSABLE
                ),
                Set.of(),
                Set.of(),
                Set.of(
                    AbilityType.BUFF,
                    AbilityType.MOVE_ALLY
                ),
                Set.of(
                    AbilityType.DISCARD_ENEMY,
                    AbilityType.MOVE_SELF
                )
            )
        );

        // ───────────────────────────
        // 🌿 ORGANIC — synergies & pression board
        // ───────────────────────────
        PROFILES.put(Faction.ORGANIC,
            new FactionProfile(
                Faction.ORGANIC,
                //+5,
                +0,
                +15,
                Set.of(
                    Keyword.TRAMPLE,
                    Keyword.PERSISTANT
                ),
                Set.of(
                    Keyword.INSAISISSABLE
                ),
                Set.of(),
                Set.of(),
                Set.of(
                    AbilityType.BUFF,
                    AbilityType.HEAL_PC
                ),
                Set.of(
                    AbilityType.DISCARD_SELF,
                    AbilityType.MOVE_SELF
                )
            )
        );

        // ───────────────────────────
        // 🧳 NOMAD — mobilité & opportunisme
        // ───────────────────────────
        PROFILES.put(Faction.NOMAD,
            new FactionProfile(
                Faction.NOMAD,
               // 0,
                -5,
                +20,
                Set.of(
                    Keyword.MOBILE,
                    Keyword.FURTIF,
                    Keyword.INSAISISSABLE
                ),
                Set.of(
                    Keyword.BOUCLIER
                ),
                Set.of(	TriggerType.ON_MOVE),
                Set.of(),
                Set.of(
                    AbilityType.MOVE_ALLY,
                    AbilityType.MOVE_ENEMY
                ),
                Set.of(AbilityType.MOVE_SELF)
            )
        );

        // ───────────────────────────
        // 🌌 ASTRAL — contrôle & timing
        // ───────────────────────────
        PROFILES.put(Faction.ASTRAL,
            new FactionProfile(
                Faction.ASTRAL,
                //-5,
                +10,
                +0,
                Set.of(
                    Keyword.UNIQUE,
                    Keyword.INSAISISSABLE
                ),
                Set.of(
                    Keyword.TRAMPLE
                ),
                Set.of(),
                Set.of(),
                Set.of(
                    AbilityType.DEBUFF_ENEMY,
                    AbilityType.DRAW,
                    AbilityType.DAMAGE_PC_ENEMY,
                    AbilityType.BUFF
                ),
                Set.of(
                    AbilityType.MOVE_ALLY,
                    AbilityType.MOVE_SELF
                )
            )
        );
    }

    private FactionProfileRegistry() {
        // classe utilitaire
    }

    /**
     * Retourne le profil de la faction.
     * Fallback sur un profil neutre si absent.
     */
    public static FactionProfile get(Faction faction) {
        FactionProfile profile = PROFILES.get(faction);
        if (profile == null) {
            System.err.println("⚠ Profil neutre utilisé pour " + faction);
            return FactionProfileRegistry.neutral();
        }
        return profile;
    }

    public static FactionProfile neutral() {
        return new FactionProfile(
            null,
            
            0, // effectBias
            0, // keywordBias
            Set.of(), // favoredKeywords
            Set.of(), // forbiddenKeywords
            Set.of(),
            Set.of(),
            Set.of(), // favoredAbilities
            Set.of()  // forbiddenAbilities
        );
    }

}
