package unics;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.ObjectFit;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;


public class BoosterPdfExporterStyled {

    // Magic size
   //O private static final float CARD_WIDTH = 178.6f;
    private static final float CARD_HEIGHT = 249.4f;
    static final float ILLUSTRATION_HEIGHT = 80f;
    static final float ILLUSTRATION_WIDTH = 100f;
    // Base colors
    
    private static final String CARD_BACK_PATH =
    	    "C:/Users/fabie/eclipse-workspace/unics/resources/images/CarteDos.png";

    
    private static final String ICON_PATH = "resources/images/icons/";

    private static final String ICON_ENERGY = ICON_PATH + "energy.png";
    private static final String ICON_ATTACK = ICON_PATH + "attack.png";
    private static final String ICON_DEFENSE = ICON_PATH + "defense.png";
    
    //private static final float BG_OPACITY = 0.06f;
    private static final float STATS_HEIGHT = 26f;
    
 // Compensation mécanique imprimante (verso uniquement)
    private static final float BACK_OFFSET_X = -8.5f; // -3 mm
    private static final float BACK_OFFSET_Y =  -2.8f; // +1 mm

    
    public static void exportBoosterToPdf(
            Booster cards,
            String outputPath,
            List<Path> imagePaths) {

        try {

            WriterProperties props = new WriterProperties()
                    .setCompressionLevel(9)
                    .setFullCompressionMode(true);

            PdfWriter writer = new PdfWriter(outputPath, props);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(18, 18, 18, 18);

            PdfFont normal_font = PdfFontFactory.createFont();
            PdfFont bold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            Table pageTable = new Table(UnitValue.createPercentArray(3))
                    .setWidth(UnitValue.createPercentValue(100));

            int index = 0;

            for (Card card : cards.getCards()) {

                /* ───── CELLULE DE PAGE ───── */
                Cell pageCell = new Cell()
                        .setPadding(4)
                        .setHeight(CARD_HEIGHT + 8)
                        .setVerticalAlignment(VerticalAlignment.TOP);

                /* ───── CARTE ───── */
                Div cardDiv = new Div()
                        .setHeight(CARD_HEIGHT)
                        .setPadding(3)
                        .setBorder(new SolidBorder(1));

                Div innerFrame = new Div()
                        .setBorder(new SolidBorder(
                                borderColorForFaction(card.getFaction()), 1));

                cardDiv.add(innerFrame);
                pageCell.add(cardDiv);
                pageTable.addCell(pageCell);

                /* ───── HEADER ───── */
                Table header = new Table(UnitValue.createPercentArray(new float[]{85, 15}))
                        .setWidth(UnitValue.createPercentValue(100))
                        .setBackgroundColor(colorForFaction(card.getFaction()));

                header.addCell(new Cell()
                        .add(new Paragraph(shortName(card.getName()))
                                .setFont(normal_font)
                                .setFontSize(11))
                        .setBorder(Border.NO_BORDER));

                Table energyTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                        .setWidth(UnitValue.createPercentValue(100));

                energyTable.addCell(new Cell()
                        .add(icon(ICON_ENERGY, 9))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(Border.NO_BORDER));

                energyTable.addCell(new Cell()
                        .add(new Paragraph(String.valueOf(card.getEnergyCost()))
                                .setFont(normal_font)
                                .setFontSize(10)
                                .setFontColor(ColorConstants.WHITE))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(Border.NO_BORDER));

                header.addCell(new Cell()
                        .add(energyTable)
                        .setBackgroundColor(new DeviceRgb(60, 60, 60))
                        .setBorder(Border.NO_BORDER));

                innerFrame.add(header);

                /* ───── ILLUSTRATION ───── */
                Div illustrationBox = new Div()
                        .setHeight(ILLUSTRATION_HEIGHT)
                        .setBorder(new SolidBorder(0.5f));

                if (index < imagePaths.size() && imagePaths.get(index) != null) {
                    ImageData imgData = loadOptimizedImage(
                            imagePaths.get(index).toAbsolutePath().toString(),
                            900,
                            500,
                            0.8f);

                    Image img = new Image(imgData);
                    img.setWidth(UnitValue.createPercentValue(100));
                    img.setHeight(UnitValue.createPercentValue(100));
                    img.setProperty(Property.OBJECT_FIT, ObjectFit.COVER);
                    illustrationBox.add(img);
                }

                innerFrame.add(illustrationBox);

                /* ───── RULES + FOOTER (TABLE VERTICALE) ───── */
                float RULES_ZONE_HEIGHT =
                        CARD_HEIGHT
                      - ILLUSTRATION_HEIGHT
                      - STATS_HEIGHT
                      ; // header + marges

                Table rulesAndFooter = new Table(UnitValue.createPercentArray(1))
                        .setWidth(UnitValue.createPercentValue(100))
                        .setHeight(RULES_ZONE_HEIGHT);

                /* ───── RULES CELL ───── */
                Cell rulesCell = new Cell()
                        .setBorder(Border.NO_BORDER)
                        //.setHeight()
                        .setBackgroundColor(backgroundColorForFaction(card.getFaction()))
                        .setPadding(4)
                        .setVerticalAlignment(VerticalAlignment.TOP);

                if (!card.getKeywords().isEmpty()) {
                    String kw = card.getKeywords().stream()
                            .map(Keyword::getDisplayName)
                            .reduce((a, b) -> a + " · " + b)
                            .orElse("");
                    Paragraph kwPara = new Paragraph()
                            .setFontSize(8)
                            .setMarginBottom(4);
                    Text kwText = new Text(kw)
                            .setFont(bold);   // 👈 ICI
                    kwPara.add(kwText);
                    rulesCell.add(kwPara);
                    /*
                    rulesCell.add(new Paragraph(kw)
                            .setFont(bold)
                            .setFontSize(8)
                            .setMarginBottom(4));*/
                }

                int totalLen = card.getEffects().stream()
                        .mapToInt(e -> e.toDisplayString().length())
                        .sum();

                float effectFontSize = adaptiveFontSize(totalLen);

                for (CardEffect effect : card.getEffects()) {
                    Paragraph p = new Paragraph()
                            .setFontSize(effectFontSize)
                            .setMarginBottom(3);

                    p.add(new Text(effect.getTrigger()
                            .getShortDisplay(effect.getConditionKeyword()))
                            .setUnderline());
                    p.add(" ");

                    for (String mot : effect.getEffectText().split(" ")) {
                        switch (mot) {
                            case "énergie", "énergies" ->
                                    p.add(createIcon("resources/images/icons/energy.png"));
                            case "dégât", "dégâts" ->
                                    p.add(createIcon("resources/images/icons/hit.png"));
                            case "PC" ->
                                    p.add(createIcon("resources/images/icons/PC.png"));
                            default ->
                                    p.add(new Text(mot));
                        }
                        p.add(" ");
                    }
                    rulesCell.add(p);
                }

                /* ───── FOOTER CELL ───── */
                Cell footerCell = new Cell()
                        .setHeight(STATS_HEIGHT)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(0);

                footerCell.add(buildStatsTable(card, normal_font));

                rulesAndFooter.addCell(rulesCell);
                rulesAndFooter.addCell(footerCell);

                innerFrame.add(rulesAndFooter);

                index++;

                if (index % 9 == 0) {
                    document.add(pageTable);
                    document.add(new AreaBreak());
                    addBackPage(document);
                    if (index < cards.getCards().size()) {
                    	document.add(new AreaBreak());
                    	pageTable = new Table(UnitValue.createPercentArray(3))
                            .setWidth(UnitValue.createPercentValue(100));
                    }
                    
                }
            }
            

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }
    }



