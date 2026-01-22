package unics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dbPG18.CardDbRow;
import dbPG18.JdbcCardDao;
import unics.Enum.CardType;
import unics.Enum.Faction;

public class Booster18DB extends Booster {

	JdbcCardDao dao= new JdbcCardDao();
	int index = 0;
	List<Faction> factions = FactionDistribution.generate(List.of(12, 6));
	
    public Booster18DB(ThreadLocalRandom random) {
        super(random);

        manacurve = new BoosterManaCurve(18, manaCurveProfile).getCurve();
        

        cards = new ArrayList<>();
        

        for (int i = 0; i < 10; i++) 
        	addFromDB(CardType.UNIT);
     

        for (int i = 0; i < 3; i++)
        	addFromDB(CardType.STRUCTURE);
             for (int i = 0; i < 2; i++)
        	addFromDB(CardType.ACTION);
             for (int i = 0; i < 3; i++) {
            CardType type = random.nextInt(100) < 33 ? CardType.UNIT
                    : random.nextInt(100) < 66 ? CardType.ACTION
                    : CardType.STRUCTURE;

            addFromDB(type);
        }
    }
    
    private void addFromDB(CardType type) {

        int mana = manacurve.get(index);
        Faction faction = factions.get(index);
        index++;

        try {
            CardDbRow row = JdbcCardDao.pickRandom(type, mana, faction);

            if (row == null) {
                //cards.add(generateValidatedCard(type, mana, faction));
            	 throw new IllegalStateException("Impossible de générer une carte valide "+type+ "/"+mana+"/"+faction);
            }

            Card card = dao.rebuildCard(row);
            cards.add(card);

        } catch (SQLException e) {
            // log + fallback
        	System.err.println("fallback Booster18DB");
            cards.add(CardGenerator.generateValidatedCard(type, mana, faction,random));
        }
    }

    
}
