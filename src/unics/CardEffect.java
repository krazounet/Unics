package unics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.AbilityType;
import unics.Enum.CardType;
import unics.Enum.Keyword;
import unics.Enum.TargetConstraint;
import unics.Enum.TargetType;
import unics.Enum.TriggerType;

public class CardEffect {

	private final TriggerType trigger;
	private final Keyword conditionKeyword; // nullable
	
	
    private final AbilityType ability;

    // Intensité (dégâts, énergie, pioche…)
    private final Integer value; // nullable

    // Ciblage avancé
    private final TargetType targetType; // SELF, ALLY, ENEMY
    //private final CardFilter filter;      // optionnel
    //private CardFilterType filterType;
    
    private final Set<TargetConstraint> constraints;
    
    private static int MODIFIER_CONTRAINTE_NEGATIVE = -30; //exprimé en %, arbitraire pour le moment
    
    public Set<TargetConstraint> getConstraints() {
		return constraints;
	}

	public CardEffect(
            TriggerType trigger,
            Keyword conditionKeyword,
            AbilityType ability,
            int value,
            TargetType targetType,
            Set<TargetConstraint> constraints
        ) {
            this.trigger = trigger;
            this.conditionKeyword=conditionKeyword;
            this.ability = ability;
            this.value = value;
            this.targetType = targetType;
            this.constraints = constraints;
        }

    public TriggerType getTrigger() {
        return trigger;
    }

    public AbilityType getAbility() {
        return ability;
    }

    public Integer getValue() {
        return value;
    }

    public TargetType getTargetType() {
        return targetType;
    }
    
    
    public Keyword getConditionKeyword() {
		return conditionKeyword;
	}

