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

    private final JdbcCardDao dao;
    private int index = 0;
    private final List<Faction> factions;

    public Booster18DB(ThreadLocalRandom random, JdbcCardDao dao) {
        super(random);
        this.dao = dao;
        this.manacurve = new BoosterManaCurve(18, manaCurveProfile).getCurve();
        this.cards = new ArrayList<>();
        this.factions = FactionDistribution.generate(List.of(12, 6));
    }

    public void generate() {
        for (int i = 0; i < 10; i++) addFromDB(CardType.UNIT);
        for (int i = 0; i < 3; i++) addFromDB(CardType.STRUCTURE);
        for (int i = 0; i < 2; i++) addFromDB(CardType.ACTION);

        for (int i = 0; i < 3; i++) {
            CardType type = random.nextInt(100) < 33 ? CardType.UNIT
                : random.nextInt(100) < 66 ? CardType.ACTION
                : CardType.STRUCTURE;
            addFromDB(type);
        }
    }

    private void addFromDB(CardType type) {
        if (index >= manacurve.size()) {
            throw new IllegalStateException("Dépassement de la courbe de mana");
        }

        int mana = manacurve.get(index);
        Faction faction = factions.get(index);
        index++;

        try {
            CardDbRow row = dao.pickRandom(type, mana, faction);

            if (row == null) {
                throw new IllegalStateException(
                    "Impossible de générer une carte valide "
                    + type + "/" + mana + "/" + faction
                );
            }

            cards.add(dao.rebuildCard(row));

        } catch (SQLException e) {
            System.err.println("fallback Booster18DB");
            cards.add(
                CardGenerator.generateValidatedCard(
                    type, mana, faction, random
                )
            );
        }
    }
}