    private static Table buildStatsTable(Card card, PdfFont bold) throws IOException {

        if (card.getCardType() == CardType.UNIT) {

            Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setHeight(STATS_HEIGHT);

            t.addCell(statCellIcon(ICON_ATTACK, card.getAttack(), bold));
            t.addCell(statCellIcon(ICON_DEFENSE, card.getDefense(), bold));

            t.addCell(new Cell()
                    .add(new Paragraph(card.getFaction() + "\nUNIT")
                            .setFont(bold)
                            .setFontSize(6)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setPadding(4));

            return t;
        }

        if (card.getCardType() == CardType.STRUCTURE) {

            Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setHeight(STATS_HEIGHT);

            t.addCell(statCellIcon(ICON_DEFENSE, card.getDefense(), bold));

            t.addCell(new Cell()
                    .add(new Paragraph(card.getFaction() + "\nSTRUCTURE")
                            .setFont(bold)
                            .setFontSize(6)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setPadding(4));

            return t;
        }

        // ACTION (ou autre)
        Table t = new Table(UnitValue.createPercentArray(1))
                .setWidth(UnitValue.createPercentValue(100))
                .setHeight(STATS_HEIGHT);

        t.addCell(new Cell()
                .add(new Paragraph(card.getFaction() + "\n" + card.getCardType())
                        .setFont(bold)
                        .setFontSize(6)
                        .setTextAlignment(TextAlignment.CENTER))
                .setPadding(4));

        return t;
    }


    
 

    private static Color colorForFaction(Faction faction) {
        return switch (faction) {
            case ASTRAL -> new DeviceRgb(154, 169, 232);
            case ORGANIC -> new DeviceRgb(143, 207, 155);
            case MECHANICAL -> new DeviceRgb(176, 176, 176);
            case OCCULT -> new DeviceRgb(180, 138, 199);
            case NOMAD -> new DeviceRgb(214, 192, 141);
        };
    }
    
    private static String shortName(String name) {
        int idx = name.lastIndexOf("-");
        return (idx > 0) ? name.substring(0, idx) : name;
    }
    private static float adaptiveFontSize(int totalLength) {
        if (totalLength > 200) return 6f;
        if (totalLength > 150) return 6.5f;
        if (totalLength > 110) return 7f;
        return 8f;
    }
    
    private static void addBackPage(Document document) throws IOException {

        Table backTable = new Table(UnitValue.createPercentArray(3))
        		
        		// 🎯 DÉCALAGE GLOBAL DU VERSO
                .setMarginLeft(BACK_OFFSET_X)
                .setMarginTop(BACK_OFFSET_Y)
                
                .setWidth(UnitValue.createPercentValue(100));

        ImageData backData = loadCardBackImageData();

        

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Cell cell = new Cell()
                        .setPadding(4)
                        .setHeight(CARD_HEIGHT + 8)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);

                Div cardDiv = new Div()
                        .setHeight(CARD_HEIGHT)
                        /*
                        // 🎯 compensation imprimante
                        .setMarginLeft(BACK_OFFSET_X)
                        .setMarginTop(BACK_OFFSET_Y)
                        */
                        .setBorder(new SolidBorder(1));

                // ✔ Nouvelle instance Image (obligatoire en iText)
                Image img = createCardBack(backData);

                cardDiv.add(img);
                cell.add(cardDiv);
                backTable.addCell(cell);
            }
        }

