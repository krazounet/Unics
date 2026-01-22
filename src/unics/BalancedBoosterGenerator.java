package unics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aiGenerated.ComfyUIBatchGenerator;
import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;

public class BalancedBoosterGenerator {

	static ThreadLocalRandom random = ThreadLocalRandom.current();
	
	static boolean MAKE_IA_IMAGE=false;
	static int nb_booster=1;
	static int taille_booster =18;
	
    public static void main(String[] args) throws Exception {
    	
        try {
        	for(int i=0;i<nb_booster;i++) {
            generateAndExportBooster("Booster-");
            System.out.println("Booster PDF généré : BalancedBooster-"+i);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static void generateAndExportBooster(String pdfFileName) throws Exception {
 
    	//Booster booster=new Booster18(random);
    	JdbcCardDao dao=new JdbcCardDao(DbUtil.getConnection());
    	Booster18DB booster=new Booster18DB(random,dao);
    	booster.generate();
    	dao.close();
    	List<String> prompts=getPromptduBooster(booster);
    	List<Path> lp;
    	
    	if (MAKE_IA_IMAGE)  lp =ComfyUIBatchGenerator.generateImages(prompts);
    	else 				lp=getDefaultListPath();
    	
        // Export PDF
        BoosterPdfExporterStyled.exportBoosterToPdf(booster, pdfFileName+booster.getPublicId()+"-"+booster.cards.size()+"-"+booster.getManaCurveProfile()+".pdf",lp);
    }
    /*
    public static List<Path> getDefaultListPath(){
    	List<Path> lp=new ArrayList<Path>();
    	for (int i=0;i<12;i++) {lp.add(Path.of("C:\\ai\\ComfyUI_windows_portable\\ComfyUI\\output\\default"+i+".png"));}
    	return lp;
    }*/
    public static List<Path> getDefaultListPath() {
        Path outputDir = Paths.get("C:\\ai\\ComfyUI_windows_portable\\ComfyUI\\output");

        try (Stream<Path> stream = Files.list(outputDir)) {

            List<Path> images = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
                    })
                    .collect(Collectors.toList());

            if (images.isEmpty()) {
                throw new IllegalStateException("Aucune image trouvée dans " + outputDir);
            }

            Collections.shuffle(images);
            return images.stream()
                    .limit(taille_booster)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du dossier ComfyUI output", e);
        }
    }
    public static List<String> getPromptduBooster(Booster booster){
    	
    	List<String> prompts=new ArrayList<String>();
    	for (Card c : booster.getCards()) {
    		prompts.add(new CardPromptGenerator(c).generatePrompt());
    	}
		return prompts;
    	
    }

    

    
}
