package unics.Enum;

import java.util.EnumSet;

public enum TriggerType {

    ON_PLAY(
        "À la pose",
        "La capacité se déclenche lorsqu’une carte est jouée depuis la main",
        20,
        EnumSet.of(CardType.ACTION, CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_ENTER(
        "À l’arrivée en jeu",
        "La capacité se déclenche lorsqu’une carte arrive sur le plateau",
        20,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_TURN_START(
        "Au début du tour",
        "La capacité se déclenche au début du tour du joueur",
        15,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_TURN_END(
        "À la fin du tour",
        "La capacité se déclenche à la fin du tour du joueur",
        15,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_ATTACK(
        "À l’attaque",
        "La capacité se déclenche lorsque la carte attaque",
        18,
        EnumSet.of(CardType.UNIT)
    ),

    AFTER_DAMAGE(
        "Après avoir infligé des dégâts",
        "La capacité se déclenche après que cette carte a infligé des dégâts",
        15,
        EnumSet.of(CardType.UNIT)
    ),

    ON_BEING_ATTACKED(
        "Lorsqu’elle est attaquée",
        "La capacité se déclenche lorsqu’une unité ennemie attaque cette carte",
        15,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    AFTER_RECEIVE_DAMAGE(
        "Après avoir subi des dégâts",
        "La capacité se déclenche après que cette carte a subi des dégâts",
        15,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_DEATH(
        "À la destruction",
        "La capacité se déclenche lorsque la carte est retirée après avoir subi des dégâts",
        18,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_LEAVE(
        "En quittant le plateau",
        "La capacité se déclenche lorsque la carte quitte le plateau pour une autre raison que la mort",
        10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_MOVE(
        "Lors d’un déplacement",
        "La capacité se déclenche lorsqu’une carte change de position",
        10,
        EnumSet.of(CardType.UNIT)
    ),

    POSITION_FRONT(
        "En position avant",
        "La capacité se déclenche uniquement si la carte est en position avant",
        10,
        EnumSet.of(CardType.UNIT)
    ),

    POSITION_MIDDLE(
        "En position centrale",
        "La capacité se déclenche uniquement si la carte est en position milieu",
        8,
        EnumSet.of(CardType.UNIT)
    ),

    POSITION_BACK(
        "En position arrière",
        "La capacité se déclenche uniquement si la carte est en position arrière",
        8,
        EnumSet.of(CardType.UNIT)
    ),

    ON_ACTIVATION(
        "Activation",
        "La capacité se déclenche quand le joueur choisit de l’activer",
        20,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    KEYWORD_PRESENT(
        "Si un mot-clé est présent",
        "La capacité se déclenche si une autre carte possède un mot-clé spécifique",
        12,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ALLIED_UNIT_PRESENT(
        "Si une unité alliée est en jeu",
        "La capacité se déclenche si une unité alliée spécifique est présente",
        12,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    NO_ENEMY_UNITS(
        "Si le plateau adverse est vide",
        "La capacité se déclenche uniquement si aucune unité ennemie n’est en jeu",
        10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    PC_DAMAGED(
        "Quand vous perdez des PC ",
        "La capacité se déclenche lorsque les points de commandement changent",
        15,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    );

    // ──────────────────────────────

    private final String displayName;   // Pour l'affichage carte
    private final String description;   // Pour règles / debug
    private final int weight;            // Pour générateur / équilibre
    private final EnumSet<CardType> allowedSourceTypes;
    
    TriggerType(String displayName, String description, int weight,EnumSet<CardType> allowedSourceTypes) {
        this.displayName = displayName;
        this.description = description;
        this.weight = weight;
        this.allowedSourceTypes = allowedSourceTypes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }
    public boolean isAllowedFor(CardType sourceType) {
        return allowedSourceTypes.contains(sourceType);
    }
    public String getShortDisplay(Keyword kw) {
        return switch (this) {
            case ON_PLAY -> "Quand cette carte est jouée";
            case ON_ENTER -> "Quand cette carte arrive en jeu";
            case ON_ATTACK -> "Quand cette carte attaque";
            case ON_BEING_ATTACKED -> "Quand cette carte est attaquée";
            case ON_DEATH -> "Quand cette carte meurt";
            case ON_TURN_START -> "Au début de votre tour";
            case ON_TURN_END -> "À la fin de votre tour";
            case ON_MOVE -> "Quand cette carte change de position";
            case KEYWORD_PRESENT -> "Si une de vos carte a "+kw.name();
            default -> getDisplayName();
        };
    }

    
    @Override
    public String toString() {
        return displayName + " (" + name() + ", poids=" + weight + ")";
    }
}
