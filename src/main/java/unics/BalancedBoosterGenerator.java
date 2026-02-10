package unics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aiGenerated.CardRenderPipeline;
import aiGenerated.ComfyUIClient;
import aiGenerated.ComfyUIWorker;
import dbPG18.CardRenderDaoInterface;
import dbPG18.CardSnapshotDaoInterface;
import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;
import dbPG18.JdbcCardRenderDao;
import dbPG18.JdbcCardSnapshotDao;

public class BalancedBoosterGenerator {

	static ThreadLocalRandom random = ThreadLocalRandom.current();
	
	static boolean MAKE_IA_IMAGE=true;
	static boolean isDB=true;
	static int nb_booster=1;
	static int taille_booster =18;
	
	private static CardRenderPipeline pipeline;

	
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
    	
    	Booster booster=null;
    	if (isDB) {
    		booster=new Booster18DB(random,dao);
        	
    	}else {
    		booster = new Booster18(random);
    	}
    	booster.generate();
    	dao.close();
    	CardRenderPipeline pipeline = getPipeline();

    	List<Path> lp = new ArrayList<>();

    	for (Card card : booster.getCards()) {
    		System.out.println("publicid : "+card.getPublicId());
    	    Path imagePath = pipeline.resolveImage(card);
    	    lp.add(imagePath);
    	}
    	
        // Export PDF
        BoosterPdfExporterStyled.exportBoosterToPdf(booster, pdfFileName+booster.getPublicId()+"-"+booster.cards.size()+"-"+booster.getManaCurveProfile()+".pdf",lp);
    }
 
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
    

    private static CardRenderPipeline getPipeline() {

        if (pipeline != null) {
            return pipeline;
        }

        CardSnapshotDaoInterface snapshotDao = null;
        CardRenderDaoInterface renderDao = null;
		try {
			Connection conn = DbUtil.getConnection();
			snapshotDao = new JdbcCardSnapshotDao(conn);
			renderDao =new JdbcCardRenderDao(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       

        ComfyUIClient client =
            new ComfyUIClient("http://localhost:8188");

        ComfyUIWorker worker =
            new ComfyUIWorker(client, Path.of("images"));

        pipeline = new CardRenderPipeline(
            snapshotDao,
            renderDao,
            worker
        );

        return pipeline;
    }


    
}
