package dbPG18.Exec;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;
import dbPG18.JdbcCardSnapshotDao;
import dbPG18.JdbcCardRenderDao;
import aiGenerated.CardRenderPipeline;
import aiGenerated.ComfyUIClient;
import aiGenerated.ComfyUIWorker;
import unics.Card;

public class ExecMassSnapRender {

    public static final int BATCH_SIZE = 100;

    /**
     * Pipeline :
     * 1) sélectionner des Card en base qui n'ont pas eu de snapshot
     * 2) appeler CardRenderPipeline.renderCard(card)
     *    - freeze
     *    - snapshot
     *    - cache visuel
     *    - render
     *    - génération éventuelle
     */
    public static void main(String[] args) {

        int processed = 0;

        JdbcCardSnapshotDao snapshotDao = new JdbcCardSnapshotDao();
        JdbcCardRenderDao renderDao = new JdbcCardRenderDao();

        ComfyUIWorker worker =
            new ComfyUIWorker(
                new ComfyUIClient("http://localhost:8188"),
                Path.of("images")
            );

        CardRenderPipeline pipeline =
            new CardRenderPipeline(snapshotDao, renderDao, worker);

        try (Connection c = DbUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement("""
                SELECT c.id
                FROM card c
                LEFT JOIN card_snapshot s ON s.card_id = c.id
                WHERE s.id IS NULL
                ORDER BY c.id
                LIMIT ?
            """);

            ps.setInt(1, BATCH_SIZE);

            try (
                ResultSet rs = ps.executeQuery();
                JdbcCardDao cardDao = new JdbcCardDao(c)
            ) {

                while (rs.next()) {
                    UUID cardId = UUID.fromString(rs.getString("id"));

                    try {
                        processOneCard(cardId, cardDao, pipeline);
                        processed++;
                    } catch (Exception e) {
                        System.err.println("❌ Erreur cardId=" + cardId);
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("ExecMassSnapRender failed", e);
        }

        System.out.println("✅ FIN — batch traité : " + processed + " cartes");
    }


    private static void processOneCard(
        UUID cardId,
        JdbcCardDao cardDao,
        CardRenderPipeline pipeline
    ) throws Exception {

        // 1️⃣ Rebuild carte métier
        var row = cardDao.findRowById(cardId);
        if (row == null) {
            System.err.println("⚠️ Card disparue en DB, id=" + cardId);
            return;
        }
        Card card = cardDao.rebuildCard(row);

        // 2️⃣ Pipeline COMPLET (snapshot + render + cache + génération)
        pipeline.renderCard(card);
    }
}
