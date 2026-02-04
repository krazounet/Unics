package unics.ExecUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;
import unics.Card;
import unics.CardGenerator;
import unics.Enum.CardType;
import unics.Enum.Faction;

public class ExecGenerateMassCardForDB {

    static final int NB_ESSAI_CARTE = 50;
    static final int TOTAL = 1000000;
    static Map<String, Integer> visualIdentityCount = new HashMap<>();
    
    
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int inserted = 0;
        int nbCardWithEffect = 0;
        JdbcCardDao cardDao=null;
        try {
        	cardDao = new JdbcCardDao(DbUtil.getConnection());

        
            for (int i = 0; i < TOTAL; i++) {

                int cost = random.nextInt(6) + 1;
                Faction faction = Faction.randomFaction();
                CardType type = CardType.randomCardType();

                Card card =
                    CardGenerator.generateValidatedCard(
                        type, cost, faction, random
                    );

                if (!card.getEffects().isEmpty()) {
                    nbCardWithEffect++;
                }

                cardDao.insertCard(card);
                inserted++;

                if (inserted % 10_000 == 0) {
                    System.out.println(
                        "Cartes générées : " + inserted
                        + " | Inserted cards: " + cardDao.countCards()
                    );
                }
            }
            System.out.println("FIN DE CREATION DE "+TOTAL+" CARTES");
            
            

        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // 🔒 fermeture EXPLICITE, même si exception
            cardDao.close();
        }

        long end = System.currentTimeMillis();
        double tempsEnSecondes = (end - start) / 1000.0;

        System.out.println("Temps : " + tempsEnSecondes + " s");
        System.out.println(
            "DONE: " + inserted + " cards generated. "
            + nbCardWithEffect + " avec effet"
        );
    }
}
