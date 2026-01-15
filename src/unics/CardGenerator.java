package unics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.AbilityType;
import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;

public abstract class CardGenerator {

	private static int BUDGET_BASE=36;
	private static int BUDGET_PER_ENERGY=24;
	
	private static int PCT_0_KEYWORD=15;
	private static int PCT_1_KEYWORD=70;
	private static int PCT_2_KEYWORD=10;
	//private static int PCT_3_KEYWORD=5;
	
	private static int PCT_0_EFFECT=10;
	private static int PCT_1_EFFECT=80;
	//private static int PCT_2_EFFECT=10;
	
	private static int SEUIL_BUDGET_RESTANT = 20;
	private static int NB_MAX_EFFECT_ACTION = 4;
	
	private static int COUT_ATK = 12;
	private static int COUT_DEF = 8;
	
	private static int INITIAL_ATK = 0;
	private static int INITIAL_DEF = 1;
	
	private static int SEUIL_CARTE_VALIDE_BAS = 90; //en pourcentage
	private static int SEUIL_CARTE_VALIDE_HAUT = 110; //en pourcentage

    public CardGenerator(
  
    ) {
  
    }

    
    public static Card generateCard(CardType type, int energyCost, ThreadLocalRandom random) {
    	Faction faction = randomFaction();
    	return generateCard(type,energyCost,random,faction);
    }
    
    public static Card generateCard(CardType type, int energyCost, ThreadLocalRandom random, Faction faction) {

        int initialBudget = BUDGET_BASE + BUDGET_PER_ENERGY * energyCost;
        int remainingBudget = initialBudget;
        //int depense=0;


        FactionProfile profile = FactionProfileRegistry.get(faction);
        
        // 1️⃣ Keywords
        Set<Keyword> keywords = new HashSet<>();

        if (type != CardType.ACTION) {
            int roll_kw = random.nextInt(100)+profile.getKeywordBias();

            int keywordCount =
                    roll_kw < PCT_0_KEYWORD ? 0 :
                    roll_kw < (PCT_0_KEYWORD+PCT_1_KEYWORD) ? 1 :
                    roll_kw < (PCT_0_KEYWORD+PCT_1_KEYWORD+PCT_2_KEYWORD) ? 2 : 3;

            for (int i = 0; i < keywordCount; i++) {
                Keyword kw = Keyword.randomPlayable(random, type,profile);

                keywords.add(kw);//ajout du keyword sans toucher au budget
            }
            
            if((type == CardType.STRUCTURE)&&(!keywords.contains(Keyword.DEFENSIF))){
            	 keywords.add(Keyword.DEFENSIF);
            }
        }

        // 2️⃣ Effets
        List<CardEffect> effects = new ArrayList<>();
        Set<AbilityType> usedAbilities = new HashSet<>();
        switch (type) {

            case ACTION -> {
                // Toute la valeur passe dans les effets
                while (remainingBudget > SEUIL_BUDGET_RESTANT && effects.size() < NB_MAX_EFFECT_ACTION) {
                    CardEffect e = CardEffect.generateRandomEffect(type, energyCost, random,profile,usedAbilities);
                    //double power = Math.abs(e.computeRelativePower(energyCost));
                    double power = Math.abs(e.computeRawPower());
                    if (power <= remainingBudget) {
                        effects.add(e);
                        usedAbilities.add(e.getAbility());
                        remainingBudget -= power;
                    } else {
                        break;
                    }
                }
            }

            case UNIT,STRUCTURE -> {
                // 90 % de chance d'avoir un effet 10% d'en avoir 2
            	
            	int roll_effect = random.nextInt(100)+profile.getEffectBias();
            	int effecCount =
            			roll_effect < PCT_0_EFFECT ? 0 :
            			roll_effect < (PCT_0_EFFECT+PCT_1_EFFECT) ? 1 : 2;
            			
                for (int i=0;i<effecCount;i++) {
                    CardEffect e = CardEffect.generateRandomEffect(type, energyCost, random,profile,usedAbilities);
                    //double power = Math.abs(e.computeRelativePower(energyCost));
                    double power = Math.abs(e.computeRawPower());
                    if (power <= remainingBudget) {
                        effects.add(e);
                        usedAbilities.add(e.getAbility());
                        remainingBudget -= power;
                    }
                }
            }
   
            default -> throw new IllegalStateException("Type non géré : " + type);
        }

        // 3️⃣ Stats
        Integer attack = INITIAL_ATK;
        Integer defense = INITIAL_DEF;
        if (type == CardType.UNIT) {//eviter la règle si une carte peut attaquer en ayant 0 de force
        	attack++;
        	remainingBudget=initialBudget-calculBudgetSpent(attack,defense,keywords,effects,energyCost);
        }
        	
        if (type == CardType.UNIT || type == CardType.STRUCTURE) {

            // Défense minimale vitale
            
            //remainingBudget -= COUT_DEF*defense;
            
            remainingBudget=initialBudget-calculBudgetSpent(attack,defense,keywords,effects,energyCost);
            if (type == CardType.STRUCTURE) {
                // 🧱 STRUCTURE → DEF uniquement
                while (remainingBudget >= COUT_DEF) {
                    defense++;
                    remainingBudget=initialBudget-calculBudgetSpent(attack,defense,keywords,effects,energyCost);
                    
                }
            } else {
                // ⚔️ UNIT → ATK / DEF
                while (remainingBudget >= COUT_DEF) {
                    if (remainingBudget >= COUT_ATK && random.nextBoolean()) {
                        attack++;
                        remainingBudget=initialBudget-calculBudgetSpent(attack,defense,keywords,effects,energyCost);
                    } else {
                        defense++;
                        remainingBudget=initialBudget-calculBudgetSpent(attack,defense,keywords,effects,energyCost);
                    }
                }
            }
        }

        // 4️⃣ PowerScore (% d'utilisation du budget)
        int spent = initialBudget - remainingBudget;
        int powerScore = (int) Math.round((spent * 100.0) / initialBudget);

        // 5️⃣ Build final
        return new CardBuilder()
                .withFaction(faction)
                .withType(type)
                .withEnergyCost(energyCost)
                .withKeywords(keywords)
                .withEffects(effects)
                .withAttack(attack)
                .withDefense(defense)
                .withPowerScore(powerScore)
                .build();
    }

