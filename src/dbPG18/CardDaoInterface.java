package dbPG18;

import unics.Card;
import unics.CardIdentity;

import java.util.Optional;

public interface CardDaoInterface {

    Optional<Card> findByIdentity(CardIdentity identity);

    Card saveIfNotExists(Card card);

    Optional<Card> findByPublicId(String publicId);
}