	/**
     * Génère un effet aléatoire pour une carte donnée,
     * proportionné au coût et type de carte.
     */
    public static CardEffect generateRandomEffect(
            CardType cardType,
            int energyCost,
            ThreadLocalRandom random,
            FactionProfile profile,
            Set<AbilityType> excludedAbilities,
            Set<Keyword> keywords
    ) {
    	
    	
    	
    	
    	List<TargetConstraint> factionConstraints = List.of(
    		    TargetConstraint.FACTION_ASTRAL,
    		    TargetConstraint.FACTION_ORGANIC,
    		    TargetConstraint.FACTION_NOMAD,
    		    TargetConstraint.FACTION_MECHANICAL,
    		    TargetConstraint.FACTION_OCCULT
    		);

    		List<TargetConstraint> costConstraints = List.of(
    		    TargetConstraint.COST_1_OR_LESS,
    		    TargetConstraint.COST_2_OR_LESS,
    		    TargetConstraint.COST_3_OR_LESS
    		);
    	


        // 2️⃣ AbilityType compatible
        List<AbilityType> abilities = Arrays.stream(AbilityType.values())
                .filter(a -> isAbilityAllowedForCardType(a, cardType))//filtre les abilitype en fonction du type
                .filter(a -> !(cardType == CardType.STRUCTURE && a.isNegativeForOwner()))//les structures ont que des effets positifs
                .filter(a -> !profile.getForbiddenAbilities().contains(a))
                .filter(a -> !excludedAbilities.contains(a))
                .filter(a -> isAbilityAllowedForKeywords(a,keywords))
                .toList();

        List<AbilityType> weighted_abilities = new ArrayList<>();
        for(AbilityType at : abilities) {
        	weighted_abilities.add(at);
        	if (profile.getFavoredAbilities().contains(at)) {
        		weighted_abilities.add(at);weighted_abilities.add(at);
        	}
        }
        
        if (weighted_abilities.isEmpty()) {
            throw new IllegalStateException("Aucun abitity jouable comme condition "+cardType);
        }
        
        AbilityType ability = weighted_abilities.get(random.nextInt(weighted_abilities.size()));

        // 3️⃣ Trigger compatible
        List<TriggerType> triggers = Arrays.stream(TriggerType.values())
        	    .filter(t -> t.isAllowedFor(cardType))
        	    .filter(t -> ability.getAllowedTriggers().contains(t))
        	    .filter(t -> !ability.getForbiddenTriggers().contains(t))
        	    .filter(t -> !profile.getForbiddenTrigger().contains(t))
        	    //ici test trigger <> keyword
        	    .toList();
        if (triggers.isEmpty()) {
            throw new IllegalStateException(
                "Aucun trigger compatible pour ability " + ability + " et cardType " + cardType
            );
        }
        List<TriggerType> weighted_triggers = new ArrayList<>();
        for(TriggerType at : triggers) {
        	weighted_triggers.add(at);
        	if (profile.getFavoredTrigger().contains(at)) {
        		weighted_triggers.add(at);weighted_triggers.add(at);
        	}
        }
        
        
        TriggerType trigger = weighted_triggers.get(random.nextInt(weighted_triggers.size()));
        Keyword conditionKW = null;
        if (trigger == TriggerType.KEYWORD_PRESENT) {
        	conditionKW = Keyword.randomPlayable(random);
        }
        // 4️⃣ TargetType
        TargetType targetType = switch (ability) {
            case BUFF, MOVE_ALLY,TAP_ALLY,UNTAP_ALLY -> TargetType.ALLY;
            case 	DAMAGE_UNIT_ENEMY, MOVE_ENEMY, DEBUFF_ENEMY,ENERGY_LOSS_ENEMY,
            		ENERGY_GAIN_ENEMY,DISCARD_ENEMY,DESTROY_STRUCTURE_ENEMY,
            		DESTROY_UNIT_ENEMY,TAP_ENEMY,UNTAP_ENEMY -> TargetType.ENEMY;
            default -> TargetType.SELF;
        };

        // 5️⃣ Valeur X
        int maxValue;

        if (cardType == CardType.ACTION) {
            maxValue = energyCost;   // ACTION = impact fort
        } else {
            maxValue = energyCost / 2 + 1;
        }
        int value = random.nextInt(1, maxValue + 1);

        /**
         * Value doit etre abaissée dans certains cas : 
         * - il ne sert a rien de buter 5 ennemy unit
         * - il ne sert a rien de buter 3 structures
         * - il ne sert a rien de faire 17 mouvements
         */
        value = Math.min(value, ability.maxValue());
        // 6️⃣ Contraintes
     // Contraintes obligatoires (ex : UNIT / STRUCTURE)
        Set<TargetConstraint> constraints = new HashSet<>();
        //constraints.addAll(ability.getMandatoryConstraints());

   
        

        int roll = random.nextInt(100);

        boolean useFaction = false;
        boolean useCost = false;

        if (roll < 60) {
            // 0 contrainte
        } else if (roll < 95) {
            // 1 contrainte
            useFaction = random.nextBoolean();
            useCost = !useFaction;
        } else {
            // 2 contraintes (rare)
            if (energyCost >= 5) {
                useFaction = true;
                useCost = true;
            } else {
                useFaction = random.nextBoolean();
                useCost = !useFaction;
            }
        }
        if (useFaction) {
            TargetConstraint faction =
                factionConstraints.get(random.nextInt(factionConstraints.size()));
            constraints.add(faction);
        }

        if (useCost) {
            TargetConstraint cost =
                costConstraints.get(random.nextInt(costConstraints.size()));
            constraints.add(cost);
        }
        
        


        return new CardEffect(
                trigger,
                conditionKW,
                ability,
                value,
                targetType,
                constraints
        );
    }

    private static boolean isAbilityAllowedForKeywords(AbilityType a, Set<Keyword> keywords) {
		for (Keyword kw : keywords) {
			if (kw.isForbiddenAbility(a)) {return false;}
		}
		return true;
	}

	private static boolean isAbilityAllowedForCardType(
            AbilityType ability,
            CardType cardType
    ) {
        return switch (cardType) {
            case UNIT -> true;
            case STRUCTURE -> 	ability != AbilityType.MOVE_ENEMY
                    			&& ability != AbilityType.MOVE_ALLY;
            //les cartes actions ne font pas de trucs négatifs pour le joueur en cours
            case ACTION -> ability != AbilityType.BUFF
                    && ability != AbilityType.DEBUFF_ALLY && !ability.isNegativeForOwner();
            default -> false;
        };
    }

