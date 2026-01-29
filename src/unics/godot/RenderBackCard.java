package unics.godot;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class RenderBackCard {

    private static final int CANVAS_WIDTH  = 300;
    private static final int CANVAS_HEIGHT = 420;

    // Zone grise EXACTE
    private static final Rectangle ZONE =
        new Rectangle(5, 46, 290, 146);

    private RenderBackCard() {}

    public static BufferedImage renderIllustrationOnly(
            BufferedImage baseRender,
            BufferedImage illustration
    ) {
        BufferedImage output = new BufferedImage(
            CANVAS_WIDTH,
            CANVAS_HEIGHT,
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = output.createGraphics();
        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        // Fond
        g.drawImage(baseRender, 0, 0, null);

        // ── COVER (remplit TOUTE la zone)
        double scaleX = ZONE.width  / (double) illustration.getWidth();
        double scaleY = ZONE.height / (double) illustration.getHeight();
        double scale  = Math.max(scaleX, scaleY);

        int scaledW = (int) Math.ceil(illustration.getWidth()  * scale);
        int scaledH = (int) Math.ceil(illustration.getHeight() * scale);

        BufferedImage scaled = new BufferedImage(
            scaledW, scaledH, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D gs = scaled.createGraphics();
        gs.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        gs.drawImage(
            illustration,
            0, 0, scaledW, scaledH,
            null
        );
        gs.dispose();

        // ── Crop CENTRÉ (zéro gris possible)
        int cropX = (scaledW - ZONE.width)  / 2;
        int cropY = (scaledH - ZONE.height) / 2;

        BufferedImage cropped = scaled.getSubimage(
            cropX,
            cropY,
            ZONE.width,
            ZONE.height
        );

        // Dessin final
        g.drawImage(cropped, ZONE.x, ZONE.y, null);

        g.dispose();
        return output;
    }
}
