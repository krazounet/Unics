package unics.Enum;

public enum CardFilterType {

    // 🔹 Type
    IS_UNIT("unité"),
    IS_STRUCTURE("structure"),
    IS_ACTION("action"),

    // 🔹 Faction
    IS_ASTRAL("astrale"),
    IS_MECHANICAL("mécanique"),
    IS_ORGANIC("organique"),
    IS_OCCULT("occulte"),
    IS_NOMAD("nomade"),

    // 🔹 Coût
    COST_1_OR_LESS("coût ≤ 1"),
    COST_2_OR_LESS("coût ≤ 2"),
    COST_3_OR_LESS("coût ≤ 3"),

    // 🔹 Stats
    ATTACK_3_PLUS("attaque ≥ 3"),
    DEFENSE_4_PLUS("défense ≥ 4"),

    // 🔹 Mots-clés
    HAS_VOLATIL("ayant Volatil"),
    HAS_BOUCLIER("ayant Bouclier"),
    HAS_FRAPPE_IMMEDIATE("ayant Frappe immédiate"),

    // 🔹 Spéciaux
    IS_UNIQUE("unique"), 
    NONE("aucun filtre"), UNIT_ONLY("unité uniquement");

    private final String displayName;

    CardFilterType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

