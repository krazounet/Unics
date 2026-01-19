package unics;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;

public final class CardNameGenerator {

    // Préfixes par faction
    private static final Map<Faction, String[]> PREFIXES = Map.of(
    		Faction.ASTRAL, new String[]{
    	            "Star","Cosmo","Luna","Nova","Celest","Galax","Nebula","Orion","Solar","Eclipse",
    	            "Meteor","Comet","Astro","Photon","Quasar","Orbit","Stellar","Void","Aether","Corona"
    	        },
    	        Faction.ORGANIC, new String[]{
    	            "Flora","Thorn","Root","Leaf","Bloom","Vine","Moss","Sap","Seed","Petal",
    	            "Bark","Branch","Mush","Fern","Sprout","Pollen","Wild","Nature","Twig","Ivy"
    	        },
    	        Faction.NOMAD, new String[]{
    	            "Rover","Wander","Drift","Nomad","Path","Scout","Trail","Gyro","Harbor","Quest",
    	            "Vagab","Voyage","Horizon","Drifter","Wayfar","Journey","Ramble","Seek","Exile","Stride"
    	        },
    	        Faction.MECHANICAL, new String[]{
    	            "Robo","Iron","Steel","Titan","Mecha","Gear","Bolt","Circuit","Mech","Drone",
    	            "Volt","Hydro","Auto","Nano","Core","Mach","Cyber","Axle","Servo","Quantum"
    	        },
    	        Faction.OCCULT, new String[]{
    	            "Myst","Hex","Shadow","Nox","Grim","Ritual","Crypt","Obscur","Phantom","Curse",
    	            "Spectre","Wraith","Eclipse","Bane","Occult","Void","Eerie","Coven","Spell","Ghoul"
    	        }
    );

    // Suffixes par type de carte
    private static final Map<CardType, String[]> SUFFIXES = Map.of(
    		CardType.UNIT, new String[]{"-bot","-guard","-tron","-knight","-drone","-soldier","-warrior","-sentinel","-beast","-rider",
                    "-scout","-mech","-golem","-assault","-defender"},
			CardType.STRUCTURE, new String[]{"-tower","-fort","-wall","-spire","-dome","-outpost","-bastion","-keep","-citadel","-gate",
			                        "-stronghold","-platform","-station","-bunker","-arch"},
			CardType.ACTION, new String[]{"-burst","-veil","-flare","-wave","-hex","-strike","-blast","-shock","-shockwave","-eruption",
			                     "-storm","-pulse","-beam","-flash","-flarex"}
	//		CardType.TOKEN, new String[]{"-token","-promo","-sigil","-mark","-icon","-emblem","-glyph","-tag","-stamp","-badge",
	//		                    "-symbol","-chip","-print","-mini","-tokenx"}
    );

    private CardNameGenerator() {
        // Classe utilitaire
    }

    /**
     * Génère un nom de carte thématique, lisible et unique.
     *
     * @param faction la faction de la carte
     * @param type le type de carte
     * @param publicId identifiant visible pour garantir unicité
     * @param keywords optionnel : mots-clés pour enrichir le nom
     * @return nom généré
     */
    public static String generateName(Faction faction, CardType type, String publicId, Set<Keyword> keywords) {
    	ThreadLocalRandom random = ThreadLocalRandom.current();

        // Tirer 1 ou 2 préfixes
        String[] factionPrefixes = PREFIXES.getOrDefault(faction, new String[]{"Neo"});
        String prefix1 = factionPrefixes[random.nextInt(factionPrefixes.length)];
        String prefixPart = prefix1;

        if (random.nextBoolean()) { // 50% de chance d'ajouter un 2e préfixe
            String prefix2 = factionPrefixes[random.nextInt(factionPrefixes.length)];
            prefixPart += prefix2;
        }

        // Suffixe
        String[] typeSuffixes = SUFFIXES.getOrDefault(type, new String[]{"-unit"});
        String suffix = typeSuffixes[random.nextInt(typeSuffixes.length)];

        // Mot-clé optionnel
        String keywordPart = "";
        if (keywords != null && !keywords.isEmpty()) {
            Keyword k = keywords.iterator().next();
            keywordPart = "-" + k.name().substring(0, Math.min(k.name().length(), 3)).toLowerCase();
        }

        // Combinaison finale
        return prefixPart + suffix + keywordPart + "-" + publicId;
    }
}
