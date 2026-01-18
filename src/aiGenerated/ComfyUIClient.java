package aiGenerated;


import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public final class ComfyUIClient {

    private final HttpClient client;
    private final URI endpoint;

    public ComfyUIClient(String baseUrl) {
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        this.endpoint = URI.create(baseUrl + "/prompt");
    }

    public String sendPrompt(String jsonPayload) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(endpoint)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(
                "ComfyUI error " + response.statusCode() + ": " + response.body()
            );
        }

        return response.body(); // contient prompt_id
    }
}

