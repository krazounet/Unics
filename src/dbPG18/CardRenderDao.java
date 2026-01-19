package dbPG18;

import java.time.Instant;
import java.util.UUID;

import aiGenerated.CardRender;

public interface CardRenderDao {

    void insert(CardRender render);

    void markDone(
        UUID renderId,
        String imagePath,
        Instant finishedAt
    );

    void markFailed(
        UUID renderId,
        String errorMessage,
        Instant finishedAt
    );

    CardRender findById(UUID renderId);
}
