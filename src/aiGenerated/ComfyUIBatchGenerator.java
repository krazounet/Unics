package aiGenerated;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class ComfyUIBatchGenerator {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String COMFY_URL = "http://127.0.0.1:8188";
    
    private static final Path COMFY_ROOT =
            Paths.get("C:", "ai", "ComfyUI_windows_portable");

    private static final Path OUTPUT_DIR =
            COMFY_ROOT.resolve("ComfyUI").resolve("output");

    
    
    private static final String NEGATIVE_PROMPT =
    	    "blurry, low quality, low detail, bad anatomy, extra limbs, extra fingers, "
    	  + "deformed hands, distorted face, "
    	  + "text, letters, logo, watermark, signature, "
    	  + "card frame, card border, card layout, "
    	  + "trading card frame, collectible card border, "
    	  + "UI elements, interface, text box, mana cost, rarity gem, "
    	  + "flat lighting, overexposed, underexposed,"
    + "multiple cards, card backs, card grid, card layout, "
    + "collection of cards, deck, tabletop, board game, "
    + "spread of cards, flat lay, ";


    // ------------------------------------------------------------
    // Envoi du prompt
    // ------------------------------------------------------------
    private static String sendPrompt(String positivePrompt) throws Exception {

        long seed = System.currentTimeMillis() % Integer.MAX_VALUE;

        String json = """
        		{
        		  "prompt": {
        		    "1": {
        		      "class_type": "CheckpointLoaderSimple",
        		      "inputs": {
        		        "ckpt_name": "sd_xl_base_1.0.safetensors"
        		      }
        		    },

"2": {
  "class_type": "CLIPTextEncodeSDXL",
  "inputs": {
    "text_g": "%s",
    "text_l": "%s",
    "width": 1152,
    "height": 768,
    "crop_w": 0,
    "crop_h": 0,
    "target_width": 1152,
    "target_height": 768,
    "clip": ["1", 1]
  }
}
,
"3": {
  "class_type": "CLIPTextEncodeSDXL",
  "inputs": {
    "text_g": "%s",
    "text_l": "%s",
    "width": 1152,
    "height": 768,
    "crop_w": 0,
    "crop_h": 0,
    "target_width": 1152,
    "target_height": 768,
    "clip": ["1", 1]
  }
}
,



        		    "4": {
        		      "class_type": "EmptyLatentImage",
        		      "inputs": {
        		        "width": 896,
        		        "height": 640,
        		        "batch_size": 1
        		      }
        		    },

        		    "5": {
        		      "class_type": "KSampler",
        		      "inputs": {
        		        "model": ["1", 0],
        		        "positive": ["2", 0],
        		        "negative": ["3", 0],
        		        "latent_image": ["4", 0],
        		        "steps": 16,
        		        "cfg": 6,
        		        "sampler_name": "euler",
        		        "scheduler": "karras",
        		        "seed": %d,
        		        "denoise": 1.0
        		      }
        		    },

        		    "6": {
        		      "class_type": "VAEDecode",
        		      "inputs": {
        		        "samples": ["5", 0],
        		        "vae": ["1", 2]
        		      }
        		    },

        		    "7": {
        		      "class_type": "SaveImage",
        		      "inputs": {
        		        "images": ["6", 0],
        		        "filename_prefix": "card"
        		      }
        		    }
        		  }
        		}
        		""".formatted(
        		    positivePrompt.replace("\"", "\\\""),   // text_g positif
        		    positivePrompt.replace("\"", "\\\""),   // text_l positif
        		    NEGATIVE_PROMPT.replace("\"", "\\\""),  // text_g négatif
        		    NEGATIVE_PROMPT.replace("\"", "\\\""),  // text_l négatif
        		    seed
        		);


        System.out.println("  → Envoi du prompt à ComfyUI");
        System.out.println("  Positive prompt : " + positivePrompt);
        System.out.println("  Negative prompt : " + NEGATIVE_PROMPT);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(COMFY_URL + "/prompt"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        System.out.println("  ← Réponse brute : " + body);

        return extractPromptId(body);
    }


    // ------------------------------------------------------------
    // Attente de la génération
    // ------------------------------------------------------------

    private static String waitForImage(String promptId) throws Exception {

        String historyUrl = COMFY_URL + "/history/" + promptId;

        while (true) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(historyUrl))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            // parse JSON
            JsonObject root = JsonParser.parseString(body).getAsJsonObject();

            if (!root.has(promptId)) {
                Thread.sleep(500);
                continue;
            }

            JsonObject promptData = root.getAsJsonObject(promptId);

            JsonObject status = promptData.getAsJsonObject("status");
            boolean completed = status.get("completed").getAsBoolean();

            if (!completed) {
                Thread.sleep(500);
                continue;
            }

            // 🎯 EXTRACTION DU FICHIER IMAGE
            JsonObject outputs = promptData.getAsJsonObject("outputs");

            for (Map.Entry<String, JsonElement> entry : outputs.entrySet()) {
                JsonObject outputNode = entry.getValue().getAsJsonObject();

                if (outputNode.has("images")) {
                    JsonArray images = outputNode.getAsJsonArray("images");

                    if (images.size() > 0) {
                        JsonObject image = images.get(0).getAsJsonObject();
                        return image.get("filename").getAsString();
                    }
                }
            }

            throw new IllegalStateException("Aucune image trouvée pour prompt " + promptId);
        }
    }


    // ------------------------------------------------------------
    // Parsing robuste du prompt_id
    // ------------------------------------------------------------

    private static String extractPromptId(String body) {

        if (body == null) {
            throw new RuntimeException("Réponse ComfyUI nulle");
        }

        int keyIndex = body.indexOf("\"prompt_id\"");
        if (keyIndex == -1) {
            throw new RuntimeException("prompt_id introuvable dans : " + body);
        }

        // Trouver le premier guillemet après les :
        int firstQuote = body.indexOf("\"", keyIndex + 11);
        int secondQuote = body.indexOf("\"", firstQuote + 1);

        if (firstQuote == -1 || secondQuote == -1) {
            throw new RuntimeException("Format prompt_id invalide : " + body);
        }

        return body.substring(firstQuote + 1, secondQuote);
    }
    public static List<Path> generateImages(List<String> prompts) throws Exception {
    	long start = System.nanoTime();
        List<Path> generatedImagePaths = new ArrayList<>();

        int total = prompts.size();

        for (int i = 0; i < total; i++) {

            String promptText = prompts.get(i);

            System.out.printf("▶ Génération image %d/%d%n", i + 1, total);

            String promptId = sendPrompt(promptText);
            System.out.println("  prompt_id = " + promptId);

            String filename = waitForImage(promptId);
            System.out.println("  image générée = " + filename);

            Path imagePath = OUTPUT_DIR.resolve(filename).toAbsolutePath();

            if (!Files.exists(imagePath)) {
                throw new IllegalStateException(
                    "Image générée introuvable : " + imagePath
                );
            }

            generatedImagePaths.add(imagePath);
        }
        long end = System.nanoTime();
    	System.out.println("Temps d'exécution : " + (end-start)/1000000000 + " s");
        return generatedImagePaths;
    }

}
