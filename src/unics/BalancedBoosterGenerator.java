package unics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import aiGenerated.ComfyUIBatchGenerator;

public class BalancedBoosterGenerator {

	static ThreadLocalRandom random = ThreadLocalRandom.current();
	
	static boolean MAKE_IA_IMAGE=false;
			
	static int nb_booster=5;
	
	
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
 
    	Booster booster=new Booster18(random);
    	List<String> prompts=getPromptduBooster(booster);
    	List<Path> lp;
    	
    	if (MAKE_IA_IMAGE)  lp =ComfyUIBatchGenerator.generateImages(prompts);
    	else 				lp=getDefaultListPath();
    	
        // Export PDF
        BoosterPdfExporterStyled.exportBoosterToPdf(booster, pdfFileName+booster.getPublicId()+"-"+booster.cards.size()+"-"+booster.getManaCurveProfile()+".pdf",lp);
    }

    public static List<Path> getDefaultListPath(){
    	List<Path> lp=new ArrayList<Path>();
    	for (int i=0;i<12;i++) {lp.add(Path.of("C:\\ai\\ComfyUI_windows_portable\\ComfyUI\\output\\default"+i+".png"));}
    	return lp;
    }
    
    public static List<String> getPromptduBooster(Booster booster){
    	
    	List<String> prompts=new ArrayList<String>();
    	for (Card c : booster.getCards()) {
    		prompts.add(new CardPromptGenerator(c).generatePrompt());
    	}
		return prompts;
    	
    }

    

    
}
