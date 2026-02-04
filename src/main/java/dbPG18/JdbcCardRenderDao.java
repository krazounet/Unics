package dbPG18;

import java.sql.*;
import java.time.Instant;
import java.util.UUID;

import aiGenerated.CardRender;
import aiGenerated.RenderProfile;
import aiGenerated.RenderStatus;

public class JdbcCardRenderDao implements CardRenderDaoInterface {

    // ─────────────────────────────────────────────
    // INSERT (idempotent via UNIQUE constraint)
    // ─────────────────────────────────────────────

    @Override
    public void insert(CardRender r) {

        String sql = """
            INSERT INTO card_render (
        render_id,
        visual_signature,
        render_profile,

        prompt,
        negative_prompt,

        seed,
        workflow_id,
        status,
        created_at
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

        	ps.setObject(1, r.renderId);
        	ps.setString(2, r.visual_signature);
        	ps.setString(3, r.renderProfile.name());

        	ps.setString(4, r.prompt);
        	ps.setString(5, r.negativePrompt);

        	ps.setLong(6, r.seed);
        	ps.setString(7, r.workflowId);
        	ps.setString(8, r.status.name());
        	ps.setTimestamp(9, Timestamp.from(r.createdAt));


            ps.executeUpdate();

            System.out.println("INSERT RENDER → " + r.visual_signature);

        } catch (SQLException e) {
            throw new RuntimeException("insert CardRender failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // FIND BY VISUAL SIGNATURE
    // ─────────────────────────────────────────────

    @Override
    public CardRender findByVisualSignature(
        String visualSignature,
        RenderProfile profile
    ) {

        String sql = """
            SELECT *
            FROM card_render
            WHERE visual_signature = ?
              AND render_profile = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, visualSignature);
            ps.setString(2, profile.name());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("findByVisualSignature failed", e);
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

            return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // MAPPER
    // ─────────────────────────────────────────────

    private CardRender map(ResultSet rs) throws SQLException {

        return new CardRender(
            UUID.fromString(rs.getString("render_id")),
            rs.getString("visual_signature"),
            RenderProfile.valueOf(rs.getString("render_profile")),

            null, // prompt (inutile ici)
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
    }
}
