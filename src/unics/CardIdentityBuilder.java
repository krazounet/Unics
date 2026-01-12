package unics;

import java.util.ArrayList;
import java.util.List;

import unics.Enum.CardType;
import unics.Enum.Faction;

public class CardIdentityBuilder {

    private CardType type;
    private Faction faction;
    private int energyCost;
    private int atk;
    private int def;
    private List<String> keywords = new ArrayList<>();
    private List<CardEffect> effects = new ArrayList<>();

    public CardIdentityBuilder type(CardType type) {
        this.type = type;
        return this;
    }

    public CardIdentityBuilder faction(Faction faction) {
        this.faction = faction;
        return this;
    }

    public CardIdentityBuilder cost(int cost) {
        this.energyCost = cost;
        return this;
    }

    public CardIdentityBuilder atk(int atk) {
        this.atk = atk;
        return this;
    }

    public CardIdentityBuilder def(int def) {
        this.def = def;
        return this;
    }

    public CardIdentityBuilder addKeyword(String keyword) {
        this.keywords.add(keyword);
        return this;
    }

    public CardIdentityBuilder addEffect(CardEffect cardeffect) {
        this.effects.add(cardeffect);//attention ici, faire un identity peut etre pour CardEffect
        return this;
    }

    public CardIdentity build() {
        return new CardIdentity(
            type, faction, energyCost, atk, def, keywords, effects
        );
    }
}
