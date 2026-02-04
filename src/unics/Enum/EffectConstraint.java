package unics.Enum;

import java.util.ArrayList;
import java.util.List;

/**
 * contrainte a la réalisation d'un effet
 * le powermodifer est en %
 */
public enum EffectConstraint {
	/*
    // ───────── Type de carte ─────────
    UNIT("unité", 0),
    STRUCTURE("structure", 0),
	*/
    // ───────── Faction ─────────
    FACTION_ASTRAL		(ConstraintType.FACTION,"astrale", -30),
    FACTION_ORGANIC		(ConstraintType.FACTION,"organique", -30),
    FACTION_NOMAD		(ConstraintType.FACTION,"nomade", -30),
    FACTION_MECHANICAL	(ConstraintType.FACTION,"mécanique", -30),
    FACTION_OCCULT		(ConstraintType.FACTION,"occulte", -30),

    // ───────── Coût ─────────
    COST_3_OR_LESS		(ConstraintType.COUT,"de coût 3 ou moins", -20),
    COST_2_OR_LESS		(ConstraintType.COUT,"de coût 2 ou moins", -25),
    COST_1_OR_LESS		(ConstraintType.COUT,"de coût 1", -30),//a revoir si des cartes coute 0

	//───────── POSITION ─────────
	POSITION_LEFT		(ConstraintType.POSITION,"en position Gauche",-15),
	POSITION_CENTER		(ConstraintType.POSITION,"en position Centrale",-15),
	POSITION_RIGHT		(ConstraintType.POSITION,"en position Droite",-15),
	
	//───────── KW ─────────
	//KEYWORD_PRESENT("",0), à revoir
	
	//───────── PRESENCE ─────────
	ALLIED_UNIT_PRESENT	(ConstraintType.PRESENSE,"une autre unité en jeu",-10),
	ENEMY_HAS_STRUCTURE	(ConstraintType.PRESENSE,"l'adversaire a une structure en jeu",-20),
	NO_ENEMY_UNIT		(ConstraintType.PRESENSE,"l'adversaire n'a pas de carte en jeu",0),
	
	//───────── Taille de MAIN ─────────
	
	
	HAND_EMPTY(ConstraintType.HAND,"si votre main est vide",-30),
    HAND_SIZE_3_OR_LESS(ConstraintType.HAND,"si vous avez 3 cartes ou moins en main",-10);

	
    private final String displayName;
    private final int powerModifier; // ⚠️ négatif
    private final ConstraintType constraintType;

    EffectConstraint(ConstraintType ct,String displayName, int powerModifier) {
        this.displayName = displayName;
        this.powerModifier = powerModifier; // en pourcentage !
        this.constraintType = ct;
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
    
    


    public ConstraintType getConstraintType() {
		return constraintType;
	}

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
                    
            default -> this.getDisplayName();
                    
                  
        };
    }
    
    public String getPossessionText() {

        return switch (this) {

            // Factions
            case FACTION_ASTRAL ->
                    "vous avez une autre carte astrale";

            case FACTION_ORGANIC ->
                    "vous avez une autre carte organique";

            case FACTION_NOMAD ->
                    "vous avez une autre carte nomade";

            case FACTION_MECHANICAL ->
                    "vous avez une autre carte mécanique";

            case FACTION_OCCULT ->
                    "vous avez une autre carte occulte";

            // Coûts
            case COST_1_OR_LESS ->
                    "vous avez une autre carte de coût 1 ou moins";

            case COST_2_OR_LESS ->
                    "vous avez une autre carte de coût 2 ou moins";

            case COST_3_OR_LESS ->
                    "vous avez une autre carte de coût 3 ou moins";
            
            case POSITION_LEFT ->
            		"vous avez une carte à gauche";
            case POSITION_CENTER ->
            		"vous avez une carte au centre";
            case POSITION_RIGHT ->
            		"vous avez une carte à droite";
            		
            case ALLIED_UNIT_PRESENT ->
            		"une autre carte en jeu";
            case NO_ENEMY_UNIT ->
            		"l'adversaire n'a aucune carte en jeu";
            case ENEMY_HAS_STRUCTURE ->
            		"l'adversaire a une structure en jeu";
            case HAND_EMPTY ->
            		"votre main est vide";
            case HAND_SIZE_3_OR_LESS ->
            		"votre main a 3 cartes ou moins";

        };
    }
    
    public static List<EffectConstraint> getListConstraintByType (ConstraintType type){
    	List<EffectConstraint> list = new ArrayList<EffectConstraint>();
    	
    	for (EffectConstraint ec : EffectConstraint.values()) {
    		if (ec.getConstraintType() == type)list.add(ec);
    	}
    	
    	return list;
    }

	public boolean appliesToTarget() {
		return this.constraintType.applyTarget();		
	} 
    
}
