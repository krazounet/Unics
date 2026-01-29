package unics.godot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;

import aiGenerated.CardRender;
import aiGenerated.RenderProfile;
import dbPG18.JdbcCardSnapshotDao;
import dbPG18.JdbcCardRenderDao;
import unics.snapshot.CardSnapshot;


public class RenderBackCardDbTest {

    private static final UUID SNAPSHOT_ID =
        UUID.fromString("428571b4-33c4-4a0a-b4ba-3cc34ead6298");

    private static final String OUTPUT_PATH =
        "target/test-output/card-illustration-only.png";
    
    private static final String SOURCE_PATH =
            "resources/asset/UNIT-ASTRAL.png";
        

    public static void main(String[] args) {
        System.out.println("=== RenderBackCard DB Test ===");
        System.out.println("Snapshot ID : " + SNAPSHOT_ID);

        try {
            // ─────────────────────────────
            // 1. Charger le snapshot
            // ─────────────────────────────
            JdbcCardSnapshotDao snapshotDao = new JdbcCardSnapshotDao();
            CardSnapshot snapshot = snapshotDao.findById(SNAPSHOT_ID);

            if (snapshot == null) {
                throw new IllegalStateException(
                    "Snapshot introuvable : " + SNAPSHOT_ID
                );
            }

            System.out.println("✔ Snapshot chargé");
            System.out.println("  - visualSignature : " + snapshot.visualSignature);

            // ─────────────────────────────
            // 2. Charger le render via visualSignature
            // ─────────────────────────────
            JdbcCardRenderDao renderDao = new JdbcCardRenderDao();
            CardRender render =
                renderDao.findByVisualSignature(snapshot.visualSignature,RenderProfile.DEFAULT);

            if (render == null) {
                throw new IllegalStateException(
                    "CardRender introuvable pour visualSignature : "
                    + snapshot.visualSignature
                );
            }

            System.out.println("✔ CardRender trouvé");
            System.out.println("  - render      : " + render.renderId);
            System.out.println("  - illustration: " + render.imagePath);

            // ─────────────────────────────
            // 3. Charger les images
            // ─────────────────────────────
            BufferedImage baseRender =
                loadImage(SOURCE_PATH);

            BufferedImage illustration =
                loadImage(render.imagePath);

            // ─────────────────────────────
            // 4. Rendu
            // ─────────────────────────────
            BufferedImage result =
                RenderBackCard.renderIllustrationOnly(
                    baseRender,
                    illustration
                );

            // ─────────────────────────────
            // 5. Sauvegarde
            // ─────────────────────────────
            save(result, OUTPUT_PATH);

            System.out.println("✅ Rendu généré avec succès");
            System.out.println("➡ " + OUTPUT_PATH);

        } catch (Exception e) {
            System.err.println("❌ Échec du test RenderBackCard");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // ─────────────────────────────
    // Utils
    // ─────────────────────────────

    private static BufferedImage loadImage(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalStateException(
                "Image introuvable : " + file.getAbsolutePath()
            );
        }

        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IllegalStateException(
                "Lecture image impossible : " + file.getAbsolutePath()
            );
        }

        System.out.println("✔ Image chargée : "
            + path + " (" + img.getWidth() + "x" + img.getHeight() + ")");

        return img;
    }

    private static void save(BufferedImage img, String path) throws Exception {
        File out = new File(path);
        out.getParentFile().mkdirs();
        ImageIO.write(img, "PNG", out);
    }
}
