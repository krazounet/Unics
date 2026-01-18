package aiGenerated;



import java.nio.file.*;
import java.time.Instant;



public final class ComfyUIWorker {

    private final ComfyUIClient client;
    private final Path imageRoot;

    public ComfyUIWorker(ComfyUIClient client, Path imageRoot) {
        this.client = client;
        this.imageRoot = imageRoot;
    }

    // ─────────────────────────────────────────────
    // EXECUTION
    // ─────────────────────────────────────────────

    public CardRender execute(CardRender render) {

        try {
            // 1️⃣ Construire le payload
            String payload =
                ComfyUIWorkflowBuilder.buildPayload(render);

            // 2️⃣ Envoyer à ComfyUI
            String response =
                client.sendPrompt(payload);

            // ⚠️ Ici tu récupéreras le prompt_id
            // puis tu polleras /history/{id}
            // pour récupérer l’image finale
            // (simplifié ici)

            byte[] imageData = waitAndFetchImage(response);

            // 3️⃣ Sauvegarder l’image
            Path imagePath = saveImage(render, imageData);

            // 4️⃣ Retourner un render DONE
            return new CardRender(
                render.renderId,
                render.cardSnapshotId,
                render.renderProfile,

                render.prompt,
                render.negativePrompt,
                render.seed,
                render.workflowId,

                imagePath.toString(),
                RenderStatus.DONE,

                render.createdAt,
                Instant.now()
            );

        } catch (Exception e) {

            // 5️⃣ En cas d’erreur → FAILED
            return new CardRender(
                render.renderId,
                render.cardSnapshotId,
                render.renderProfile,

                render.prompt,
                render.negativePrompt,
                render.seed,
                render.workflowId,

                null,
                RenderStatus.FAILED,

                render.createdAt,
                Instant.now()
            );
        }
    }

    // ─────────────────────────────────────────────
    // SIMPLIFICATION (à adapter)
    // ─────────────────────────────────────────────

    private byte[] waitAndFetchImage(String promptResponse) {
        // TODO :
        // - extraire prompt_id
        // - poll /history/{id}
        // - récupérer l’image (base64 → bytes)
        return new byte[0];
    }

    private Path saveImage(CardRender render, byte[] data) throws Exception {

        Path dir = imageRoot.resolve(render.cardSnapshotId.toString());
        Files.createDirectories(dir);

        Path file = dir.resolve(render.renderId + ".png");
        Files.write(file, data);

        return file;
    }
}
