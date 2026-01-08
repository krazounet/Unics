package aiGenerated;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ComfyUISimpleClient {

    public static void main(String[] args) throws Exception {

        String promptText = "a small cozy cabin in the forest, cinematic lighting";

        String json = """
        {
          "prompt": {
            "1": {
              "class_type": "CheckpointLoaderSimple",
              "inputs": {
                "ckpt_name": "v1-5-pruned-emaonly.safetensors"
              }
            },
            "2": {
              "class_type": "CLIPTextEncode",
              "inputs": {
                "text": "%s",
                "clip": ["1", 1]
              }
            },
            "3": {
              "class_type": "EmptyLatentImage",
              "inputs": {
                "width": 384,
                "height": 384,
                "batch_size": 1
              }
            },
            "4": {
              "class_type": "KSampler",
              "inputs": {
                "model": ["1", 0],
                "positive": ["2", 0],
                "negative": ["2", 0],
                "latent_image": ["3", 0],
                "steps": 15,
                "cfg": 6,
                "sampler_name": "euler",
                "scheduler": "normal",
                "seed": 0,
                "denoise": 1.0
              }
            },
            "5": {
              "class_type": "VAEDecode",
              "inputs": {
                "samples": ["4", 0],
                "vae": ["1", 2]
              }
            },
            "6": {
              "class_type": "SaveImage",
              "inputs": {
                "images": ["5", 0],
                "filename_prefix": "java_test"
              }
            }
          }
        }
        """.formatted(promptText.replace("\"", "\\\""));

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8188/prompt"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Réponse ComfyUI :");
        System.out.println(response.body());
    }
}


