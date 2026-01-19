package dbPG18;

import java.util.UUID;

import unics.snapshot.CardSnapshot;

public interface CardSnapshotDao {

    void insert(CardSnapshot snapshot);

    CardSnapshot findBySignature(String signature);

    CardSnapshot findById(UUID id);
}