        document.add(backTable);
    }
    private static ImageData CARD_BACK_DATA;

    private static ImageData loadCardBackImageData() throws IOException {
        if (CARD_BACK_DATA == null) {
            CARD_BACK_DATA = loadOptimizedImage(
                    CARD_BACK_PATH,
                    900,
                    1250,
                    0.8f
            );
        }
        return CARD_BACK_DATA;
    }
    private static Image createCardBack(ImageData data) {
        Image img = new Image(data);

        // ⚠️ NE PAS fixer de taille absolue
        img.setWidth(UnitValue.createPercentValue(100));
        img.setHeight(UnitValue.createPercentValue(100));

        img.setProperty(Property.OBJECT_FIT, ObjectFit.COVER);
        img.setBorder(Border.NO_BORDER);

        return img;
    }


    private static Image icon(String path, float size) throws IOException {
        ImageData data = ImageDataFactory.create(path);
        Image img = new Image(data);
        img.setWidth(size);
        img.setHeight(size);
        img.setAutoScale(false);
        img.setBorder(Border.NO_BORDER);
        return img;
    }
    private static Cell statCellIcon(String iconPath, int value, PdfFont font) throws IOException {

        Paragraph p = new Paragraph()
            .setTextAlignment(TextAlignment.CENTER)
            .setMargin(0)
            .setFont(font)
            .setFontSize(11);

        p.add(icon(iconPath, 12));
        p.add(" ");
        p.add(String.valueOf(value));

        return new Cell()
        	    .setBorder(new SolidBorder(0.5f))
        	    .setBackgroundColor(new DeviceRgb(245, 245, 245))
        	    .setPadding(4)
        	    .add(p);

    }

    private static Color borderColorForFaction(Faction faction) {
        return switch (faction) {
            case ASTRAL -> new DeviceRgb(90, 110, 180);
            case ORGANIC -> new DeviceRgb(90, 150, 100);
            case MECHANICAL -> new DeviceRgb(120, 120, 120);
            case OCCULT -> new DeviceRgb(120, 90, 150);
            case NOMAD -> new DeviceRgb(160, 140, 100);
        };
    }
    private static Color backgroundColorForFaction(Faction faction) {
        return switch (faction) {
            case ASTRAL -> new DeviceRgb(225, 230, 245);
            case ORGANIC -> new DeviceRgb(225, 240, 230);
            case MECHANICAL -> new DeviceRgb(235, 235, 235);
            case OCCULT -> new DeviceRgb(235, 225, 245);
            case NOMAD -> new DeviceRgb(245, 240, 225);
        };
    }



    private static ImageData loadOptimizedImage(
            String path,
            int targetWidth,
            int targetHeight,
            float jpegQuality) throws IOException {

        BufferedImage src = ImageIO.read(new File(path));

        BufferedImage resized = new BufferedImage(
                targetWidth,
                targetHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(src, 0, 0, targetWidth, targetHeight, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(jpegQuality); // 0.75–0.85

        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new IIOImage(resized, null, null), param);
        writer.dispose();

        return ImageDataFactory.create(baos.toByteArray());
    }

   


    private static Image createIcon(String path) throws MalformedURLException {
        Image img = new Image(ImageDataFactory.create(path));
        img.setWidth(10);
        img.setHeight(10);
        img.setMarginRight(2);
        img.setRelativePosition(0, -2f, 0, 0);//décalage en bas de l'image
        return img;
    }




}
