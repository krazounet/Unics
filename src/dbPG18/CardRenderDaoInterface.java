package dbPG18;

import java.time.Instant;
import java.util.UUID;

import aiGenerated.CardRender;
import aiGenerated.RenderProfile;

public interface CardRenderDaoInterface {

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

	CardRender findByVisualSignature(String visualSignature, RenderProfile profile);
}
