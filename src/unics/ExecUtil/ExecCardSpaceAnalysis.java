package unics.ExecUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import aiGenerated.VisualIdentity;
import unics.Card;
import unics.CardGenerator;
import unics.CardIdentity;
import unics.Enum.CardType;
import unics.Enum.Faction;

public class ExecCardSpaceAnalysis {

	public static void main(String[] args) {
		final int TARGET = 1_000_000;
		final int STEP = 10_000;

		Map<String, Card> uniques = new HashMap<>(TARGET * 2);
		Map<String, Card> visualIdentityCounts = new HashMap<>();

		long generated = 0;
		long collisions = 0;

		long lastGenerated = 0;
		long lastUnique = 0;

		ThreadLocalRandom random = ThreadLocalRandom.current();

		while (uniques.size() < TARGET) {

		    generated++;

		    Card c = CardGenerator.generateValidatedCard(
		        CardType.randomCardType(),
		        random.nextInt(1, 7),
		        Faction.randomFaction(),
		        random
		    );

		    // invariant métier
		    if (c.getCardType() == CardType.ACTION && c.getEffects().isEmpty()) {
		        throw new IllegalStateException("ACTION sans effet");
		    }

		    String key = identityKey(c);

		    if (uniques.putIfAbsent(key, c) != null) {
		        collisions++;
		    }

		    if (uniques.size() % STEP == 0 && uniques.size() != lastUnique) {

		        long deltaGen = generated - lastGenerated;
		        long deltaUniq = uniques.size() - lastUnique;

		        double stepRate = (double) deltaUniq / deltaGen;
		        double globalRate = (double) uniques.size() / generated;

		        System.out.printf(
		            "unique=%d | generated=%d | collisions=%d | step_acceptance=%.3f | global_acceptance=%.3f%n",
		            uniques.size(),
		            generated,
		            collisions,
		            stepRate,
		            globalRate
		        );

		        lastGenerated = generated;
		        lastUnique = uniques.size();
		    }
		}
		// j'ai atteint la cible. combien sont uniques visuellement
		for (Card card : uniques.values()) {
		    // traitement sur la Card
			VisualIdentity vid= new VisualIdentity(card.freeze());
			visualIdentityCounts.putIfAbsent(vid.computeSignatureHash(), card);
		}
		System.out.println("sur "+TARGET+" cartes générées, "+visualIdentityCounts.size()+" sont uniques");
	}
	static String identityKey(Card c) {
	    return c.getIdentity().toString()
	         + ":" + CardIdentity.GENERATION_VERSION;
	}
}
