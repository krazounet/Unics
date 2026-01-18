package aiGenerated;


public enum RenderProfile {

    DEFAULT(
        // 🎨 Style positif
        "epic fantasy illustration, high detail, dramatic lighting, "
      + "cinematic composition, dynamic pose, professional concept art, "
      + "digital painting, sharp focus",

        // 🚫 Négatifs STRICTS
        "no text, no typography, no letters, no watermark, no logo, "
      + "no UI, no frame, no borders, no card layout, no mockup, "
      + "no screenshot, no photograph, no real people, no low quality",

        // 🤖 Workflow / modèle
        "comfyui-fantasy-v1",

        // 🖼️ Cadrage
        "1:1",

        // 🔢 Paramètres techniques
        10,   // steps !! repasser à 30
        6   // cfgScale !! repasser à 7,5
    );

    // ─────────────────────────────────────────────
    // CHAMPS
    // ─────────────────────────────────────────────

    public final String stylePrompt;
    public final String negativePrompt;
    public final String workflowId;
    public final String aspectRatio;
    public final int steps;
    public final double cfgScale;

    RenderProfile(
        String stylePrompt,
        String negativePrompt,
        String workflowId,
        String aspectRatio,
        int steps,
        double cfgScale
    ) {
        this.stylePrompt = stylePrompt;
        this.negativePrompt = negativePrompt;
        this.workflowId = workflowId;
        this.aspectRatio = aspectRatio;
        this.steps = steps;
        this.cfgScale = cfgScale;
    }
}
