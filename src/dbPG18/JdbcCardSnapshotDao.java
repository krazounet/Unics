package dbPG18;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import unics.snapshot.CardSnapshot;
import unics.snapshot.EffectSnapshot;
import unics.Enum.*;

public class JdbcCardSnapshotDao implements CardSnapshotDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ─────────────────────────────────────────────
    // INSERT
    // ─────────────────────────────────────────────

    @Override
    public void insert(CardSnapshot s) {

        String sql = """
            INSERT INTO card_snapshot (
                id,
                card_id,
                public_id,
                signature,

                card_type,
                faction,
                cost,
                attack,
                health,

                keywords,
                effects,

                name,
                rules_text,
                flavor_text,

                illustration_prompt_base,
                frame_id,

                generated_at,
                generator_version,
                schema_version
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (signature) DO NOTHING
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, s.snapshotId);          // snapshot id                     !!!!! A CHANGER
            ps.setObject(2, s.cardId);          // FK card_id (voir note plus bas) !!!!! A CHANGER
            ps.setString(3, s.publicId);
            ps.setString(4, s.signature);

            ps.setString(5, s.type.name());
            ps.setString(6, s.faction.name());
            ps.setInt(7, s.cost);
            ps.setObject(8, s.attack);
            ps.setObject(9, s.health);

            ps.setString(10, toJson(s.keywords));
            ps.setString(11, toJson(s.effects));

            ps.setString(12, s.name);
            ps.setString(13, s.rulesText);
            ps.setString(14, s.flavorText);

            ps.setString(15, s.illustrationPromptBase);
            ps.setString(16, s.frameId);

            ps.setTimestamp(17, Timestamp.from(s.generatedAt));
            ps.setString(18, s.generatorVersion);
            ps.setInt(19, s.schemaVersion);

            ps.executeUpdate();
            System.out.println("INSERT SNAPSHOT → " + s.snapshotId);
        } catch (SQLException e) {
            throw new RuntimeException("insert CardSnapshot failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // FIND BY SIGNATURE
    // ─────────────────────────────────────────────

    @Override
    public CardSnapshot findBySignature(String signature) {

        String sql = """
            SELECT *
            FROM card_snapshot
            WHERE signature = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, signature);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? mapRow(rs) : null;

        } catch (SQLException e) {
            throw new RuntimeException("findBySignature failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // FIND BY ID
    // ─────────────────────────────────────────────

    @Override
    public CardSnapshot findById(UUID id) {

        String sql = """
            SELECT *
            FROM card_snapshot
            WHERE id = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? mapRow(rs) : null;

        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // ROW MAPPING
    // ─────────────────────────────────────────────

    private CardSnapshot mapRow(ResultSet rs) throws SQLException {

        try {
            return new CardSnapshot(
            		UUID.fromString(rs.getString("id")),        // snapshotId
            	    UUID.fromString(rs.getString("card_id")),   // cardId
                rs.getString("public_id"),
                rs.getString("signature"),
                rs.getInt("schema_version"),

                CardType.valueOf(rs.getString("card_type")),
                Faction.valueOf(rs.getString("faction")),
                rs.getInt("cost"),
                rs.getInt("attack"),
                rs.getInt("health"),

                fromJson(rs.getString("keywords"),
                    new TypeReference<List<Keyword>>() {}),
                fromJson(rs.getString("effects"),
                    new TypeReference<List<EffectSnapshot>>() {}),

                rs.getString("name"),
                rs.getString("rules_text"),
                rs.getString("flavor_text"),

                rs.getString("illustration_prompt_base"),
                rs.getString("frame_id"),

                rs.getTimestamp("generated_at").toInstant(),
                rs.getString("generator_version"),
                rs.getInt("schema_version")
            );

        } catch (Exception e) {
            throw new SQLException("map CardSnapshot failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // JSON HELPERS
    // ─────────────────────────────────────────────

    private static String toJson(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    private static <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
}
