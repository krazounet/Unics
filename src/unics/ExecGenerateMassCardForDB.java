package unics;



import java.util.concurrent.ThreadLocalRandom;

import dbPG18.JdbcCardDao;
import unics.Enum.CardType;

public class ExecGenerateMassCardForDB {

    static final int NB_ESSAI_CARTE = 50;
    static final int TOTAL = 1_000_000;

    public static void main(String[] args) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        JdbcCardDao cardDao = new JdbcCardDao();

        int inserted = 0;

        for (int i = 0; i < TOTAL; i++) {

            Card card = generateValidatedCard(random);
            cardDao.insertCard(card);
            inserted++;

            if (inserted % 10_000 == 0) {
                System.out.println("Inserted cards: " + inserted);
            }
        }

        //cardDao.close();
        System.out.println("DONE: " + inserted + " cards generated");
    }

    protected static Card generateValidatedCard(ThreadLocalRandom random) {
        for (int i = 0; i < NB_ESSAI_CARTE; i++) {

        	CardType[] values = CardType.values();
            CardType type = values[ThreadLocalRandom.current().nextInt(values.length)];
            int cost = random.nextInt(6)+1;

            Card candidate =
                CardGenerator.generateCard(type, cost, random);

            if (CardGenerator.isValid(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Impossible de générer une carte valide");
    }
}
