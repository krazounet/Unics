package dbPG18;

import java.util.UUID;

import unics.Enum.CardType;
import unics.Enum.Faction;

public final class CardDbRow {

    public final UUID id;
    public final String identityHash;
    public final String publicId;
    public final String name;

    public final CardType cardType;
    public final Faction faction;

    public final int energyCost;
    public final Integer attack;
    public final Integer defense;

    public final int powerScore;

    public CardDbRow(
        UUID id,
        String identityHash,
        String publicId,
        String name,
        CardType cardType,
        Faction faction,
        int energyCost,
        Integer attack,
        Integer defense,
        int powerScore
    ) {
        this.id = id;
        this.identityHash = identityHash;
        this.publicId = publicId;
        this.name = name;
        this.cardType = cardType;
        this.faction = faction;
        this.energyCost = energyCost;
        this.attack = attack;
        this.defense = defense;
        this.powerScore = powerScore;
    }
    
    
}
