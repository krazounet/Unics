package unics.Enum;

/**
 * Enum représentant la cible d'une capacité.
 * Décrit QUI est affecté par l'effet.
 */
public enum TargetType {

    SELF(
        "Soi-même",
        "La capacité cible uniquement cette carte",
        false,
        5
    ),

    ALLY(
        "Allié",
        "La capacité cible une carte alliée",
        true,
        10
    ),

    ENEMY(
        "Ennemi",
        "La capacité cible une carte ennemie",
        true,
        15
    ),

    ALL(
        "Toutes les cartes",
        "La capacité affecte toutes les cartes en jeu",
        false,
        25
    );

    private final String displayName;     // Affichage carte
    private final String description;     // Règles / debug
    private final boolean requiresChoice; // Le joueur doit choisir une cible
    private final int weight;              // Poids pour génération / équilibre

    TargetType(String displayName, String description, boolean requiresChoice, int weight) {
        this.displayName = displayName;
        this.description = description;
        this.requiresChoice = requiresChoice;
        this.weight = weight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean requiresChoice() {
        return requiresChoice;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Utilisé pour debug / logs
     */
    @Override
    public String toString() {
        return displayName + " (" + name() + ", poids=" + weight + ")";
    }
}
