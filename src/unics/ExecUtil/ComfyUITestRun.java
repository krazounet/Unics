package unics.ExecUtil;


import java.nio.file.Path;

import aiGenerated.CardRender;
import aiGenerated.CardRenderFactory;
import aiGenerated.ComfyUIClient;
import aiGenerated.ComfyUIWorker;
import aiGenerated.RenderStatus;
import dbPG18.CardDbRow;
import dbPG18.JdbcCardDao;
import unics.Card;
import unics.snapshot.CardSnapshot;



public final class ComfyUITestRun {

    public static void main(String[] args) {

        try {
            // ─────────────────────────────────────────────
            // 1️⃣ Récupérer UNE carte depuis la DB
            // ─────────────────────────────────────────────
        	/*
            CardRepository cardRepository = new CardRepository(); 
            // ⚠️ adapte au vrai nom de ton repo

            Optional<Card> maybeCard = cardRepository.findAny();

            if (maybeCard.isEmpty()) {
                System.err.println("❌ Aucune carte trouvée en base");
                return;
            }
            */
            JdbcCardDao dao = new JdbcCardDao();

            CardDbRow row = dao.findRowByPublicId("6M6TNA"); // adapte si besoin
            if (row == null) {
                System.out.println("Carte non trouvée");
                return;
            }
            //Card rebuilt = dao.rebuildCard(row);
            
            
            Card card = dao.rebuildCard(row);
            System.out.println("✅ Carte chargée : " + card.getName());

            // ─────────────────────────────────────────────
            // 2️⃣ Freeze → CardSnapshot
            // ─────────────────────────────────────────────

            CardSnapshot snapshot = card.freeze();
            System.out.println("✅ Snapshot créé : " + snapshot.publicId);

            // ─────────────────────────────────────────────
            // 3️⃣ Création du CardRender (PENDING)
            // ─────────────────────────────────────────────

            CardRender render =
                CardRenderFactory.create(snapshot);

            System.out.println("🎨 Prompt généré :");
            System.out.println(render.prompt);

            // ─────────────────────────────────────────────
            // 4️⃣ Initialisation ComfyUI
            // ─────────────────────────────────────────────

            ComfyUIClient client =
                new ComfyUIClient("http://localhost:8188");

            ComfyUIWorker worker =
                new ComfyUIWorker(
                    client,
                    Path.of("images/test")
                );

            // ─────────────────────────────────────────────
            // 5️⃣ Exécution du rendu
            // ─────────────────────────────────────────────

            CardRender result = worker.execute(render);

            if (result.status == RenderStatus.DONE) {
                System.out.println("✅ Image générée !");
                System.out.println("📁 Chemin : " + result.imagePath);
            } else {
                System.err.println("❌ Rendu échoué");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}