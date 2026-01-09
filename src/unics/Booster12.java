package unics;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.CardType;

public class Booster12 extends Booster {
	 static int NB_UNIT_MIN = 8;
	 static int NB_STRUCTURE_MIN = 1;
	    
	    
	public Booster12(ThreadLocalRandom random) {
		super(random);
		
		manacurve = new BoosterManaCurve(12).getCurve();
		
		cards= new ArrayList<>();
		cards.addAll(generateCards(CardType.UNIT, NB_UNIT_MIN));
		cards.addAll(generateCards(CardType.STRUCTURE, NB_STRUCTURE_MIN));
    	for (int i = 0; i < 3; i++) {
    	    CardType type = random.nextInt(100) < 33 ? CardType.UNIT
    	                    : random.nextInt(100) < 66 ? CardType.ACTION
    	                    : CardType.STRUCTURE;

    	    cards.addAll(generateCards(type,1));
		
	}
	}
	
}
