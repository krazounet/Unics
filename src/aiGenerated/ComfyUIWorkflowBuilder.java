package aiGenerated;


import java.util.HashMap;
import java.util.Map;



public final class ComfyUIWorkflowBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ComfyUIWorkflowBuilder() {}

    public static String buildPayload(CardRender render) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("prompt", render.prompt);
        payload.put("negative_prompt", render.negativePrompt);
        payload.put("seed", render.seed);
        payload.put("steps", render.renderProfile.steps);
        payload.put("cfg", render.renderProfile.cfgScale);

        payload.put("workflow_id", render.workflowId);

        try {
            return MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ComfyUI payload", e);
        }
    }
}
