package unics.godot.Exec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import unics.Enum.Keyword;

public class KeywordforGodot {

	public static void main(String[] args) {

        Path output = Path.of("keyword_godot.txt");
        StringBuilder sb = new StringBuilder();

        
        sb.append("extends Resource\n"
        		+ "class_name Keyword\n"
        		+ "\n"
        		+ "@export var keyword: KeywordType\n"
        		+ "\n"
        		+ "enum KeywordType  {");

        for (Keyword keyword : Keyword.values()) {
            sb.append(keyword.name().toUpperCase()).append(",\n");
            
        }
        sb.append("}\n"
        		+ "func get_label() -> String:\n"
        		+ "\treturn KeywordType.keys()[keyword]");
        // =========================
        // Écriture fichier
        // =========================
        try {
            Files.writeString(output, sb.toString());
            System.out.println("Fichier d'aide genere avec succes :");
            System.out.println(output.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de la generation du fichier : " + e.getMessage());
        }
    }

}