    @Override
    public String toString() {
        return "Effect{" +
                "trigger=" + trigger +
                ", ability=" + ability +
                ", value=" + value +
                ", target=" + targetType +
                ", constraints=" + constraints +
                '}';
    }
    
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("("+this.computeRawPower()+":"+this.getTrigger().getWeight()+"/"+this.getAbility().getWeight()+"/"+this.getValue()+")");
        // Trigger
        
        
        
        sb.append(trigger.getShortDisplay(conditionKeyword)).append(" : ");
        // Texte principal
        String text = ability.buildText(value);

        boolean targetsCard =
                ability.requiresTarget()
                && (targetType == TargetType.ALLY || targetType == TargetType.ENEMY);

        if (constraints != null && !constraints.isEmpty()) {
            if (targetsCard) {
                text = applyTargetConstraints(text, constraints);
            } else {
                text += " si " + buildPossessionCondition(constraints);
            }
        }

        sb.append(text);
        return sb.toString().trim();
    }
    /*
     * L'effet sans le trigger
     */
    public String getEffectText() {
        StringBuilder sb = new StringBuilder();
        sb.append(": ("+(int)Math.round(this.computeRawPower())+") ");
        
        //A conserver pour debug puissance d'un Effect
        //sb.append("("+(int)Math.round(this.computeRawPower())+":"+this.getTrigger().getWeight()+"/"+this.getAbility().getWeight()+"/"+this.getValue()+"/"+this.getConstraints().size());
        

        String text = ability.buildText(value);

        boolean targetsCard =
                ability.requiresTarget()
                && (targetType == TargetType.ALLY || targetType == TargetType.ENEMY);

        if (constraints != null && !constraints.isEmpty()) {
            if (targetsCard) {
                text = applyTargetConstraints(text, constraints);
            } else {
                text += " si " + buildPossessionCondition(constraints);
            }
        }

        sb.append(text);
        return sb.toString().trim();
    }
    private String applyTargetConstraints(String base, Set<TargetConstraint> constraints) {

        StringBuilder sb = new StringBuilder(base);

        sb.append(" ");

        // ex: "à une unité ennemie"
        // → enrichir avec adjectifs
        for (TargetConstraint c : constraints) {
            sb.append(c.getTargetAdjective()).append(" ");
        }

        return sb.toString().trim();
    }
    private String buildPossessionCondition(Set<TargetConstraint> constraints) {

        return constraints.stream()
                .map(c -> "vous avez " + c.getPossessionText())
                .reduce((a, b) -> a + " et " + b)
                .orElse("");
    }


    
   
    /**
     * calcul brute de la puissance
     * @return
     */
    public double computeRawPower() {
    	int trigger_weight = trigger.getWeight();
    	if (ability.isNegativeForOwner()) trigger_weight =-trigger_weight;
        double raw =
        		trigger_weight
              + (ability.getWeight()*getValue());
              //* (value != null ? value : 1));

        if (ability.isNegativeForOwner() && raw >0)raw=0; //protection contre un effet négatif qui couerait des stats
        
        double modifier = 100;//pour 100%
        for (TargetConstraint c : getConstraints()) {
        	
            modifier += c.getPowerModifier(); // ex: -15
            if (ability.requiresTarget() && targetType == TargetType.ENEMY) {//cible une carte ennemy
            	modifier+=MODIFIER_CONTRAINTE_NEGATIVE;//valeur arbitraire temporaire
            }
            if (ability.isNegativeForOwner()) modifier+=MODIFIER_CONTRAINTE_NEGATIVE;//valeur arbitraire temporaire
        }
        modifier =modifier/100;
        

        return (raw * modifier);
    }
    /**
     * sert à l'unicité des effects et pour le hash
     * @return
     */
    public String toIdentityString() {

        String constraintsPart = constraints == null
            ? ""
            : constraints.stream() 
                .map(Enum::name)
                .sorted()
                .collect(java.util.stream.Collectors.joining(","));

        return String.join(":",
            trigger.name(),
            conditionKeyword != null ? conditionKeyword.name() : "NONE",
            ability.name(),
            value != null ? value.toString() : "0",
            targetType.name(),
            constraintsPart
        );
    }
    
}
