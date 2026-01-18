package aiGenerated;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ComfyUIHistoryParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ComfyUIHistoryParser() {}

    public static String extractImageFilename(
        String historyJson,
        String promptId
    ) {

        try {
            JsonNode root = MAPPER.readTree(historyJson);
            JsonNode promptNode = root.get(promptId);
            if (promptNode == null) return null;

            JsonNode outputs = promptNode.get("outputs");
            if (outputs == null) return null;

            for (JsonNode output : outputs) {
                JsonNode images = output.get("images");
                if (images != null && images.isArray() && images.size() > 0) {
                    return images.get(0).get("filename").asText();
                }
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to parse ComfyUI history JSON",
                e
            );
        }
    }
}

