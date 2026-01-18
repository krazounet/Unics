package aiGenerated;


import java.time.Instant;
import java.util.UUID;


public final class CardRender {

    // ===== IDENTITE =====
    public final UUID renderId;
    public final UUID cardSnapshotId;

    // ===== PROFIL =====
    public final RenderProfile renderProfile;

    // ===== DONNEES DE RENDU =====
    public final String prompt;
    public final String negativePrompt;
    public final long seed;
    public final String workflowId;

    // ===== ARTEFACT =====
    public final String imagePath; // null tant que pas généré

    // ===== ETAT =====
    public final RenderStatus status;

    // ===== META =====
    public final Instant createdAt;
    public final Instant renderedAt;

    public CardRender(
        UUID renderId,
        UUID cardSnapshotId,
        RenderProfile renderProfile,
        String prompt,
        String negativePrompt,
        long seed,
        String workflowId,
        String imagePath,
        RenderStatus status,
        Instant createdAt,
        Instant renderedAt
    ) {
        this.renderId = renderId;
        this.cardSnapshotId = cardSnapshotId;
        this.renderProfile = renderProfile;
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.seed = seed;
        this.workflowId = workflowId;
        this.imagePath = imagePath;
        this.status = status;
        this.createdAt = createdAt;
        this.renderedAt = renderedAt;
    }
}
