package unics;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

import unics.Enum.CardType;
import unics.Enum.Faction;

public final class CardIdentity {

    public static final int GENERATION_VERSION = 1;

    private final CardType type;
    private final Faction faction;
    private final int energyCost;
    private final int atk;
    private final int def;
    private final List<String> keywords;
    private final List<CardEffect> effects;

    public CardIdentity(
        CardType type,
        Faction faction,
        int energyCost,
        int atk,
        int def,
        List<String> keywords,
        List<CardEffect> effects
    ) {
        this.type = Objects.requireNonNull(type);
        this.faction = Objects.requireNonNull(faction);
        this.energyCost = energyCost;
        this.atk = atk;
        this.def = def;
        this.keywords = List.copyOf(keywords == null ? List.of() : keywords);
        this.effects = List.copyOf(effects == null ? List.of() : effects);
    }

    public String computeHash() {
        return DigestUtils.sha256Hex(buildSignature());
    }

    private String buildSignature() {

        String keywordsPart = keywords.stream()
            .map(k -> k.trim().toLowerCase())
            .sorted()
            .collect(Collectors.joining(","));

        String effectsPart = effects.stream()
        	    .map(CardEffect::toIdentityString)
        	    .sorted()
        	    .collect(Collectors.joining("|"));


        return String.join(";",
            "V=" + GENERATION_VERSION,
            "TYPE=" + type.name(),
            "FACTION=" + faction.name(),
            "COST=" + energyCost,
            "ATK=" + atk,
            "DEF=" + def,
            "KEYWORDS=" + keywordsPart,
            "EFFECTS=" + effectsPart
        );
    }
    @Override
    public String toString() {
        return computeHash(); // ou le hash final
    }
    /**
     * Représentation lisible de l'identité.
     * ⚠️ Destinée uniquement au debug / logs / inspection humaine.
     * ⚠️ Ne pas utiliser pour la persistance ou les comparaisons.
     */
    public String debugSignature() {
        return buildSignature();
    }
}
