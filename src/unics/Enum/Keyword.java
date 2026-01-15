package unics.Enum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import unics.FactionProfile;

/**
 * Enum représentant les mots-clés des cartes.
 * Chaque mot-clé a une description et un poids pour le générateur.
 */
public enum Keyword {
	
    //EPHEMERE("La carte dure exactement 2 tours puis est retirée", 10),//inutile c'est la règle de base
	TRAMPLE("Les dégats en excès sur une unité sont reportés sur le PC adverse",0),
    INSTABLE("La carte ne dure qu’un seul tour", 0),
    UNIQUE("Deux cartes Unique ne peuvent pas être présentes en même temps sur le plateau du même joueur", -5),
    INSAISISSABLE("Ne peut être attaquée que par une unité ayant elle-même INSAISISSABLE", 0),
    FURTIF("Ignore les DEFENSIFS", 0),
    PERSISTANT("Ne s'incline pas", 0),
    DEFENSIF("Doit être ciblée en priorité par l’adversaire et ne peut pas attaquer directement les PC ennemis", 0),
    FRAPPE_IMMEDIATE("Peut attaquer le tour où elle arrive", 0),
    BOUCLIER("Peut absorber les dégâts d’une autre carte alliée une fois par tour", 0),
    MOBILE("Permet de permuter sa position avec une autre unité alliée", 5),
    FIRST_STRIKE("frappe en premier",0),//kw frappe en premier
    IMMUNITE_CONTRE_ASTRAL("",5),
	IMMUNITE_CONTRE_ORGANIC("",5),
	IMMUNITE_CONTRE_NOMAD("",5),
	IMMUNITE_CONTRE_MECHANICAL("",5),
	IMMUNITE_CONTRE_OCCULT("",5),
	SANGUINAIRE("Ne peut pas attaquer le PC adverse",-0); //A rajouter : ne peut attaquer que les structure et unité
//imunite contre X
	
    private final String description;
    private final int generatorWeight;

        
    // Constructeur
    Keyword(String description, int generatorWeight) {
        this.description = description;
        this.generatorWeight = generatorWeight;
    }

    // Getter pour la description
    public String getDescription() {
        return description;
    }

    // Getter pour le poids pour le générateur
    public int getWeight() {
        return generatorWeight;
    }
    public static Keyword randomPlayable(ThreadLocalRandom random, CardType cardType, FactionProfile profil) {
    	
    	// amélioration possible en mettant un poids
        List<Keyword> playable = Arrays.stream(values())
        		.filter(t -> t.isAllowedCardTypes(cardType))
        		.filter(k -> !profil.getForbiddenKeywords().contains(k))
                .toList();
        
        List<Keyword> weighted = new ArrayList<>();

        for (Keyword k : playable) {
            weighted.add(k); // chance normale
         // amélioration possible en mettant un poids à chaque faction x2 /x3 etc...
            if (profil.getFavoredKeywords().contains(k)) {
                weighted.add(k); // +1
                weighted.add(k); // +2 (x3 au total)
            }
        }
        
        
        if (weighted.isEmpty()) {
            throw new IllegalStateException("Aucun Keyword jouable comme condition "+cardType);
        }

        return weighted.get(random.nextInt(weighted.size()));
    }
    
    public static Keyword randomPlayable(ThreadLocalRandom random) {
        List<Keyword> playable = Arrays.stream(values())
        		
                .toList();

        if (playable.isEmpty()) {
            throw new IllegalStateException("Aucun Keyword jouable comme condition");
        }

        return playable.get(random.nextInt(playable.size()));
    }
    public boolean isAllowedCardTypes(CardType cardType) {
        switch (cardType) {
          
            case UNIT : return true;
            case STRUCTURE : 
            	if (this == MOBILE) return false;
            	if (this == FURTIF) return false;
            	if (this == FRAPPE_IMMEDIATE) return false;
            	if (this == TRAMPLE) return false;
            	if (this == FIRST_STRIKE) return false;
            	if (this == INSAISISSABLE) return false;
            	if (this == SANGUINAIRE) return false;
            	
            	return true;
            case ACTION : return false;
            case TOKEN : return false;
            default :
            	return true;
        }
    }
    
    @Override
    public String toString() {
        return this.name() + " : " + description + " (Poids: " + generatorWeight + ")";
    }
    
    public int computeWeight(int atk, int def, int energy) {
        switch (this) {
            case TRAMPLE:
                return atk * 4;

            case INSTABLE:
                return energy * -8;

            case INSAISISSABLE:
                return atk * 3;
                
            case FURTIF:
                return atk * 4;

            case PERSISTANT:
                return energy * 12;

            case DEFENSIF:
                return def * 8;

            case FRAPPE_IMMEDIATE:
                return atk * 4;
            case BOUCLIER:
                return def * 5;

            case FIRST_STRIKE:
                return atk * 3;
            case SANGUINAIRE:
                return -(atk * 3);    
           
            default:
                return this.generatorWeight;
        }
    }
    public String getDisplayName() {
    	switch (this) {
        case IMMUNITE_CONTRE_ASTRAL: return "IMMUNITE CONTRE ASTRAL";
        case IMMUNITE_CONTRE_ORGANIC: return "IMMUNITE CONTRE ORGANIC";
        case IMMUNITE_CONTRE_MECHANICAL: return "IMMUNITE CONTRE MECHANICAL";
        case IMMUNITE_CONTRE_NOMAD: return "IMMUNITE CONTRE NOMAD";
        case IMMUNITE_CONTRE_OCCULT: return "IMMUNITE CONTRE OCCULT";
        
        default: return name();
    	}
    }
}