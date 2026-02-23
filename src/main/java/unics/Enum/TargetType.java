package unics.Enum;

/**
 * Enum représentant la cible d'une capacité.
 * Décrit QUI est affecté par l'effet.
 */
public enum TargetType {

    SELF(
        "Soi-même",
        "La capacité cible uniquement cette carte",
        false
    ),

    ALLY(
        "Allié",
        "La capacité cible une carte alliée",
        true
    ),

    ENEMY(
        "Ennemi",
        "La capacité cible une carte ennemie",
        true
    ),

    ALL(
        "Toutes les cartes",
        "La capacité affecte toutes les cartes en jeu",
        false
    );

    private final String displayName;     // Affichage carte
    private final String description;     // Règles / debug
    private final boolean requiresChoice; // Le joueur doit choisir une cible


    TargetType(String displayName, String description, boolean requiresChoice) {
        this.displayName = displayName;
        this.description = description;
        this.requiresChoice = requiresChoice;

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

 

    /**
     * Utilisé pour debug / logs
     */
    @Override
    public String toString() {
        return name();
    }
}
