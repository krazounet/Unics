package unics;

import java.util.Set;

import unics.Enum.AbilityType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.Enum.TriggerType;

@SuppressWarnings("unused")
public final class FactionProfile {

    private final Faction faction;

    
	//private final int statBias;        // + = favorise stats
    private final int effectBias;      // + = favorise effets
    private final int keywordBias;     // + = favorise keywords

    private final Set<Keyword> favoredKeywords;
    private final Set<Keyword> forbiddenKeywords;

    private final Set<TriggerType> favoredTrigger;
    private final Set<TriggerType> forbiddenTrigger;
    
    
    private final Set<AbilityType> favoredAbilities;
    private final Set<AbilityType> forbiddenAbilities;

    public FactionProfile(
            Faction faction,
 
            int effectBias,
            int keywordBias,
            Set<Keyword> favoredKeywords,
            Set<Keyword> forbiddenKeywords,
            
            Set<TriggerType> favoredTrigger,
            Set<TriggerType> forbiddenTrigger,
            
            Set<AbilityType> favoredAbilities,
            Set<AbilityType> forbiddenAbilities
    ) {
        this.faction = faction;
      //  this.statBias = statBias;
        this.effectBias = effectBias;
        this.keywordBias = keywordBias;
        this.favoredKeywords = favoredKeywords;
        this.forbiddenKeywords = forbiddenKeywords;
		this.favoredTrigger = favoredTrigger;
		this.forbiddenTrigger = forbiddenTrigger;
        this.favoredAbilities = favoredAbilities;
        this.forbiddenAbilities = forbiddenAbilities;
    }

	public Faction getFaction() {
		return faction;
	}

	public int getEffectBias() {
		return effectBias;
	}

	public int getKeywordBias() {
		return keywordBias;
	}

	public Set<Keyword> getFavoredKeywords() {
		return favoredKeywords;
	}

	public Set<Keyword> getForbiddenKeywords() {
		return forbiddenKeywords;
	}

	public Set<TriggerType> getFavoredTrigger() {
		return favoredTrigger;
	}

	public Set<TriggerType> getForbiddenTrigger() {
		return forbiddenTrigger;
	}

	public Set<AbilityType> getFavoredAbilities() {
		return favoredAbilities;
	}

	public Set<AbilityType> getForbiddenAbilities() {
		return forbiddenAbilities;
	}

    // Getters
    
}
