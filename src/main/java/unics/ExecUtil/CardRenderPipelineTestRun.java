package unics.ExecUtil;

import java.nio.file.Path;
import java.sql.Connection;

import aiGenerated.*;
import dbPG18.*;
import unics.Card;

public final class CardRenderPipelineTestRun {

    public static void main(String[] args) {

    	Card card = null;

        try {
            // ─────────────────────────────────────────────
            // 1️⃣ Charger la carte (DB courte et fermée)
            // ─────────────────────────────────────────────

            JdbcCardDao cardDao =
                new JdbcCardDao(DbUtil.getConnection());

            try {
                CardDbRow row =
                    cardDao.findRowByPublicId("AZNR4V");

                if (row == null) {
                    System.err.println("❌ Carte non trouvée");
                    return;
                }

                card = cardDao.rebuildCard(row);

                System.out.println("✅ Carte chargée : " + card.getName());

            } finally {
                // 🔒 fermeture EXPLICITE
                cardDao.close();
            }
            // ─────────────────────────────────────────────
            // 2️⃣ Initialiser le pipeline
            // ─────────────────────────────────────────────
            Connection conn = DbUtil.getConnection();
            CardSnapshotDaoInterface snapshotDao =
                new JdbcCardSnapshotDao(conn);

            CardRenderDaoInterface renderDao =
                new JdbcCardRenderDao(conn);

            ComfyUIClient client =
                new ComfyUIClient("http://localhost:8188");

            ComfyUIWorker worker =
                new ComfyUIWorker(
                    client,
                    Path.of("images")
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
