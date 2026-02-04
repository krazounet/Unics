package aiGenerated;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public final class ComfyUIClient {

    private final HttpClient client;
    private final String baseUrl;

    public ComfyUIClient(String baseUrl) {
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        this.baseUrl = baseUrl;
    }

    public String sendPrompt(String jsonPayload) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/prompt"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("ComfyUI error: " + response.body());
        }

        return response.body();
    }

    public String getHistory(String promptId) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/history/" + promptId))
            .GET()
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("ComfyUI history error");
        }

        return response.body();
    }
}

