package unics;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.snapshot.CardSnapshot;
import unics.snapshot.EffectSnapshot;
import unics.snapshot.SnapshotVersion;

public final class Card {

    // Identité
    private final UUID id; 			//identifiant technique
    private final String publicId;	//identifiant exposé
    private CardIdentity identity;	//unicité métier (éviter les doublons)
    
    private final String name;

    // Classification
    private final CardType cardType;
    private final Faction faction;

    // Coûts
    private final int energyCost;

    // Stats (optionnelles selon le type)
    private final Integer attack;
    private final Integer defense;

    // Mots-clés passifs
    private final Set<Keyword> keywords;

    // Capacités / effets
    private final List<CardEffect> effects;

    // Métadonnées générateur
    private final int powerScore;

    public Card(
            UUID id,
            String pubid,
            CardIdentity identity,
            String name,
            CardType cardType,
            Faction faction,
            int energyCost,
            Integer attack,
            Integer defense,
            Set<Keyword> keywords,
            List<CardEffect> effects,
            int powerScore
    ) {
        this.id = id;
        this.publicId = pubid;
        this.identity = identity;
        this.name = name;
        this.cardType = cardType;
        this.faction = faction;
        this.energyCost = energyCost;
        this.attack = attack;
        this.defense = defense;
        this.keywords = Set.copyOf(keywords);
        this.effects = List.copyOf(effects);
        this.powerScore = powerScore;
    }

    // Getters uniquement (immutabilité)
    public UUID getId() { return id; }
    public String getPublicId() {return publicId;}
    public CardIdentity getIdentity() {return identity;}
    public String getName() { return name; }
    public CardType getCardType() { return cardType; }
    public Faction getFaction() { return faction; }
    public int getEnergyCost() { return energyCost; }
    public Integer getAttack() { return attack; }
    public Integer getDefense() { return defense; }
    public Set<Keyword> getKeywords() { return keywords; }
    public List<CardEffect> getEffects() { return effects; }
    public int getPowerScore() { return powerScore; }
 // ==================================================
    // FREEZE
    // ==================================================
    public CardSnapshot freeze() {

        // 1️⃣ Effets figés
        List<EffectSnapshot> effectSnapshots = effects.stream()
            .map(e -> new EffectSnapshot(
                e.getTrigger(),
                e.getConditionKeyword(),
                e.getAbility(),
                e.getValue(),
                e.getTargetType(),
                e.getConstraints()
            ))
            .toList();

        // 2️⃣ Texte FINAL (figé)
        String rulesText = effects.stream()
            .map(CardEffect::toDisplayString)
            .collect(Collectors.joining("\n"));

        // 3️⃣ Signature (source = CardIdentity)
        String signature = identity.buildSignature();

        // 4️⃣ PublicId (stable, exposable)
        String snapshotPublicId = publicId != null
            ? publicId
            : signature.substring(0, 8);
        UUID snapshotId = UUID.randomUUID();
        return new CardSnapshot(
        		snapshotId,
        	    id, //id de la Carte
        	    snapshotPublicId,
        	    signature,
        	    SnapshotVersion.CURRENT,

        	    cardType,
        	    faction,
        	    energyCost,
        	    attack != null ? attack : 0,
        	    defense != null ? defense : 0,
        	    List.copyOf(keywords),   // ✅ ICI
        	    effectSnapshots,

        	    name,
        	    rulesText,
        	    null,

        	    buildIllustrationPromptBase(),
        	    buildFrameId(),

        	    Instant.now(),
        	    "card-generator-1.0",
        	    SnapshotVersion.CURRENT
        	);

    }

    // ==================================================
    // RENDU (placeholder volontaire)
    // ==================================================
    private String buildIllustrationPromptBase() {
        return name + ", " + faction + " " + cardType;
    }

    private String buildFrameId() {
        return cardType.name().toLowerCase();
    }
}
