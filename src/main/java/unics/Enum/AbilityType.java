package unics.Enum;

import java.util.EnumSet;
import java.util.List;

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
        false
    ),

    DISCARD_ENEMY(
        "Défausse (ennemi)",
        "L’adversaire défausse X cartes",
        40,
        false
    ),

    // ──────────────── Dégâts / soins ────────────────

    DAMAGE_UNIT_ENEMY(
        "Dégâts unité ennemie",
        "Inflige des dégâts à une unité ciblée",
        15,
        true
    ),
    DAMAGE_UNIT_ALLY(
            "Dégâts unité amie",
            "Inflige des dégâts à une unité ciblée",
            -10,
            true
        ),

    DAMAGE_PC_SELF(
        "Dégâts PC Self",
        "Inflige des dégâts aux points de commandement",
        -10,
        false
    ),
    DAMAGE_PC_ENEMY(
            "Dégâts PC Enemy",
            "Inflige des dégâts aux points de commandement",
            25,
            false
        ),

    HEAL_PC(
        "Soin PC",
        "Restaure des points de commandement",
        20,
        false
    ),

    // ──────────────── Stats ────────────────

    BUFF(
        "Buff",
        "Augmente temporairement les statistiques d’une unité",
        15,
        true
    ),

    DEBUFF_ENEMY(
        "Debuff unité ennemie",
        "Réduit temporairement les statistiques d’une unité ennemie",
        15,
        true
    ),
    DEBUFF_ALLY(
            "Debuff unité amie",
            "Réduit temporairement les statistiques d’une unité amie",
            -10,
            true
        ),
    // ──────────────── Cartes ────────────────

    DRAW(
        "Pioche",
        "Le joueur pioche X cartes",
        15,
        false
    ),

    DESTROY_UNIT_ENEMY(
        "Destruction Structure",
        "Détruit une unité",
        60,
        true
    ),
    DESTROY_STRUCTURE_ENEMY(
            "Destruction",
            "Détruit une unité",
            30,
            true
        ),


    // ──────────────── Mouvement ────────────────

    MOVE_ALLY(
        "Déplacement allié",
        "Déplace une unité alliée",
        10,
        true
    ),

    MOVE_ENEMY(
        "Déplacement ennemi",
        "Déplace une unité ennemie",
        10,
        true
    ),
    MOVE_SELF(
    		"Déplacement",
            "Se déplace",
            5000,
            true
    		
    		
    		),

    // ──────────────── Énergie ────────────────

    ENERGY_GAIN_SELF(
        "Gain énergie (self)",
        "Le joueur gagne X énergie",
        15,
        false
    ),

    ENERGY_GAIN_ENEMY(
        "Gain énergie (ennemi)",
        "L’adversaire gagne X énergie",
        -15,
        false
    ),

    ENERGY_LOSS_SELF(
        "Perte énergie (self)",
        "Le joueur perd X énergie",
        -15,
        false
    		),
    ENERGY_LOSS_ENEMY(
        "Perte énergie (ennemi)",
        "L’adversaire perd X énergie",
        20,
        false
    ),
	//
    // ────────────────TAP_UNTAP─────────────────────
	TAP_ENEMY(
	        "Incline un ennemi",
	        "Incline un ennemi",
	        25,
	        true
	    ),
	TAP_ALLY(
	        "Incline un allié",
	        "Incline un allié",
	        -25,
	        true
	    ),
	UNTAP_ENEMY(
	        "Redresse un ennemi",
	        "Redresse un ennemi",
	        -25,
	        true
	    ),
	UNTAP_ALLY(
	        "Redresse un allié",
	        "Redresse un allié",
	        25,
	        false
	    ),
	RETURN_TO_HAND(
	        "Renvoie une carte en main",
	        "Renvoie une carte en main",
	        25,
	        true
	    );
	/**
	 * Silence
	 * reveal hand
	 * regard (scry)
	 * draw_from_discard
	 */
    private final String displayName;
    private final String description;
    private final int weight;
    private final boolean requiresTarget;


    AbilityType(
        String displayName,
        String description,
        int weight,
        boolean requiresTarget

    ) {
        this.displayName = displayName;
        this.description = description;
        this.weight = weight;
        this.requiresTarget = requiresTarget;

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



    public EnumSet<TriggerType> getAllowedTriggers() {
        return switch (this) {
            case DEBUFF_ENEMY, DEBUFF_ALLY,BUFF -> EnumSet.of(
                TriggerType.AFTER_ATTACK,
                TriggerType.AFTER_BEING_ATTACKED,  
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
    
    public EnumSet<TriggerType> getForbiddenTriggers() {
    	return switch (this) {
    	/*
    	case TAP_ENEMY,UNTAP_ENEMY,DESTROY_STRUCTURE_ENEMY,DESTROY_UNIT_ENEMY,DAMAGE_UNIT_ENEMY,DEBUFF_ENEMY,MOVE_ENEMY,RETURN_TO_HAND -> EnumSet.of(
    			TriggerType.NO_ENEMY_UNITS
    			);
    	*/
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
    public String buildText(int value, List<EffectConstraint> lec) {
    	String faction="";
    	String cost="";
    	for (EffectConstraint ec : lec) {
    		if (ec.getConstraintType()==ConstraintType.FACTION)faction=ec.getTargetAdjective();
    		if (ec.getConstraintType()==ConstraintType.COUT)cost=ec.getTargetAdjective();
    	}
        String x = value > 1 ? value + " " : value + " ";
        String effect="";
        switch (this) {

            // ──────────────── Défausse ────────────────

            case DISCARD_SELF ->
            effect="Défaussez " + x + "carte" + (value > 1 ? "s" : "");

            case DISCARD_ENEMY ->
            effect="L’adversaire défausse " + x + "carte" + (value > 1 ? "s" : "");

            // ──────────────── Dégâts / soins ────────────────

            case DAMAGE_UNIT_ENEMY ->
            effect="Inflige " + x + "dégâts à une unité "+faction+" ennemie "+cost;

            case DAMAGE_UNIT_ALLY ->
            effect="Inflige " + x + "dégâts à une unité "+faction+" alliée "+cost;

            case DAMAGE_PC_SELF ->
            effect="Vous subissez " + x + "PC";

            case DAMAGE_PC_ENEMY ->
            effect="Inflige " + x + "PC à l'ennemi";

            case HEAL_PC ->
            effect="Restaure " + x + "PC";

            // ──────────────── Stats ────────────────

            case BUFF ->
            effect= "+"+value+"/+"+value+" à une unité "+faction+" alliée "+cost;

            case DEBUFF_ENEMY ->
            effect="-"+value+"/-"+value+" à une unité "+faction+" ennemie "+cost;

            case DEBUFF_ALLY ->
            effect="-"+value+"/-"+value+" à une unité "+faction+" alliée "+cost;

            // ──────────────── Cartes ────────────────

            case DRAW ->
            effect="Piochez " + x + "carte" + (value > 1 ? "s" : "");

            case DESTROY_UNIT_ENEMY ->
            effect="Détruit "+x+" unité"+ (value > 1 ? "s" : "")+" "+ faction+" ennemie "+cost;

            case DESTROY_STRUCTURE_ENEMY ->
            effect="Détruit "+x+" structure"+(value > 1 ? "s" : "")+" "+faction+" ennemie "+cost;

            // ──────────────── Mouvement ────────────────

            case MOVE_ALLY ->
            effect="Déplace une unité "+faction+" alliée "+x+" fois "+cost;

            case MOVE_ENEMY ->
            effect= "Déplace une unité "+faction+" ennemie "+x+" fois "+cost;

            // ──────────────── Énergie ────────────────

            case ENERGY_GAIN_SELF ->
            effect="+" + x + "énergie";

            case ENERGY_GAIN_ENEMY ->
            effect="L’adversaire gagne " + x + "énergie"+(value > 1 ? "s" : "");

            case ENERGY_LOSS_SELF ->
            effect="-" + x + "énergie";

            case ENERGY_LOSS_ENEMY ->
            effect= "L’adversaire perd " + x + "énergie"+(value > 1 ? "s" : "");
                    
            case TAP_ALLY ->
            effect="Incline " + x + "unité"+(value > 1 ? "s" : "")+" "+ faction+" alliée"+(value > 1 ? "s" : "")+" "+cost;        
            case UNTAP_ALLY ->
            effect="Redresse " + x + "unité"+(value > 1 ? "s" : "")+" "+ faction+" alliée"+(value > 1 ? "s" : "")+" "+cost;  
            case TAP_ENEMY ->
            effect="Incline " + x + "unité"+(value > 1 ? "s" : "")+" "+ faction+" ennemie"+(value > 1 ? "s" : "")+" "+cost; 
            case UNTAP_ENEMY ->
            effect="Redresse " + x + "unité"+(value > 1 ? "s" : "")+" "+ faction+" ennemie"+(value > 1 ? "s" : "")+" "+cost;  
            case RETURN_TO_HAND ->
            effect="La carte "+ faction+" ciblée "+cost+" retourne en main";
            case MOVE_SELF ->
            effect="Cette carte peut se déplacer sur un autre slot";
        };
        return effect;
    }

    @Override
    public String toString() {
        return name();
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
    	case RETURN_TO_HAND	:			return 1;
		default:return 100;
			
    	
    	}
    }
    /**
    DOUBLE AVEC REQUIRE TARGET
    */
    
    public boolean isTargetingACard() {
    	return switch (this) {
    	case 	DAMAGE_UNIT_ALLY,
    			DAMAGE_UNIT_ENEMY,
    			DEBUFF_ALLY,
    			DEBUFF_ENEMY,
    			DESTROY_STRUCTURE_ENEMY,
    			DESTROY_UNIT_ENEMY,
    			MOVE_ALLY,
    			MOVE_ENEMY,
    			RETURN_TO_HAND,
    			TAP_ALLY,
    			TAP_ENEMY,
    			UNTAP_ALLY,BUFF,
    			UNTAP_ENEMY
    			-> true;
    	default -> false;
    	};
    		
    	
    }
    
}
