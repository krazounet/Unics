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
    
    static int NB_ESSAI_CARTE=50;
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
            cards.add(generateValidatedCard(type,cost));
            index_cost++;
        }
        return cards;
    }
    
    private Card generateValidatedCard(CardType type, int cost) {
        //Card bestCard = null;
        for (int i = 0; i < NB_ESSAI_CARTE; i++) {
            Card candidate = CardGenerator.generateCard(type,cost,random);//ici agir sur le cout
            if (CardGenerator.isValid(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Impossible de générer une carte valide "+type+ "/"+cost);
    }
    protected Card generateValidatedCard(CardType type, int cost,Faction faction) {
        //Card bestCard = null;
        for (int i = 0; i < NB_ESSAI_CARTE; i++) {
            Card candidate = CardGenerator.generateCard(type,cost,random,faction);//ici agir sur le cout
            if (CardGenerator.isValid(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Impossible de générer une carte valide "+type+ "/"+cost);
    }
    
    
	public List<Card> getCards() {
		return cards;
	}

	public String getPublicId() {
		return publicId;
	}
    
    
    
}
