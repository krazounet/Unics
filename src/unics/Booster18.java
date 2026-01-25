package unics;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.CardType;
import unics.Enum.Faction;

public class Booster18 extends Booster {

	 static int NB_UNIT_MIN = 10;
	 static int NB_STRUCTURE_MIN = 3;
	 static int NB_ACTION_MIN = 2;
	 int index_booster=0;
	 List<Faction> factions =
		        FactionDistribution.generate(List.of(12, 6));
	
	public Booster18(ThreadLocalRandom random) {
		super(random);
		manacurve = new BoosterManaCurve(18,manaCurveProfile).getCurve();
		
		
		
		
	}
	@Override
	 public void generate() {
		 for (int i=0; i<NB_UNIT_MIN;i++) {
				cards.add(CardGenerator.generateValidatedCard(CardType.UNIT,manacurve.get(index_booster),factions.get(index_booster),random));
				index_booster++;
			}
			for (int i=0; i<NB_STRUCTURE_MIN;i++) {
				cards.add(CardGenerator.generateValidatedCard(CardType.STRUCTURE,manacurve.get(index_booster),factions.get(index_booster),random));
				index_booster++;
			}
			for (int i=0; i<NB_ACTION_MIN;i++) {
				cards.add(CardGenerator.generateValidatedCard(CardType.ACTION,manacurve.get(index_booster),factions.get(index_booster),random));
				index_booster++;
			}

	    	for (int i = 0; i < 3; i++) {
	    	    CardType type = random.nextInt(100) < 33 ? CardType.UNIT
	    	                    : random.nextInt(100) < 66 ? CardType.ACTION
	    	                    : CardType.STRUCTURE;
	    	    cards.add(CardGenerator.generateValidatedCard(type,manacurve.get(index_booster),factions.get(index_booster),random));
	    	    index_booster++;

			
	    	}
	 }
}
