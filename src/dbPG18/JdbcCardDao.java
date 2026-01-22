package dbPG18;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import unics.Card;
import unics.CardBuilder;
import unics.CardEffect;
import unics.CardIdentity;
import unics.Enum.AbilityType;
import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.Enum.TargetConstraint;
import unics.Enum.TargetType;
import unics.Enum.TriggerType;

public class JdbcCardDao {

	public int countCards() {
	    final String sql = "SELECT COUNT(*) FROM card";

	    try (Connection conn = DbUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	        throw new SQLException("COUNT(*) did not return a result");

	    } catch (SQLException e) {
	        throw new RuntimeException("Failed to count cards", e);
	    }
	}

	
	
    public boolean existsByIdentity(CardIdentity identity) {

        String sql = """
            SELECT 1
            FROM card
            WHERE identity_hash = ?
              AND identity_version = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, identity.toString());
            ps.setInt(2, CardIdentity.GENERATION_VERSION);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    public void insertCard(Card card) {

        String sql = """
            INSERT INTO card (
                id,
                public_id,
                identity_hash,
                identity_version,
                name,
                card_type,
                faction,
                energy_cost,
                attack,
                defense,
                power_score
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (identity_hash, identity_version)
            DO NOTHING
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, card.getId());
            ps.setString(2, card.getPublicId());
            ps.setString(3, card.getIdentity().toString());
            ps.setInt(4, CardIdentity.GENERATION_VERSION);

            ps.setString(5, card.getName());
            ps.setString(6, card.getCardType().name());
            ps.setString(7, card.getFaction().name());
            ps.setInt(8, card.getEnergyCost());

            ps.setObject(9, card.getAttack());
            ps.setObject(10, card.getDefense());

            ps.setInt(11, card.getPowerScore());

            ps.executeUpdate();
            
            insertKeywords(card, c);
            insertEffects(card, c);
        } catch (SQLException e) {
            throw new RuntimeException("insertCard failed", e);
        }
    }*/
    /*
    public void insertCard(Card card) {

        // 🔴 GARDE-FOU CRITIQUE (temporaire mais très utile)
        if (card.getCardType() == CardType.ACTION && card.getEffects().isEmpty()) {
            System.err.println(
                "[ERROR] Attempting to persist ACTION without effects: "
                + card.getPublicId()
            );
            throw new IllegalStateException("Invalid ACTION without effects");
        }

        System.out.println(
            "[DB] INSERT CARD "
            + card.getPublicId()
            + " id=" + card.getId()
            + " type=" + card.getCardType()
            + " cost=" + card.getEnergyCost()
            + " effects(before insert)=" + card.getEffects().size()
        );

        String sql = """
            INSERT INTO card (
                id,
                public_id,
                identity_hash,
                identity_version,
                name,
                card_type,
                faction,
                energy_cost,
                attack,
                defense,
                power_score
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (identity_hash, identity_version)
            DO NOTHING
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, card.getId());
            ps.setString(2, card.getPublicId());
            ps.setString(3, card.getIdentity().toString());
            ps.setInt(4, CardIdentity.GENERATION_VERSION);

            ps.setString(5, card.getName());
            ps.setString(6, card.getCardType().name());
            ps.setString(7, card.getFaction().name());
            ps.setInt(8, card.getEnergyCost());

            ps.setObject(9, card.getAttack());
            ps.setObject(10, card.getDefense());

            ps.setInt(11, card.getPowerScore());

            int rows = ps.executeUpdate();

            System.out.println(
                "[DB] card insert rows=" + rows
                + " (0 means duplicate)"
            );

            // 🔑 INSERT KEYWORDS
            insertKeywords(card, c);

            // 🔑 INSERT EFFECTS (LOG AVANT)
            System.out.println(
                "[DB] inserting effects for card "
                + card.getId()
                + " count=" + card.getEffects().size()
            );

            insertEffects(card, c);

            System.out.println(
                "[DB] DONE card "
                + card.getPublicId()
            );

        } catch (SQLException e) {
            throw new RuntimeException("insertCard failed for " + card.getPublicId(), e);
        }
    }*/
    public void insertCard(Card card) {

        String sql = """
            INSERT INTO card (
                id,
                public_id,
                identity_hash,
                identity_version,
                name,
                card_type,
                faction,
                energy_cost,
                attack,
                defense,
                power_score
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (identity_hash, identity_version)
            DO NOTHING
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, card.getId());
            ps.setString(2, card.getPublicId());
            ps.setString(3, card.getIdentity().toString());
            ps.setInt(4, CardIdentity.GENERATION_VERSION);

            ps.setString(5, card.getName());
            ps.setString(6, card.getCardType().name());
            ps.setString(7, card.getFaction().name());
            ps.setInt(8, card.getEnergyCost());

            ps.setObject(9, card.getAttack());
            ps.setObject(10, card.getDefense());
            ps.setInt(11, card.getPowerScore());

            int rows = ps.executeUpdate();

            // 🚨 SI doublon → on sort IMMEDIATEMENT
            if (rows == 0) {
                return;
            }

            // ✅ Carte réellement insérée → on insère le reste
            insertKeywords(card, c);
            insertEffects(card, c);

        } catch (SQLException e) {
            throw new RuntimeException("insertCard failed", e);
        }
    }


    public CardDbRow findRowByPublicId(String publicId) {

        String sql = """
            SELECT
                id,
                identity_hash,
                public_id,
                name,
                card_type,
                faction,
                energy_cost,
                attack,
                defense,
                power_score
            FROM card
            WHERE public_id = ?
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, publicId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new CardDbRow(
            	UUID.fromString(rs.getString("id")),
                rs.getString("identity_hash"),
                rs.getString("public_id"),
                rs.getString("name"),
                CardType.valueOf(rs.getString("card_type")),
                Faction.valueOf(rs.getString("faction")),
                rs.getInt("energy_cost"),
                (Integer) rs.getObject("attack"),
                (Integer) rs.getObject("defense"),
                rs.getInt("power_score")
            );

        } catch (SQLException e) {
            throw new RuntimeException("findRowByPublicId failed", e);
        }
    }

    private Set<Keyword> loadKeywords(UUID cardId, Connection c) throws SQLException {

        String sql = """
            SELECT keyword
            FROM card_keyword
            WHERE card_id = ?
        """;

        Set<Keyword> result = new HashSet<>();

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, cardId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(Keyword.valueOf(rs.getString("keyword")));
            }
        }

        return result;
    }
    public Card rebuildCard(CardDbRow row) throws SQLException {
    	Connection c_tmp = DbUtil.getConnection();
    	Set<Keyword> keywords = loadKeywords(row.id, c_tmp);
    	List<CardEffect> effects = loadEffects(row.id, c_tmp);
        return new CardBuilder()
            .withId(row.id)
            .withPublicId(row.publicId)
            .withName(row.name)
            .withType(row.cardType)
            .withFaction(row.faction)
            .withEnergyCost(row.energyCost)
            .withAttack(row.attack)
            .withDefense(row.defense)
            .withKeywords(keywords)   
            .withEffects(effects)   
            .withPowerScore(row.powerScore)
            .build();
    }
    private void insertKeywords(Card card, Connection c) throws SQLException {

        if (card.getKeywords().isEmpty()) {
            return;
        }

        String sql = """
            INSERT INTO card_keyword (card_id, keyword)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (Keyword k : card.getKeywords()) {
                ps.setObject(1, card.getId());
                ps.setString(2, k.name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    private void insertEffects(Card card, Connection c) throws SQLException {

        if (card.getEffects().isEmpty()) {
            return;
        }

        String effectSql = """
            INSERT INTO card_effect (
                id, card_id,
                trigger, condition_keyword,
                ability, value, target_type
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT DO NOTHING
        """;

        String constraintSql = """
            INSERT INTO card_effect_constraint (
                effect_id, constraint_type
            )
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;

        try (
            PreparedStatement eps = c.prepareStatement(effectSql);
            PreparedStatement cps = c.prepareStatement(constraintSql)
        ) {
            for (CardEffect e : card.getEffects()) {

                //UUID eid = effectId(e);
            	UUID eid = UUID.randomUUID(); // ← FIX CRITIQUE
                eps.setObject(1, eid);
                eps.setObject(2, card.getId());
                eps.setString(3, e.getTrigger().name());
                eps.setString(4,
                    e.getConditionKeyword() != null ? e.getConditionKeyword().name() : null
                );
                eps.setString(5, e.getAbility().name());
                eps.setObject(6, e.getValue());
                eps.setString(7, e.getTargetType().name());
                eps.addBatch();

                for (TargetConstraint cst : e.getConstraints()) {
                    cps.setObject(1, eid);
                    cps.setString(2, cst.name());
                    cps.addBatch();
                }
            }
            eps.executeBatch();
            cps.executeBatch();
        }
    }
    /*
    private UUID effectId(CardEffect effect) {
        return UUID.nameUUIDFromBytes(
            effect.toIdentityString().getBytes(StandardCharsets.UTF_8)
        );
    }*/
    private List<CardEffect> loadEffects(UUID cardId, Connection c) throws SQLException {

        String sql = """
            SELECT
                e.id,
                e.trigger,
                e.condition_keyword,
                e.ability,
                e.value,
                e.target_type
            FROM card_effect e
            WHERE e.card_id = ?
        """;

        Map<UUID, CardEffectBuilderData> tmp = new LinkedHashMap<>();

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, cardId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID eid = UUID.fromString(rs.getString("id"));

                tmp.put(eid, new CardEffectBuilderData(
                    TriggerType.valueOf(rs.getString("trigger")),
                    rs.getString("condition_keyword") != null
                        ? Keyword.valueOf(rs.getString("condition_keyword"))
                        : null,
                    AbilityType.valueOf(rs.getString("ability")),
                    rs.getObject("value") != null ? rs.getInt("value") : null,
                    TargetType.valueOf(rs.getString("target_type"))
                ));
            }
        }

        loadEffectConstraints(tmp, c);

        return tmp.values().stream()
            .map(CardEffectBuilderData::build)
            .toList();
    }
    private void loadEffectConstraints(
    	    Map<UUID, CardEffectBuilderData> effects,
    	    Connection c
    	) throws SQLException {

    	    String sql = """
    	        SELECT effect_id, constraint_type
    	        FROM card_effect_constraint
    	        WHERE effect_id = ANY (?)
    	    """;

    	    Array effectIds = c.createArrayOf(
    	        "uuid",
    	        effects.keySet().toArray()
    	    );

    	    try (PreparedStatement ps = c.prepareStatement(sql)) {
    	        ps.setArray(1, effectIds);
    	        ResultSet rs = ps.executeQuery();

    	        while (rs.next()) {
    	            UUID eid = UUID.fromString(rs.getString("effect_id"));
    	            TargetConstraint cst =
    	                TargetConstraint.valueOf(rs.getString("constraint_type"));

    	            effects.get(eid).constraints.add(cst);
    	        }
    	    }
    	}
    public static CardDbRow pickRandom(
    	    
    	    CardType type,
    	    int mana,
    	    Faction faction
    	) throws SQLException {

    	    String sql = """
    	        SELECT
    	        id,
                identity_hash,
                public_id,
                name,
                card_type,
                faction,
                energy_cost,
                attack,
                defense,
                power_score
    	        FROM card
    	        WHERE card_type = ?
    	          AND energy_cost = ?
    	          AND faction = ?
    	        ORDER BY RANDOM()
    	        LIMIT 1
    	    """;
    	    
    	    try (
    	    		Connection c = DbUtil.getConnection();
    	    		PreparedStatement ps = c.prepareStatement(sql)) {

    	        ps.setString(1, type.name());
    	        ps.setInt(2, mana);
    	        ps.setString(3, faction.name());

    	        try (ResultSet rs = ps.executeQuery()) {

    	        	if (!rs.next()) {
    	                return null;
    	            }

    	            return new CardDbRow(
    	            	UUID.fromString(rs.getString("id")),
    	                rs.getString("identity_hash"),
    	                rs.getString("public_id"),
    	                rs.getString("name"),
    	                CardType.valueOf(rs.getString("card_type")),
    	                Faction.valueOf(rs.getString("faction")),
    	                rs.getInt("energy_cost"),
    	                (Integer) rs.getObject("attack"),
    	                (Integer) rs.getObject("defense"),
    	                rs.getInt("power_score")
    	            );
    	        }
    	    }

    	   
    	}
    private static class CardEffectBuilderData {

        final TriggerType trigger;
        final Keyword condition;
        final AbilityType ability;
        final Integer value;
        final TargetType targetType;
        final Set<TargetConstraint> constraints = new HashSet<>();

        CardEffectBuilderData(
            TriggerType trigger,
            Keyword condition,
            AbilityType ability,
            Integer value,
            TargetType targetType
        ) {
            this.trigger = trigger;
            this.condition = condition;
            this.ability = ability;
            this.value = value;
            this.targetType = targetType;
        }

        CardEffect build() {
            return new CardEffect(
                trigger,
                condition,
                ability,
                value != null ? value : 0,
                targetType,
                constraints
            );
        }
    }

    
}
