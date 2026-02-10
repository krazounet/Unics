package unics;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;

public final class CardBuilder {

    private UUID id;
    private String publicId;
    private String name;
    private Faction faction;
    private CardType type;
    private Set<Keyword> keywords;
    private List<CardEffect> effects;
    private Integer attack;
    private Integer defense;
    private int energyCost;
    private int powerScore;
    
    public CardBuilder() {
        // valeurs par défaut
        //this.id = UUID.randomUUID();
    	
    }
    public CardBuilder withRandomUuid() {
    	this.id = UUID.randomUUID();
    	return this;
    }
    public CardBuilder withId(UUID id) {
    	this.id=id;
    	return this;
    }
    public CardBuilder withGeneratePublicID() {
    	this.publicId=PublicIdGenerator.fromUuid(id);
    	return this;
    }
    public CardBuilder withPublicId(String publicId) {
    	this.publicId=publicId;
    	return this;
    }
    public CardBuilder withGeneratename() {
    	this.name=CardNameGenerator.generateName(faction, type, publicId, keywords);
    	return this;
    }
    public CardBuilder withName(String name) {
    	this.name=name;
    	return this;
    }
    public CardBuilder withFaction(Faction faction) {
        this.faction = faction;
        return this;
    }

    public CardBuilder withType(CardType type) {
        this.type = type;
        return this;
    }

    public CardBuilder withKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
        return this;
    }

    public CardBuilder withEffects(List<CardEffect> effects) {
        this.effects = effects;
        return this;
    }

    public CardBuilder withAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public CardBuilder withDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public CardBuilder withEnergyCost(int cost) {
        this.energyCost = cost;
        return this;
    }
    public CardBuilder withPowerScore(int score) {
        this.powerScore = score;
        return this;
    }
    /**
     * Génère la carte complète avec :
     * - publicId
     * - nom généré automatiquement
     * - calcul du powerScore
     */
    public Card build() {
        if (faction == null) {
            throw new IllegalStateException("Faction doit être définie");
        }
        if (type == null) {
            throw new IllegalStateException("CardType doit être défini");
        }
        //this.id = UUID.randomUUID();
        //String publicId = PublicIdGenerator.fromUuid(id);
        //String name = CardNameGenerator.generateName(faction, type, publicId, keywords);

     // 1️⃣ Construire l'identité gameplay
        CardIdentity identity = new CardIdentity(
            type,
            faction,
            energyCost,
            attack != null ? attack : 0,
            defense != null ? defense : 0,
            keywords != null
                ? keywords.stream().map(Enum::name).toList()
                : List.of(),
            effects != null ? effects : List.of()
        );

        // Construire la carte finale
        return new Card(
                id,
                publicId,
                identity,
                name,
                type,
                faction,
                energyCost,
                attack,
                defense,
                keywords != null ? keywords : Set.of(),
                effects != null ? effects : List.of(),
                powerScore
        );
    }
}