package unics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.ManaCurveProfile;

public abstract class Booster {

	private final UUID id;
	private final String publicId;
	
    protected List<Card> cards;
    protected List<Integer> manacurve;

    ThreadLocalRandom random;
    

    protected ManaCurveProfile manaCurveProfile;
    protected List<Integer> manaCurve;

	public ManaCurveProfile getManaCurveProfile() {
		return manaCurveProfile;
	}

	public Booster(ThreadLocalRandom random) {
		super();
		this.id = UUID.randomUUID();
		publicId = PublicIdGenerator.fromUuid(id);
		this.random=random;
		this.manaCurveProfile = ManaCurveProfile.random();
		cards= new ArrayList<>();
		
	
	}

	protected List<Card> generateCards(CardType type, int count) {
    	int index_cost=0;
        List<Card> cards = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
        	int cost=manacurve.get(index_cost);
        	Faction f = Faction.randomFaction();
            cards.add(CardGenerator.generateValidatedCard(type,cost,f,random));
            index_cost++;
        }
        return cards;
    }

    
    
	public List<Card> getCards() {
		return cards;
	}

	public String getPublicId() {
		return publicId;
	}
    
	 public void generate() {}
    
}
