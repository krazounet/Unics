package unics.Enum;

import java.util.EnumSet;
import java.util.Set;

public enum TriggerType {

    ON_PLAY(
        "À la pose",
        "La capacité se déclenche lorsqu’une carte est jouée depuis la main",
        0,
        EnumSet.of(CardType.ACTION, CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_ENTER(
        "À l’arrivée en jeu",
        "La capacité se déclenche lorsqu’une carte arrive sur le plateau",
        0,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_TURN_START(
        "Au début du tour",
        "La capacité se déclenche au début du tour du joueur",
        -5,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_TURN_END(//va forcément se trigger 1 fois comme les précédents, peut être deux fois 
        "À la fin du tour",
        "La capacité se déclenche à la fin du tour du joueur",
        10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_ATTACK(//moins probable car il faut avoir survécu
        "À l’attaque",
        "La capacité se déclenche lorsque la carte attaque",
        -5,
        EnumSet.of(CardType.UNIT)
    ),

    AFTER_DAMAGE(
        "Après avoir infligé des dégâts",
        "La capacité se déclenche après que cette carte a infligé des dégâts",
        -5,
        EnumSet.of(CardType.UNIT)
    ),

    ON_BEING_ATTACKED(//rien n'indique qu'elle sera attaquée sauf les structures... le cout devrait peut être est dépendant des KW
        "Lorsqu’elle est attaquée",
        "La capacité se déclenche lorsqu’une unité ennemie attaque cette carte",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    AFTER_RECEIVE_DAMAGE(//idem au dessus
        "Après que cette carte ait subi des dégâts",
        "La capacité se déclenche après que cette carte a subi des dégâts",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_DEATH(//elle peut mourrir de sa belle mort. interaction avec persisant, bouclier, fragile
        "À la destruction",
        "La capacité se déclenche lorsque la carte est retirée après avoir subi des dégâts",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_LEAVE(//interaction avec persistant
        "En quittant le plateau",
        "La capacité se déclenche lorsque la carte quitte le plateau pour une autre raison que la mort",
        0,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    ON_MOVE(//interaction avec mobile
        "Lors d’un déplacement",
        "La capacité se déclenche lorsqu’une carte change de position",
        -20,
        EnumSet.of(CardType.UNIT)
    ),
    /*
    POSITION_FRONT(
        "En position avant",
        "La capacité se déclenche uniquement si la carte est en position avant",
        -5,
        EnumSet.of(CardType.UNIT)
    ),

    POSITION_MIDDLE(
        "En position centrale",
        "La capacité se déclenche uniquement si la carte est en position milieu",
        -5,
        EnumSet.of(CardType.UNIT)
    ),

    POSITION_BACK(
        "En position arrière",
        "La capacité se déclenche uniquement si la carte est en position arrière",
        -5,
        EnumSet.of(CardType.UNIT)
    ),
	*/
    ON_ACTIVATION(
        "Activation",
        "La capacité se déclenche quand le joueur choisit de l’activer",
        0,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    KEYWORD_PRESENT(
        "Si un mot-clé est présent",
        "La capacité se déclenche si une autre carte possède un mot-clé spécifique",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),
    /*
    ALLIED_UNIT_PRESENT(
        "Si une unité alliée est en jeu",
        "La capacité se déclenche si une unité alliée spécifique est présente",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),

    NO_ENEMY_UNITS(
        "Si le plateau adverse est vide",
        "La capacité se déclenche uniquement si aucune unité ennemie n’est en jeu",
        -10,
        EnumSet.of(CardType.UNIT, CardType.STRUCTURE)
    ),
	*/
    PC_DAMAGED(
        "Quand vous perdez des PC ",
        "La capacité se déclenche lorsque les points de commandement changent",
        -10,
        EnumSet.of(CardType.UNIT)// Structure retirée, car le calcul de la puissance n'avait aucun sens.
    );
	/**
	 * ON PLAY ADJACENT
	 * ON TARGETED
	 */
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
            case KEYWORD_PRESENT -> "Activation si une de vos carte a "+kw.name();
            default -> getDisplayName();
        };
    }

    
    @Override
    public String toString() {
        return displayName + " (" + name() + ", poids=" + weight + ")";
    }
    /**
     * Liste les associations interdites entre Keyword et Trigger
     * esemple : BOUCLIER et  “quand vous perdez des PC”
     * @param keywords
     * @return
     */
	public boolean isAllowedFor(Set<Keyword> keywords) {
		return switch (this) {
		case PC_DAMAGED -> !keywords.contains(Keyword.BOUCLIER);
		default -> true;
		};
		//return true;
	}
}
