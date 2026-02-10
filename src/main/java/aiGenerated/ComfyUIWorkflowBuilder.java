package aiGenerated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class ComfyUIWorkflowBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ComfyUIWorkflowBuilder() {}

    public static String buildPayload(CardRender render) {

        try {
            ObjectNode root = MAPPER.createObjectNode();
            ObjectNode prompt = root.putObject("prompt");

            // ------------------------------------------------------------
            // 1️⃣ Checkpoint (SDXL)
            // ------------------------------------------------------------
            prompt.putObject("1")
                .put("class_type", "CheckpointLoaderSimple")
                .putObject("inputs")
                .put("ckpt_name", "sd_xl_base_1.0.safetensors");

            // ------------------------------------------------------------
            // 2️⃣ Positive prompt (SDXL)
            // ------------------------------------------------------------
            ObjectNode pos = prompt.putObject("2");
            pos.put("class_type", "CLIPTextEncodeSDXL");

            ObjectNode posInputs = pos.putObject("inputs");
            posInputs.put("text_g", render.prompt);
            posInputs.put("text_l", render.prompt);

            posInputs.put("width", 1024);
            posInputs.put("height", 512);
            posInputs.put("crop_w", 0);
            posInputs.put("crop_h", 0);
            posInputs.put("target_width", 1024);
            posInputs.put("target_height", 512);

            posInputs.putArray("clip")
                     .add("1").add(1);

            // ------------------------------------------------------------
            // 3️⃣ Negative prompt (SDXL)
            // ------------------------------------------------------------
            ObjectNode neg = prompt.putObject("3");
            neg.put("class_type", "CLIPTextEncodeSDXL");

            ObjectNode negInputs = neg.putObject("inputs");
            negInputs.put("text_g", render.negativePrompt);
            negInputs.put("text_l", render.negativePrompt);

            negInputs.put("width", 1024);
            negInputs.put("height", 512);
            negInputs.put("crop_w", 0);
            negInputs.put("crop_h", 0);
            negInputs.put("target_width", 1024);
            negInputs.put("target_height", 512);

            negInputs.putArray("clip")
                     .add("1").add(1);

            // ------------------------------------------------------------
            // 4️⃣ Latent image
            // ------------------------------------------------------------
            prompt.putObject("4")
                .put("class_type", "EmptyLatentImage")
                .putObject("inputs")
                .put("width", 1024)
                .put("height", 512)
                .put("batch_size", 1);

            // ------------------------------------------------------------
            // 5️⃣ Sampler
            // ------------------------------------------------------------
            ObjectNode sampler = prompt.putObject("5");
            sampler.put("class_type", "KSampler");

            ObjectNode sInputs = sampler.putObject("inputs");
            sInputs.putArray("model").add("1").add(0);
            sInputs.putArray("positive").add("2").add(0);
            sInputs.putArray("negative").add("3").add(0);
            sInputs.putArray("latent_image").add("4").add(0);

            sInputs.put("steps", render.renderProfile.steps);   // ex: 16
            sInputs.put("cfg", render.renderProfile.cfgScale); // ex: 6
            sInputs.put("sampler_name", "euler");
            sInputs.put("scheduler", "karras");
            sInputs.put("seed", render.seed);
            sInputs.put("denoise", 1.0);

            // ------------------------------------------------------------
            // 6️⃣ VAE Decode (SDXL)
            // ------------------------------------------------------------
            ObjectNode decode = prompt.putObject("6");
            decode.put("class_type", "VAEDecode");

            ObjectNode decodeInputs = decode.putObject("inputs");
            decodeInputs.putArray("samples").add("5").add(0);
            decodeInputs.putArray("vae").add("1").add(2);

            // ------------------------------------------------------------
            // 7️⃣ Save Image
            // ------------------------------------------------------------
            ObjectNode save = prompt.putObject("7");
            save.put("class_type", "SaveImage");

            ObjectNode saveInputs = save.putObject("inputs");
            saveInputs.putArray("images").add("6").add(0);
            saveInputs.put("filename_prefix", "card");

            return MAPPER.writeValueAsString(root);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build ComfyUI SDXL workflow JSON", e);
        }
    }
}
