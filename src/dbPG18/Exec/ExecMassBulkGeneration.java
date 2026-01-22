package dbPG18.Exec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;
import unics.Card;
import unics.CardGenerator;
import unics.CardIdentity;
import unics.Enum.CardType;
import unics.Enum.Faction;

public class ExecMassBulkGeneration {
	static final int TARGET = 1000000;
	static final int MAX_TRIES = TARGET * 3; // sécurité
	static Map<String, Card> uniques = new LinkedHashMap<>(1_200_000);


	
	
	
	
	public static void main(String[] args) {
		
		int tries = 0;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		while (uniques.size() < TARGET && tries < MAX_TRIES) {
		    tries++;

		    int cost = random.nextInt(1, 7);
		    Faction faction = Faction.randomFaction();
		    CardType type = CardType.randomCardType();

		    Card card = CardGenerator.generateValidatedCard(type, cost, faction, random);

		    String key = card.getIdentity().toString()
		               + ":" + CardIdentity.GENERATION_VERSION;

		    uniques.putIfAbsent(key, card);

		    

		    if (uniques.size() % 100_000 == 0) {
		        System.out.println("Uniques: " + uniques.size());
		    }
		}

		if (uniques.size() < TARGET) {
		    throw new IllegalStateException(
		        "Impossible d'atteindre " + TARGET + " cartes uniques, obtenu: " + uniques.size()
		    );
		}
		// 🔥 INSERTION DB — pattern CORRECT
        try (JdbcCardDao dao = new JdbcCardDao(DbUtil.getConnection())) {

            for (Card c : uniques.values()) {

                if (c.getCardType() == CardType.ACTION && c.getEffects().isEmpty()) {
                    throw new IllegalStateException("carte action sans effet");
                }

                dao.insertCardBatch(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
