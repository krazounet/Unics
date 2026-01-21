package unics.ExecUtil;



import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import dbPG18.JdbcCardDao;
import unics.Card;
import unics.CardBuilder;
import unics.CardEffect;
import unics.FactionProfileRegistry;
import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;



public class CardInsertTest {

    public static void main(String[] args) {

        // 1️⃣ Construire la carte VIA LE BUILDER
        Card card = new CardBuilder()
        		.withRandomUuid()
        		.withGeneratePublicID()
                .withFaction(Faction.ASTRAL)
                .withType(CardType.UNIT)
                .withEnergyCost(3)
                .withKeywords(Set.of(Keyword.BOUCLIER))
                .withAttack(2)
                .withDefense(4)
                .withPowerScore(42)
                .withEffects(List.of(CardEffect.generateRandomEffect(CardType.UNIT, 3, ThreadLocalRandom.current(), FactionProfileRegistry.get(Faction.ASTRAL), new HashSet<>(),Set.of(Keyword.BOUCLIER))))
                .withGeneratename()
                .build();
        
        
        // 2️⃣ Vérification visuelle
        System.out.println("Identity = " + card.getIdentity());

        // 3️⃣ Insertion DB via DAO minimal
        JdbcCardDao dao = new JdbcCardDao();

        if (dao.existsByIdentity(card.getIdentity())) {
            System.out.println("Carte déjà existante en base");
        } else {
            dao.insertCard(card);
            System.out.println("Carte insérée en base");
        }
    }
}

