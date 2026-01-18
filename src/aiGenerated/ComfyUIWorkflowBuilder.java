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

            // 1️⃣ Checkpoint
            prompt.putObject("1")
                .put("class_type", "CheckpointLoaderSimple")
                .putObject("inputs")
                .put("ckpt_name", "v1-5-pruned-emaonly.safetensors");

            // 2️⃣ Positive prompt
            ObjectNode pos = prompt.putObject("2");
            pos.put("class_type", "CLIPTextEncode");
            pos.putObject("inputs")
                .put("text", render.prompt)
                .putArray("clip")
                .add("1").add(1);

            // 3️⃣ Negative prompt
            ObjectNode neg = prompt.putObject("3");
            neg.put("class_type", "CLIPTextEncode");
            neg.putObject("inputs")
                .put("text", render.negativePrompt)
                .putArray("clip")
                .add("1").add(1);

            // 4️⃣ Latent
            prompt.putObject("4")
                .put("class_type", "EmptyLatentImage")
                .putObject("inputs")
                .put("width", 768)
                .put("height", 512)
                .put("batch_size", 1);

            // 5️⃣ Sampler
            ObjectNode sampler = prompt.putObject("5");
            sampler.put("class_type", "KSampler");
            ObjectNode sInputs = sampler.putObject("inputs");

            sInputs.putArray("model").add("1").add(0);
            sInputs.putArray("positive").add("2").add(0);
            sInputs.putArray("negative").add("3").add(0);
            sInputs.putArray("latent_image").add("4").add(0);

            sInputs.put("steps", render.renderProfile.steps);
            sInputs.put("cfg", render.renderProfile.cfgScale);
            sInputs.put("sampler_name", "euler");
            sInputs.put("scheduler", "normal");
            sInputs.put("seed", render.seed);
            sInputs.put("denoise", 1.0);

            // 6️⃣ Decode
            ObjectNode decode = prompt.putObject("6");
            decode.put("class_type", "VAEDecode");

            ObjectNode decodeInputs = decode.putObject("inputs");
            decodeInputs.putArray("samples").add("5").add(0);
            decodeInputs.putArray("vae").add("1").add(2);


            // 7️⃣ Save
            ObjectNode save = prompt.putObject("7");
            save.put("class_type", "SaveImage");

            ObjectNode saveInputs = save.putObject("inputs");
            saveInputs.putArray("images").add("6").add(0);
            saveInputs.put("filename_prefix", "card");


            return MAPPER.writeValueAsString(root);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build ComfyUI workflow JSON", e);
        }
    }
}
