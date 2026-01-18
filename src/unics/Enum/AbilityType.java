package unics.Enum;

import java.util.EnumSet;

/**
 * Enum représentant le type d'effet d'une capacité.
 * Décrit CE QUE fait la capacité, indépendamment du déclencheur.
 */
public enum AbilityType {

    // ──────────────── Défausse ────────────────

    DISCARD_SELF(
        "Défausse (self)",
        "Le joueur défausse X cartes",
        -15,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    DISCARD_ENEMY(
        "Défausse (ennemi)",
        "L’adversaire défausse X cartes",
        40,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    // ──────────────── Dégâts / soins ────────────────

    DAMAGE_UNIT_ENEMY(
        "Dégâts unité ennemie",
        "Inflige des dégâts à une unité ciblée",
        15,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),
    DAMAGE_UNIT_ALLY(
            "Dégâts unité amie",
            "Inflige des dégâts à une unité ciblée",
            -10,
            true,
            EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
        ),

    DAMAGE_PC_SELF(
        "Dégâts PC Self",
        "Inflige des dégâts aux points de commandement",
        -10,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),
    DAMAGE_PC_ENEMY(
            "Dégâts PC Enemy",
            "Inflige des dégâts aux points de commandement",
            25,
            false,
            EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
            
        ),

    HEAL_PC(
        "Soin PC",
        "Restaure des points de commandement",
        20,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    // ──────────────── Stats ────────────────

    BUFF(
        "Buff",
        "Augmente temporairement les statistiques d’une unité",
        15,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    DEBUFF_ENEMY(
        "Debuff unité ennemie",
        "Réduit temporairement les statistiques d’une unité ennemie",
        15,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),
    DEBUFF_ALLY(
            "Debuff unité amie",
            "Réduit temporairement les statistiques d’une unité amie",
            -10,
            true,
            EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
        ),
    // ──────────────── Cartes ────────────────

    DRAW(
        "Pioche",
        "Le joueur pioche X cartes",
        15,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    DESTROY_UNIT_ENEMY(
        "Destruction Structure",
        "Détruit une unité",
        60,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),
    DESTROY_STRUCTURE_ENEMY(
            "Destruction",
            "Détruit une unité",
            30,
            true,
            EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
        ),


    // ──────────────── Mouvement ────────────────

    MOVE_ALLY(
        "Déplacement allié",
        "Déplace une unité alliée",
        10,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    MOVE_ENEMY(
        "Déplacement ennemi",
        "Déplace une unité ennemie",
        10,
        true,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    // ──────────────── Énergie ────────────────

    ENERGY_GAIN_SELF(
        "Gain énergie (self)",
        "Le joueur gagne X énergie",
        15,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    ENERGY_GAIN_ENEMY(
        "Gain énergie (ennemi)",
        "L’adversaire gagne X énergie",
        -15,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    ENERGY_LOSS_SELF(
        "Perte énergie (self)",
        "Le joueur perd X énergie",
        -15,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),

    ENERGY_LOSS_ENEMY(
        "Perte énergie (ennemi)",
        "L’adversaire perd X énergie",
        20,
        false,
        EnumSet.allOf(TargetConstraint.class),
        EnumSet.noneOf(TargetConstraint.class)
    ),
	//
    // ────────────────TAP_UNTAP─────────────────────
	TAP_ENEMY(
	        "Incline un ennemi",
	        "Incline un ennemi",
	        25,
	        true,
	        EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
	    ),
	TAP_ALLY(
	        "Incline un allié",
	        "Incline un allié",
	        -25,
	        true,
	        EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
	    ),
	UNTAP_ENEMY(
	        "Redresse un ennemi",
	        "Redresse un ennemi",
	        -25,
	        true,
	        EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
	    ),
	UNTAP_ALLY(
	        "Redresse un allié",
	        "Redresse un allié",
	        25,
	        false,
	        EnumSet.allOf(TargetConstraint.class),
            EnumSet.noneOf(TargetConstraint.class)
	    );
    private final String displayName;
    private final String description;
    private final int weight;
    private final boolean requiresTarget;
    private final EnumSet<TargetConstraint> mandatoryConstraints;
    private final EnumSet<TargetConstraint> forbiddenConstraints;

    AbilityType(
        String displayName,
        String description,
        int weight,
        boolean requiresTarget,
        EnumSet<TargetConstraint> mandatoryConstraints,
        EnumSet<TargetConstraint> forbiddenConstraints
    ) {
        this.displayName = displayName;
        this.description = description;
        this.weight = weight;
        this.requiresTarget = requiresTarget;
        this.mandatoryConstraints = mandatoryConstraints;
        this.forbiddenConstraints = forbiddenConstraints;
    }

    // ──────────────── Getters ────────────────

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public boolean requiresTarget() {
        return requiresTarget;
    }

    public EnumSet<TargetConstraint> getMandatoryConstraints() {
        return mandatoryConstraints;
    }

    public EnumSet<TargetConstraint> getForbiddenConstraints() {
        return forbiddenConstraints;
    }

    public EnumSet<TriggerType> getAllowedTriggers(CardType cardType) {
        return switch (this) {
            case DEBUFF_ENEMY, DEBUFF_ALLY,BUFF -> EnumSet.of(
                TriggerType.ON_ATTACK,
                TriggerType.ON_BEING_ATTACKED,  
                TriggerType.ON_ENTER,
                TriggerType.ON_ACTIVATION,
                TriggerType.AFTER_DAMAGE,
                TriggerType.ON_TURN_START,
                TriggerType.ON_PLAY
            );
            //case BUFF -> EnumSet.of();
            default -> EnumSet.allOf(TriggerType.class);
        };
    }
    
    public EnumSet<TriggerType> getForbiddenTriggers(CardType cardType) {
    	return switch (this) {
    	case TAP_ENEMY,UNTAP_ENEMY,DESTROY_STRUCTURE_ENEMY,DESTROY_UNIT_ENEMY,DAMAGE_UNIT_ENEMY,DEBUFF_ENEMY,MOVE_ENEMY -> EnumSet.of(
    			TriggerType.NO_ENEMY_UNITS
    			);
    	default -> EnumSet.noneOf(TriggerType.class);
    	};
    }
    
    
    public boolean isNegativeForOwner() {
        return switch (this) {
            case DEBUFF_ALLY,
                 ENERGY_LOSS_SELF,
                 DISCARD_SELF,
                 DAMAGE_UNIT_ALLY,
                 TAP_ALLY,
                 UNTAP_ENEMY,
                 DAMAGE_PC_SELF,
                 ENERGY_GAIN_ENEMY
                 -> true;

            default -> false;
        };
    }
    public String buildText(int value) {

        String x = value > 1 ? value + " " : value + " ";

        return switch (this) {

            // ──────────────── Défausse ────────────────

            case DISCARD_SELF ->
                    "Défaussez " + x + "carte" + (value > 1 ? "s" : "");

            case DISCARD_ENEMY ->
                    "L’adversaire défausse " + x + "carte" + (value > 1 ? "s" : "");

            // ──────────────── Dégâts / soins ────────────────

            case DAMAGE_UNIT_ENEMY ->
                    "Inflige " + x + "dégâts à une unité ennemie";

            case DAMAGE_UNIT_ALLY ->
                    "Inflige " + x + "dégâts à une unité alliée";

            case DAMAGE_PC_SELF ->
                    "Vous subissez " + x + "PC";

            case DAMAGE_PC_ENEMY ->
                    "Inflige " + x + "PC à l'ennemi";

            case HEAL_PC ->
                    "Restaure " + x + "PC";

            // ──────────────── Stats ────────────────

            case BUFF ->
                    "+"+value+"/+"+value+" à une unité alliée";

            case DEBUFF_ENEMY ->
                    "-"+value+"/-"+value+" à une unité ennemie";

            case DEBUFF_ALLY ->
            		"-"+value+"/-"+value+" à une unité alliée";

            // ──────────────── Cartes ────────────────

            case DRAW ->
                    "Piochez " + x + "carte" + (value > 1 ? "s" : "");

            case DESTROY_UNIT_ENEMY ->
                    "Détruit "+x+" unité"+ (value > 1 ? "s" : "")+ " ennemie";

            case DESTROY_STRUCTURE_ENEMY ->
                    "Détruit "+x+" structure"+(value > 1 ? "s" : "")+ " ennemie";

            // ──────────────── Mouvement ────────────────

            case MOVE_ALLY ->
                    "Déplace une unité alliée "+x+" fois";

            case MOVE_ENEMY ->
                    "Déplace une unité ennemie "+x+" fois";

            // ──────────────── Énergie ────────────────

            case ENERGY_GAIN_SELF ->
                    "+" + x + "énergie";

            case ENERGY_GAIN_ENEMY ->
                    "L’adversaire gagne " + x + "énergie"+(value > 1 ? "s" : "");

            case ENERGY_LOSS_SELF ->
                    "-" + x + "énergie";

            case ENERGY_LOSS_ENEMY ->
                    "L’adversaire perd " + x + "énergie"+(value > 1 ? "s" : "");
                    
            case TAP_ALLY ->
            	"Incline " + x + "unité"+(value > 1 ? "s" : "")+" alliée"+(value > 1 ? "s" : "");        
            case UNTAP_ALLY ->
            "Redresse " + x + "unité"+(value > 1 ? "s" : "")+" alliée"+(value > 1 ? "s" : "");  
            case TAP_ENEMY ->
           	"Incline " + x + "unité"+(value > 1 ? "s" : "")+" ennemie"+(value > 1 ? "s" : ""); 
            case UNTAP_ENEMY ->
            "Redresse " + x + "unité"+(value > 1 ? "s" : "")+" ennemie"+(value > 1 ? "s" : "");  
        };
    }

    @Override
    public String toString() {
        return displayName + " (" + name() + ", poids=" + weight + ")";
    }
    
    public int maxValue() {
    	switch(this){
    	case DESTROY_UNIT_ENEMY : 		return 3;
    	case DESTROY_STRUCTURE_ENEMY : 	return 2;
    	case MOVE_ALLY :				return 2;
    	case MOVE_ENEMY : 				return 2;
    	case TAP_ALLY :					return 2;
    	case UNTAP_ALLY :				return 2;
    	case TAP_ENEMY :				return 2;
    	case UNTAP_ENEMY :				return 2;
    	
		default:return 100;
			
    	
    	}
    }
}
