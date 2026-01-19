package unics.ExecUtil;

import java.nio.file.Path;

import aiGenerated.*;
import dbPG18.*;
import unics.Card;

public final class CardRenderPipelineTestRun {

    public static void main(String[] args) {

        try {
            // ─────────────────────────────────────────────
            // 1️⃣ Charger une carte depuis la DB
            // ─────────────────────────────────────────────

            JdbcCardDao cardDao = new JdbcCardDao();

            CardDbRow row =
                cardDao.findRowByPublicId("6M6TNA");

            if (row == null) {
                System.err.println("❌ Carte non trouvée");
                return;
            }

            Card card =
                cardDao.rebuildCard(row);

            System.out.println("✅ Carte chargée : " + card.getName());

            // ─────────────────────────────────────────────
            // 2️⃣ Initialiser le pipeline
            // ─────────────────────────────────────────────

            CardSnapshotDao snapshotDao =
                new JdbcCardSnapshotDao();

            CardRenderDao renderDao =
                new JdbcCardRenderDao();

            ComfyUIClient client =
                new ComfyUIClient("http://localhost:8188");

            ComfyUIWorker worker =
                new ComfyUIWorker(
                    client,
                    Path.of("images/test")
                );

            CardRenderPipeline pipeline =
                new CardRenderPipeline(
                    snapshotDao,
                    renderDao,
                    worker
                );

            // ─────────────────────────────────────────────
            // 3️⃣ Lancer le rendu complet
            // ─────────────────────────────────────────────

            CardRender result =
                pipeline.renderCard(card);

            // ─────────────────────────────────────────────
            // 4️⃣ Résultat
            // ─────────────────────────────────────────────

            if (result.status == RenderStatus.DONE) {
                System.out.println("🎉 RENDU OK");
                System.out.println("📁 Image : " + result.imagePath);
            } else {
                System.err.println("❌ RENDU FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
