package unics.Enum;

public enum TargetConstraint {

    // ───────── Type de carte ─────────
    UNIT("unité", 0),
    STRUCTURE("structure", 0),

    // ───────── Faction ─────────
    FACTION_ASTRAL("astrale", -5),
    FACTION_ORGANIC("organique", -5),
    FACTION_NOMAD("nomade", -5),
    FACTION_MECHANICAL("mécanique", -5),
    FACTION_OCCULT("occulte", -5),

    // ───────── Coût ─────────
    COST_3_OR_LESS("de coût 3 ou moins", -5),
    COST_2_OR_LESS("de coût 2 ou moins", -10),
    COST_1_OR_LESS("de coût 1 ou moins", -15);

    private final String displayName;
    private final int powerModifier; // ⚠️ négatif

    TargetConstraint(String displayName, int powerModifier) {
        this.displayName = displayName;
        this.powerModifier = powerModifier; // en pourcentage !
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Modificateur de puissance (négatif = restriction)
     */
    public int getPowerModifier() {
        return powerModifier;
    }
 // ─────────────────────────────────────────────
    // 1️⃣ TEXTE POUR CIBLAGE (unité / structure)
    // ─────────────────────────────────────────────

    public String getTargetAdjective() {

        return switch (this) {

            // Factions → adjectif
            case FACTION_ASTRAL,
                 FACTION_ORGANIC,
                 FACTION_NOMAD,
                 FACTION_MECHANICAL,
                 FACTION_OCCULT
                    -> getDisplayName();

            // Coût → complément
            case COST_1_OR_LESS,
                 COST_2_OR_LESS,
                 COST_3_OR_LESS
                    -> getDisplayName();

            // Type → rarement affiché (souvent déjà implicite)
            case UNIT,
                 STRUCTURE
                    -> ""; // volontairement vide
        };
    }
    
    public String getPossessionText() {

        return switch (this) {

            // Factions
            case FACTION_ASTRAL ->
                    "une carte astrale en jeu";

            case FACTION_ORGANIC ->
                    "une carte organique en jeu";

            case FACTION_NOMAD ->
                    "une carte nomade en jeu";

            case FACTION_MECHANICAL ->
                    "une carte mécanique en jeu";

            case FACTION_OCCULT ->
                    "une carte occulte en jeu";

            // Coûts
            case COST_1_OR_LESS ->
                    "une carte de coût 1 ou moins";

            case COST_2_OR_LESS ->
                    "une carte de coût 2 ou moins";

            case COST_3_OR_LESS ->
                    "une carte de coût 3 ou moins";

            // Types
            case UNIT ->
                    "une unité en jeu";

            case STRUCTURE ->
                    "une structure en jeu";
        };
    }
    
}