    private static int calculBudgetSpent(Integer attack, Integer defense, Set<Keyword> keywords, List<CardEffect> effects, int energyCost) {
		int depense=0;
		depense += attack*COUT_ATK;
		depense += defense*COUT_DEF;
		for(Keyword k : keywords) {
			depense +=k.computeWeight(attack,defense,energyCost);
		}
		for(CardEffect e : effects) {
			//depense +=e.computeRelativePower(energyCost);
			depense +=e.computeRawPower();
		}
		return depense;
	}




	private static Faction randomFaction() {
        Faction[] values = Faction.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

	
	
	public static boolean isValid(Card card) {

        // PowerScore
        if (card.getPowerScore() < SEUIL_CARTE_VALIDE_BAS || card.getPowerScore() > SEUIL_CARTE_VALIDE_HAUT) {
            return false;
        }

        // Action
        if (card.getCardType() == CardType.ACTION) {
            if (card.getEffects().isEmpty()) return false;
        }

        // Unit
        if (card.getCardType() == CardType.UNIT) {
            if (card.getDefense() < 1) return false;
            if (card.getAttack() + card.getDefense() < 2) return false;
        }

        // Structure
        if (card.getCardType() == CardType.STRUCTURE) {
            if (card.getDefense() < 2) return false;
        }

        // Keywords absurdes
        if (card.getCardType() == CardType.ACTION && !card.getKeywords().isEmpty()) {
            return false;
        }

        return true;
    }
	
}

