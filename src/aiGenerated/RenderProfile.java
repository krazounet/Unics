package aiGenerated;


public enum RenderProfile {

    DEFAULT(
        // 🎨 Style positif
        "epic fantasy illustration, high detail, dramatic lighting, "
      + "cinematic composition, dynamic pose, professional concept art, "
      + "digital painting, sharp focus",

        // 🚫 Négatifs STRICTS
        "multiple characters,  "
        + "group,  "
        + "crowd,  "
        + "duplicates,  "
        + "clones,  "
        + "same character twice,  "
        + "character sheet,  "
        + "turnaround,  "
        + "lineup,  "
        + "reference sheet,  "
        + "concept art,  "
        + "poses,  "
        + "sketch,  "
        + "draft,  "
        + "low quality,  "
        + "blurry,  "
        + "cropped,  "
        + "out of frame,  "
        + "text,  "
        + "logo,  "
        + "watermark,  "
        + "ui,  "
        + "frame,  "
        + "border  "
        
      + "",

        // 🤖 Workflow / modèle
        "comfyui-fantasy-v1",

        // 🖼️ Cadrage
        "1:1",

        // 🔢 Paramètres techniques
        16,   // steps !! repasser à 30
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
