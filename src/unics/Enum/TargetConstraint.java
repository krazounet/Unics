package unics.Enum;
/**
 * contrainte a la réalisation d'un effet
 * le powermodifer est en %
 */
public enum TargetConstraint {
	/*
    // ───────── Type de carte ─────────
    UNIT("unité", 0),
    STRUCTURE("structure", 0),
	*/
    // ───────── Faction ─────────
    FACTION_ASTRAL("astrale", -30),
    FACTION_ORGANIC("organique", -30),
    FACTION_NOMAD("nomade", -30),
    FACTION_MECHANICAL("mécanique", -30),
    FACTION_OCCULT("occulte", -30),

    // ───────── Coût ─────────
    COST_3_OR_LESS("de coût 3 ou moins", -20),
    COST_2_OR_LESS("de coût 2 ou moins", -25),
    COST_1_OR_LESS("de coût 1", -30);//a revoir si des cartes coute 0

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
                    /*
            // Type → rarement affiché (souvent déjà implicite)
            case UNIT,
                 STRUCTURE
                    -> "*"; // volontairement vide
              		*/
        };
    }
    
    public String getPossessionText() {

        return switch (this) {

            // Factions
            case FACTION_ASTRAL ->
                    "une autre carte astrale en jeu";

            case FACTION_ORGANIC ->
                    "une autre carte organique en jeu";

            case FACTION_NOMAD ->
                    "une autre carte nomade en jeu";

            case FACTION_MECHANICAL ->
                    "une autre carte mécanique en jeu";

            case FACTION_OCCULT ->
                    "une autre carte occulte en jeu";

            // Coûts
            case COST_1_OR_LESS ->
                    "une autre carte de coût 1 ou moins";

            case COST_2_OR_LESS ->
                    "une autre carte de coût 2 ou moins";

            case COST_3_OR_LESS ->
                    "une autre carte de coût 3 ou moins";
                    /*
            // Types
            case UNIT ->
                    "une autre unité en jeu";

            case STRUCTURE ->
                    "une autre structure en jeu";
                    */
        };
    }
    
}
