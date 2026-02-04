package aiGenerated;

import java.nio.file.Path;
import java.time.Instant;

import dbPG18.CardRenderDaoInterface;
import dbPG18.CardSnapshotDaoInterface;
import unics.Card;
import unics.snapshot.CardSnapshot;

public class CardRenderPipeline {

    private final CardSnapshotDaoInterface snapshotDao;
    private final CardRenderDaoInterface renderDao;
    private final ComfyUIWorker worker;

    public CardRenderPipeline(
        CardSnapshotDaoInterface snapshotDao,
        CardRenderDaoInterface renderDao,
        ComfyUIWorker worker
    ) {
        this.snapshotDao = snapshotDao;
        this.renderDao = renderDao;
        this.worker = worker;
    }

    // ─────────────────────────────────────────────
    // PIPELINE PRINCIPAL
    // ─────────────────────────────────────────────

    public CardRender renderCard(Card card) {

        // 1️⃣ Freeze (création en mémoire)
        CardSnapshot frozen = card.freeze();
        
        //visual identity
        String visualSig = new VisualIdentity(frozen).computeSignatureHash(); 
        
        // 2️⃣ CACHE VISUEL (clé du système)
        CardRender existing =
            renderDao.findByVisualSignature(
                visualSig,
                RenderProfile.DEFAULT
            );

        if (existing != null && existing.status == RenderStatus.DONE) {
            System.out.println("🎯 VISUAL HIT → reuse image");
            return existing;
        }
        
        // 2️⃣ Tentative d’insertion (idempotente grâce à UNIQUE(signature))
        snapshotDao.insert(frozen);

        // 3️⃣ RELIRE DEPUIS LA DB (source de vérité)
        CardSnapshot snapshot =
            snapshotDao.findBySignature(frozen.signature);

        if (snapshot == null) {
            throw new IllegalStateException(
                "Snapshot not found after insert – DB inconsistency"
            );
        }

        // 4️⃣ Création du render (PENDING)
        CardRender render =
            CardRenderFactory.create(snapshot);

        renderDao.insert(render);

        // 5️⃣ Exécution ComfyUI
        CardRender result =
            worker.execute(render);

        // 6️⃣ Mise à jour DB
        if (result.status == RenderStatus.DONE) {
            renderDao.markDone(
                render.renderId,
                result.imagePath,
                Instant.now()
            );
        } else {
            renderDao.markFailed(
                render.renderId,
                "Render failed",
                Instant.now()
            );
        }

        return result;
    }
    public Path resolveImage(Card card) {

        CardRender render = renderCard(card);

        if (render.status != RenderStatus.DONE) {
            throw new IllegalStateException(
                "Render failed for card " + card.getName()
            );
        }

        return Path.of(render.imagePath);
    }


}
