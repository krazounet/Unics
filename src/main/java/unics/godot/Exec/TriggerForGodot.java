package unics.godot.Exec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import unics.Enum.TriggerType;

public class TriggerForGodot {
	public static void main(String[] args) {
		Path output = Path.of("trigger_godot.txt");
        StringBuilder sb = new StringBuilder();
        for (TriggerType keyword : TriggerType.values()) {
            sb.append(keyword.name().toUpperCase()).append(",\n");
            
        }
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
