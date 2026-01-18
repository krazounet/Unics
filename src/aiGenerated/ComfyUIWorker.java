package aiGenerated;

import java.nio.file.*;
import java.time.Instant;

public final class ComfyUIWorker {

    private final ComfyUIClient client;
    private final Path imageRoot;

    // ⚠️ dossier OUTPUT de ComfyUI
    private final Path comfyOutputDir =
        Path.of("C:/AI/ComfyUI_windows_portable/ComfyUI/output");

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

            // 3️⃣ Attendre le fichier image ComfyUI
            String filename =
                waitAndFetchImageFile(response);

            // 4️⃣ Copier le fichier généré
            Path imagePath =
                copyImage(render, filename);

            // 5️⃣ Retourner un render DONE
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

            e.printStackTrace();

            // 6️⃣ En cas d’erreur → FAILED
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
    // COMFYUI
    // ─────────────────────────────────────────────

    private String waitAndFetchImageFile(String promptResponse)
        throws Exception {

        String promptId =
            ComfyUIResponseParser.extractPromptId(promptResponse);

        return ComfyUIPoller.waitForImageFile(
            client,
            promptId,
            600_000, // 10 minutes
            1_000
        );
    }

    // ─────────────────────────────────────────────
    // FILE SYSTEM
    // ─────────────────────────────────────────────

    private Path copyImage(CardRender render, String filename)
        throws Exception {

        Path source =
            comfyOutputDir.resolve(filename);

        if (!Files.exists(source)) {
            throw new IllegalStateException(
                "ComfyUI image not found: " + source
            );
        }

        Path dir =
            imageRoot.resolve(render.cardSnapshotId.toString());

        Files.createDirectories(dir);

        Path target =
            dir.resolve(render.renderId + ".png");

        Files.copy(
            source,
            target,
            StandardCopyOption.REPLACE_EXISTING
        );

        return target;
    }
}
