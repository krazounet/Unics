package unics.ExecUtil;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import unics.Enum.Keyword;

/**
 * Génère un fichier d'aide imprimable (.txt) pour les mots-clés du jeu.
 */
public class KeywordHelpTxtGenerator {

    public static void main(String[] args) {

        Path output = Path.of("keyword_guide.txt");
        StringBuilder sb = new StringBuilder();

        // =========================
        // En-tête
        // =========================
        sb.append("========================================\n");
        sb.append("        GUIDE DES MOTS-CLES\n");
        sb.append("========================================\n\n");

        sb.append("Document d'aide pour les nouveaux joueurs\n");
        sb.append("Date de generation : ").append(LocalDate.now()).append("\n\n");

        // =========================
        // Introduction
        // =========================
        sb.append("----------------------------------------\n");
        sb.append("INTRODUCTION\n");
        sb.append("----------------------------------------\n\n");

        sb.append("Ce document explique les mots-cles utilises sur les cartes du jeu.\n");
        sb.append("Chaque mot-cle represente une regle speciale qui modifie\n");
        sb.append("le comportement d'une carte.\n\n");
        sb.append("Lorsqu'un mot-cle apparait sur une carte,\n");
        sb.append("son effet est actif en permanence sauf indication contraire.\n\n");

        // =========================
        // Liste des mots-clés
        // =========================
        sb.append("----------------------------------------\n");
        sb.append("LISTE DES MOTS-CLES\n");
        sb.append("----------------------------------------\n\n");

        for (Keyword keyword : Keyword.values()) {
            sb.append(keyword.getDisplayName().toUpperCase()).append("\n");
            sb.append(repeat("-", keyword.getDisplayName().length())).append("\n");
            sb.append(keyword.getDescription()).append("\n\n");
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

    private static String repeat(String s, int count) {
        return s.repeat(Math.max(0, count));
    }
}
