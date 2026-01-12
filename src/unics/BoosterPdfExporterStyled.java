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
import com.itextpdf.layout.properties.ObjectFit;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.element.Text;


import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.Enum.TriggerType;

public class BoosterPdfExporterStyled {

    // Magic size
    private static final float CARD_WIDTH = 178.6f;
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

    
    public static void exportBoosterToPdf(Booster cards, String outputPath,List<Path> imagePaths) {

        try {
          
            
            WriterProperties props = new WriterProperties()
                    .setCompressionLevel(9)
                    .setFullCompressionMode(true);

            PdfWriter writer = new PdfWriter(outputPath, props);
            PdfDocument pdf = new PdfDocument(writer);

            
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(18, 18, 18, 18);

            // Polices
            PdfFont bold = PdfFontFactory.createFont();
           

            Table table = new Table(UnitValue.createPercentArray(3))
                    .setWidth(UnitValue.createPercentValue(100));

            int index = 0;

            for (Card card : cards.getCards()) {

                /* ───── Cellule ───── */
            	Cell cell = new Cell()
            		    .setPadding(4)
            		    .setHeight(CARD_HEIGHT + 8)
            		    .setVerticalAlignment(VerticalAlignment.TOP);

                /* ───── Carte (hauteur FIXE) ───── */
               
            	// Cadre externe
            	Div cardDiv = new Div()
            	    .setHeight(CARD_HEIGHT)
            	    .setPadding(3)
            	    .setBorder(new SolidBorder(1));

            	// Cadre interne
            	Div innerFrame = new Div()
            	    .setPadding(0)
            	    .setHeight(UnitValue.createPercentValue(100))
            	    .setBorder(new SolidBorder(
            	    	    borderColorForFaction(card.getFaction()), 1));

            	// Fond teinté (bloc visuel)
            	Div backgroundLayer = new Div()
            	    .setBackgroundColor(colorForFaction(card.getFaction()))
            	    .setOpacity(bgOpacityForFaction(card.getFaction()))
            	    .setMinHeight(UnitValue.createPercentValue(100));

            	// Contenu (NE PAS fixer la hauteur)
            	Div contentLayer = new Div()
            		    .setPadding(2)
            		    ;
            	
            	// Ordre
            	innerFrame.add(backgroundLayer);
            	innerFrame.add(contentLayer);
            	cardDiv.add(innerFrame);


                /* ───── Header ───── */
               
                Table header = new Table(UnitValue.createPercentArray(new float[]{85, 15}))
                	    .setWidth(UnitValue.createPercentValue(100));

                header.addCell(new Cell()
                        .add(new Paragraph(shortName(card.getName()))
                                .setFont(bold)
                                .setFontSize(11))
                        .setBorder(Border.NO_BORDER));
                Color factionColor = colorForFaction(card.getFaction());

                header.setBackgroundColor(factionColor);


                Cell energyCell = new Cell()
                	    .setBorder(Border.NO_BORDER)
                	    .setBackgroundColor(new DeviceRgb(60, 60, 60))
                	    .setPadding(0)
                	    .setVerticalAlignment(VerticalAlignment.MIDDLE);

                	Table energyTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                	    .setWidth(UnitValue.createPercentValue(100));

                	energyTable.addCell(new Cell()
                	    .setBorder(Border.NO_BORDER)
                	    .setTextAlignment(TextAlignment.CENTER)
                	    .add(icon(ICON_ENERGY, 9)));

                	energyTable.addCell(new Cell()
                	    .setBorder(Border.NO_BORDER)
                	    .setTextAlignment(TextAlignment.CENTER)
                	    .add(new Paragraph(String.valueOf(card.getEnergyCost()))
                	        .setFont(bold)
                	        .setFontSize(10)
                	        .setFontColor(ColorConstants.WHITE)
                	        .setMargin(0)));

                	energyCell.add(energyTable);
                	header.addCell(energyCell);

                
                	contentLayer.add(header);

                /* ───── Sous-header ───── */
               
                	//contentLayer.add(new Paragraph(card.getFaction() + " • " + card.getCardType())
                			contentLayer.add(new Paragraph(" • ")
                			.setFontSize(2)
                        .setFontColor(new DeviceRgb(90, 90, 90))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(0));
				
                /* ───── Illustration ───── */
               
                Div illustrationBox = new Div()
                	    .setWidth(UnitValue.createPercentValue(100))
                	    .setHeight(ILLUSTRATION_HEIGHT)
                	    .setBorder(new SolidBorder(0.5f))
                	    .setMarginBottom(1);
                
                if (index < imagePaths.size() && imagePaths.get(index) != null) {
                    Path imagePath = imagePaths.get(index);

                    ImageData imageData = loadOptimizedImage(
                            imagePath.toAbsolutePath().toString(),
                            900,        // largeur utile réelle
                            500,        // hauteur utile
                            0.8f        // qualité JPEG
                    );

                    Image image = new Image(imageData);

                 // 👉 remplit EXACTEMENT la box
                 image.setWidth(UnitValue.createPercentValue(100));
                 image.setHeight(UnitValue.createPercentValue(100));

                 // 👉 crop intelligent
                 image.setProperty(Property.OBJECT_FIT, ObjectFit.COVER);

                 // 👉 pas de marges implicites

                 image.setPadding(0);
                 image.setBorder(Border.NO_BORDER);

                 illustrationBox.add(image);
                } else {
                    // Fallback si image manquante
                    illustrationBox.setBackgroundColor(colorForFaction(card.getFaction()))
                                   .setOpacity(0.15f);
                }

                contentLayer.add(illustrationBox);
                /* ───── Texte (zone FIXE + police adaptative) ───── */
               
                Div textBox = new Div()
                	    //.setMaxHeight(72)   // 🔒 LIMITE, pas obligation
                		.setHeight(90)
                		.setBackgroundColor(backgroundColorForFaction(card.getFaction()))
                	    .setMarginBottom(6);

                int totalTextLength = 0;

                if (!card.getKeywords().isEmpty()) {
                    String kw = card.getKeywords().stream()
                            .map(Keyword::getDisplayName)
                            .reduce((a, b) -> a + " · " + b)
                            .orElse("");

                    totalTextLength += kw.length();

                    textBox.add(new Paragraph(kw)
                            .setFont(bold)
                            .setFontSize(8)
                            .setMarginBottom(2));
                }

                for (CardEffect effect : card.getEffects()) {
                    totalTextLength += effect.toDisplayString().length();
                }

                float effectFontSize = adaptiveFontSize(totalTextLength);

                for (CardEffect effect : card.getEffects()) {

                	Paragraph p = new Paragraph()
                	        .setFontSize(effectFontSize)
                	        .setMarginBottom(2);
                	Text txt_trigger = new Text(effect.getTrigger().getShortDisplay(effect.getConditionKeyword())).setUnderline();
                	p.add(txt_trigger);
                	String txt_effect=effect.getEffectText();
                	for (String mot : txt_effect.split(" ")) {
                	    switch (mot) {
                	        case "énergie":
                	            p.add(createIcon("resources/images/icons/energy.png"));
                	            break;
                	        case "dégâts":
                	            p.add(createIcon("resources/images/icons/hit.png"));
                	            break;
                	        case "PC":
                	            p.add(createIcon("resources/images/icons/PC.png"));
                	            break;    
                	       /*
                	        case "Piochez":
                	            p.add(createIcon("resources/images/icons/pioche.png"));
                	            break;    
                	        case "Défaussez":
                	            p.add(createIcon("resources/images/icons/defausse.png"));
                	            break;
                	           */    
                	        default:
                	            p.add(new Text(mot));
                	    }
                	    p.add(" "); // espace entre mots
                	}
                	//p.add(effect.getEffectText());
                	textBox.add(p);

                }

                
                Div footerZone = new Div()
                	    .setHeight(STATS_HEIGHT)
                	    .setMarginTop(6)
                	    .setKeepTogether(true); // 🔒 IMPORTANT

                	contentLayer.add(footerZone);    
                	contentLayer.add(textBox);
                /* ───── Type spécifique ───── */
                	if (card.getCardType() == CardType.UNIT) {

                	    Table stats = new Table(UnitValue.createPercentArray(new float[]{1, 1,1}))
                	        .setWidth(UnitValue.createPercentValue(100))
                	        .setHeight(STATS_HEIGHT)
                	        .setBackgroundColor(new DeviceRgb(245, 245, 245));

                	    stats.addCell(statCellIcon(ICON_ATTACK, card.getAttack(), bold)
                	        .setBorderRight(new SolidBorder(0.5f)));

                	    stats.addCell(statCellIcon(ICON_DEFENSE, card.getDefense(), bold)
                	        .setBorderLeft(new SolidBorder(0.5f)));
                	    
                	    Paragraph p = new Paragraph()
                	            .setTextAlignment(TextAlignment.RIGHT)
                	            .setMargin(0)
                	            .setFont(bold)
                	            .setFontSize(6);
                	   
                	    p.add(new Text(card.getFaction()+"\n UNIT"));
                	   // p.add(createIcon("resources/images/icons/faction/ORGANIC.png"));
                	    Cell c =new Cell()
                        	    .setBorder(new SolidBorder(0.5f))
                        	    .setBackgroundColor(new DeviceRgb(245, 245, 245))
                        	    .setPadding(4)
                        	    .add(p);
                	    stats.addCell(c);
                    	    

                	    footerZone.add(stats);
                	} else if (card.getCardType() == CardType.STRUCTURE) {
                		Table stats = new Table(UnitValue.createPercentArray(new float[]{1,1}))
                    	        .setWidth(UnitValue.createPercentValue(100))
                    	        .setHeight(STATS_HEIGHT)
                    	        .setBackgroundColor(new DeviceRgb(245, 245, 245));
                		stats.addCell(statCellIcon(ICON_DEFENSE, card.getDefense(), bold)
                    	        .setBorderLeft(new SolidBorder(0.5f)));
                		
                		Paragraph p = new Paragraph()
                	            .setTextAlignment(TextAlignment.RIGHT)
                	            .setMargin(0)
                	            .setFont(bold)
                	            .setFontSize(6);
                	    
                	    p.add(""+card.getFaction()+"\n STRUCTURE");
                	    Cell c =new Cell()
                        	    .setBorder(new SolidBorder(0.5f))
                        	    .setBackgroundColor(new DeviceRgb(245, 245, 245))
                        	    .setPadding(4)
                        	    .add(p);
                	    stats.addCell(c);
                    	    

                	    footerZone.add(stats);
                	}else if (card.getCardType() == CardType.ACTION) {
                		Table stats = new Table(UnitValue.createPercentArray(new float[]{1}))
                    	        .setWidth(UnitValue.createPercentValue(100))
                    	        .setHeight(STATS_HEIGHT)
                    	        .setBackgroundColor(new DeviceRgb(245, 245, 245));
                		Paragraph p = new Paragraph()
                	            .setTextAlignment(TextAlignment.CENTER)
                	            .setMargin(0)
                	            .setFont(bold)
                	            .setFontSize(6);
                	    
                	    p.add(""+card.getFaction()+"\n ACTION");
                	    Cell c =new Cell()
                        	    .setBorder(new SolidBorder(0.5f))
                        	    .setBackgroundColor(new DeviceRgb(245, 245, 245))
                        	    .setPadding(4)
                        	    .add(p);
                	    stats.addCell(c);
                    	    

                	    footerZone.add(stats);
                	}

                

                cell.add(cardDiv);
                table.addCell(cell);

                index++;

                if (index % 9 == 0) {
                    // Recto
                    document.add(table);

                    // Verso
                    document.add(new AreaBreak());
                    addBackPage(document);
                    
                    if (index < cards.cards.size()) {
                    // Nouvelle page recto
                    document.add(new AreaBreak());
                    table = new Table(UnitValue.createPercentArray(3))
                            .setWidth(UnitValue.createPercentValue(100));
                    }
                }

            }

            if (index % 9 != 0) {
                document.add(table);
            }
            
            if (index < cards.cards.size()) {
            	// Verso
            	document.add(new AreaBreak());
            	addBackPage(document);
            }
            document.close();

        } catch (IOException e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }
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

    private static float bgOpacityForFaction(Faction faction) {
        return switch (faction) {
            case ORGANIC -> 0.07f;
            case ASTRAL -> 0.06f;
            case OCCULT -> 0.06f;
            case NOMAD -> 0.05f;
            case MECHANICAL -> 0.04f;
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

    private static Image triggerIconOrNull(TriggerType trigger, float size) {
        String resourcePath = "/images/icons/trigger/" + trigger.name() + ".png";

        try {
            var stream = BoosterPdfExporterStyled.class.getResourceAsStream(resourcePath);
            if (stream == null) {
            	System.out.println(resourcePath);
                return null; // icône absente → fallback texte
            }

            ImageData data = ImageDataFactory.create(stream.readAllBytes());
            Image img = new Image(data);
            img.setWidth(size);
            img.setHeight(size);
            img.setAutoScale(false);
            img.setBorder(Border.NO_BORDER);
            img.setMarginRight(4);

            return img;

        } catch (IOException e) {
            return null; // sécurité absolue
        }
    }

    private static Table triggerIconChipOrNull(
            TriggerType trigger,
            float fontSize,
            Color backgroundColor
    ) {
        String resourcePath = "/images/icons/trigger/" + trigger.name() + ".png";

        try (var stream = BoosterPdfExporterStyled.class.getResourceAsStream(resourcePath)) {

            if (stream == null) {
                return null;
            }

            float iconSize = fontSize + 1;

            ImageData data = ImageDataFactory.create(stream.readAllBytes());
            Image icon = new Image(data)
                    .setWidth(iconSize)
                    .setHeight(iconSize)
                    .setBorder(Border.NO_BORDER);

            Cell cell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(backgroundColor)
                    .setPaddingLeft(1.5f)
                    .setPaddingRight(1.5f)
                    .setPaddingTop(0.8f)
                    .setPaddingBottom(1.2f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .add(icon);

            Table chip = new Table(1)
                    .setWidth(iconSize + 3)
                    .setBorder(Border.NO_BORDER)
                    .setMarginRight(4);

            chip.addCell(cell);

            return chip;

        } catch (IOException e) {
            return null;
        }
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
