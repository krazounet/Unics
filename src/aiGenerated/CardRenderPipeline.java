package aiGenerated;

import java.time.Instant;

import dbPG18.CardRenderDao;
import dbPG18.CardSnapshotDao;
import unics.Card;
import unics.snapshot.CardSnapshot;

public class CardRenderPipeline {

    private final CardSnapshotDao snapshotDao;
    private final CardRenderDao renderDao;
    private final ComfyUIWorker worker;

    public CardRenderPipeline(
        CardSnapshotDao snapshotDao,
        CardRenderDao renderDao,
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

}
