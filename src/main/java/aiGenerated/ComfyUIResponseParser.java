package aiGenerated;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ComfyUIResponseParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ComfyUIResponseParser() {}

    public static String extractPromptId(String responseJson) {
        try {
            JsonNode node = MAPPER.readTree(responseJson);
            return node.get("prompt_id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Invalid ComfyUI /prompt response", e);
        }
    }
}
