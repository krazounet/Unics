package aiGenerated;



import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


import unics.snapshot.CardSnapshot;

public final class CardRenderFactory {

    private CardRenderFactory() {
        // factory statique
    }

    // ─────────────────────────────────────────────
    // API PUBLIQUE
    // ─────────────────────────────────────────────

    public static CardRender create(CardSnapshot snapshot) {
        return create(snapshot, RenderProfile.DEFAULT);
    }

    public static CardRender create(CardSnapshot snapshot, RenderProfile profile) {

        // 1️⃣ Génération du prompt FINAL (figé)
    	CardSnapshotPromptGenerator generator =
            new CardSnapshotPromptGenerator(snapshot, profile);

        String prompt = generator.generatePrompt();

        // 2️⃣ Seed reproductible (mais aléatoire par défaut)
        long seed = generateSeed();

        // 3️⃣ Création du render PENDING
        return new CardRender(
            UUID.randomUUID(),           // renderId
            snapshot.snapshotId,             // lien snapshot
            profile,                     // profil de rendu

            prompt,
            profile.negativePrompt,
            seed,
            profile.workflowId,

            null,                        // imagePath (pas encore générée)
            RenderStatus.PENDING,

            Instant.now(),               // createdAt
            null                         // renderedAt
        );
    }

    // ─────────────────────────────────────────────
    // UTILITAIRE
    // ─────────────────────────────────────────────

    private static long generateSeed() {
        return ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
    }
}
