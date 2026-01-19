package unics.snapshot;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import unics.Enum.*;

public final class CardSnapshot {

	// ===== IDENTITE =====
    public final UUID snapshotId;   // 🧊 ID du snapshot
    public final UUID cardId;       // 🆔 ID de la card métier
    public final String publicId;
    public final String signature;
    public final int snapshotVersion;

    // ===== GAMEPLAY =====
    public final CardType type;
    public final Faction faction;
    public final int cost;
    public final int attack;
    public final int health;
    public final List<Keyword> keywords;
    public final List<EffectSnapshot> effects;

    // ===== TEXTE =====
    public final String name;
    public final String rulesText;
    public final String flavorText;

    // ===== RENDU =====
    public final String illustrationPromptBase;
    public final String frameId;

    // ===== META =====
    public final Instant generatedAt;
    public final String generatorVersion;
    public final int schemaVersion;

    public CardSnapshot(
    	UUID snapshotId,
        UUID cardId,
        String publicId,
        String signature,
        int snapshotVersion,

        CardType type,
        Faction faction,
        int cost,
        int attack,
        int health,
        List<Keyword> keywords,
        List<EffectSnapshot> effects,

        String name,
        String rulesText,
        String flavorText,

        String illustrationPromptBase,
        String frameId,

        Instant generatedAt,
        String generatorVersion,
        int schemaVersion
    ) {
    	this.snapshotId = snapshotId;
        this.cardId = cardId;
        this.publicId = publicId;
        this.signature = signature;
        this.snapshotVersion = snapshotVersion;
        this.type = type;
        this.faction = faction;
        this.cost = cost;
        this.attack = attack;
        this.health = health;
        this.keywords = List.copyOf(keywords);
        this.effects = List.copyOf(effects);
        this.name = name;
        this.rulesText = rulesText;
        this.flavorText = flavorText;
        this.illustrationPromptBase = illustrationPromptBase;
        this.frameId = frameId;
        this.generatedAt = generatedAt;
        this.generatorVersion = generatorVersion;
        this.schemaVersion = schemaVersion;
    }
}
