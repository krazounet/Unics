package aiGenerated;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ComfyUIWaitForResult {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {

        String promptId = "f328aaa3-5c79-4704-aa92-3db406185f9c";

        String filename = waitForImage(promptId);

        System.out.println("Image générée : " + filename);
        System.out.println("Chemin local : ComfyUI/output/" + filename);
    }

    private static String waitForImage(String promptId) throws Exception {

        String url = "http://127.0.0.1:8188/history/" + promptId;

        while (true) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            // Génération pas encore terminée
            if (body == null || body.equals("{}") || !body.contains("\"images\"")) {
                Thread.sleep(500); // polling propre
                continue;
            }

            // On extrait simplement le filename (version simple)
            int index = body.indexOf("\"filename\"");
            if (index != -1) {
                int start = body.indexOf("\"", index + 11) + 1;
                int end = body.indexOf("\"", start);
                return body.substring(start, end);
            }

            Thread.sleep(500);
        }
    }
}

