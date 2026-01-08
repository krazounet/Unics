package unics;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;

public final class Card {

    // Identité
    private final UUID id;
    private final String publicId;
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
        this.publicId = PublicIdGenerator.fromUuid(id);
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
    public String getName() { return name; }
    public CardType getCardType() { return cardType; }
    public Faction getFaction() { return faction; }
    public int getEnergyCost() { return energyCost; }
    public Integer getAttack() { return attack; }
    public Integer getDefense() { return defense; }
    public Set<Keyword> getKeywords() { return keywords; }
    public List<CardEffect> getEffects() { return effects; }
    public int getPowerScore() { return powerScore; }
}
