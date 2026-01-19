package dbPG18;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import aiGenerated.CardRender;
import aiGenerated.RenderProfile;
import aiGenerated.RenderStatus;

public class JdbcCardRenderDao implements CardRenderDao {

    // ─────────────────────────────────────────────
    // INSERT
    // ─────────────────────────────────────────────

    @Override
    public void insert(CardRender r) {

        String sql = """
            INSERT INTO card_render (
                render_id,
                snapshot_id,
                status,
                image_path,
                seed,
                render_profile,
                workflow_id,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, r.renderId);
            ps.setObject(2, r.cardSnapshotId);

            ps.setString(3, r.status.name());
            ps.setString(4, r.imagePath);

            ps.setLong(5, r.seed);
            ps.setString(6, r.renderProfile.name());
            ps.setString(7, r.workflowId);

            ps.setTimestamp(8, Timestamp.from(r.createdAt));

            ps.executeUpdate();
            System.out.println("INSERT RENDER → " + r.renderId);
        } catch (SQLException e) {
            throw new RuntimeException("insert CardRender failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // MARK DONE
    // ─────────────────────────────────────────────

    @Override
    public void markDone(
        UUID renderId,
        String imagePath,
        Instant finishedAt
    ) {

        String sql = """
            UPDATE card_render
            SET status = ?,
                image_path = ?,
                finished_at = ?,
                error_message = NULL
            WHERE render_id = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, RenderStatus.DONE.name());
            ps.setString(2, imagePath);
            ps.setTimestamp(3, Timestamp.from(finishedAt));
            ps.setObject(4, renderId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("markDone failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // MARK FAILED
    // ─────────────────────────────────────────────

    @Override
    public void markFailed(
        UUID renderId,
        String errorMessage,
        Instant finishedAt
    ) {

        String sql = """
            UPDATE card_render
            SET status = ?,
                error_message = ?,
                finished_at = ?
            WHERE render_id = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, RenderStatus.FAILED.name());
            ps.setString(2, errorMessage);
            ps.setTimestamp(3, Timestamp.from(finishedAt));
            ps.setObject(4, renderId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("markFailed failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // FIND BY ID
    // ─────────────────────────────────────────────

    @Override
    public CardRender findById(UUID renderId) {

        String sql = """
            SELECT *
            FROM card_render
            WHERE render_id = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, renderId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new CardRender(
                UUID.fromString(rs.getString("render_id")),
                UUID.fromString(rs.getString("snapshot_id")),
                RenderProfile.valueOf(rs.getString("render_profile")), // ✅ on sotcke en base le string et on en récupere l'Enum

                null, // prompt (pas nécessaire ici)
                null, // negativePrompt
                rs.getLong("seed"),
                rs.getString("workflow_id"),

                rs.getString("image_path"),
                RenderStatus.valueOf(rs.getString("status")),

                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("finished_at") != null
                    ? rs.getTimestamp("finished_at").toInstant()
                    : null
            );

        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }
}

