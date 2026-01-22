package unics;



import java.util.concurrent.ThreadLocalRandom;

import dbPG18.JdbcCardDao;
import unics.Enum.CardType;
import unics.Enum.Faction;

public class ExecGenerateMassCardForDB {

    static final int NB_ESSAI_CARTE = 50;
    static final int TOTAL = 100000;

    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        JdbcCardDao cardDao = new JdbcCardDao();

        int inserted = 0;
        int nb_card_with_effect=0;
        for (int i = 0; i < TOTAL; i++) {
            int cost = random.nextInt(6)+1;
            Faction faction = Faction.randomFaction();
            CardType type = CardType.randomCardType();
            
            Card card = CardGenerator.generateValidatedCard(type,cost,faction,random);
            if (card.getEffects().size()>0)nb_card_with_effect++;
            cardDao.insertCard(card);
            inserted++;

            if (inserted % 10_000 == 0) {
                System.out.println("Cartes générées : "+inserted+ "Inserted cards: " + cardDao.countCards());
                
            }
        }
        long end = System.currentTimeMillis();
        double tempsEnSecondes = (end - start) / 1000.0;
        System.out.println("Temps : " + tempsEnSecondes + " s");
        //cardDao.close();
        System.out.println("DONE: " + inserted + " cards generated. "+nb_card_with_effect);
    }

}
