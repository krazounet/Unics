package aiGenerated;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.snapshot.CardSnapshot;

public class VisualIdentity {
	
	 	private final CardType type;
	    private final Faction faction;
	    private final List<String> keywords;  	//volontairement decouplé des Enums
	    private final List<String> effects;
	    
		public VisualIdentity(CardType type, Faction faction, List<String> keywords, List<String> effects) {
			super();
			this.type = type;
			this.faction = faction;
			this.keywords = keywords;
			this.effects = effects;
		}
	    
		public VisualIdentity(CardSnapshot snap) {
			this(
			        snap.type,
			        snap.faction,
			        snap.keywords.stream()
			            .map(Enum::name)
			            .toList(),
			        snap.effects.stream()
			            .map(effect -> effect.ability.name())
			            .toList()
			    );
		}
		
		
		public String computeSignatureHash() {
	        return DigestUtils.sha256Hex(buildSignature());
	    }
		private String buildSignature() {

	        String keywordsPart = keywords.stream()
	            .map(k -> k.trim().toLowerCase())
	            .sorted()
	            .collect(Collectors.joining(","));

	        String effectsPart = effects.stream()
	        	    .map(k -> k.trim().toLowerCase())
	        	    .sorted()
	        	    .collect(Collectors.joining("|"));


	        return String.join(";",
	            
	            "TYPE=" + type.name(),
	            "FACTION=" + faction.name(),
	            "KEYWORDS=" + keywordsPart,
	            "EFFECTS=" + effectsPart
	        );
	    }
		@Override
	    public String toString() {
	        return computeSignatureHash(); // ou le hash final
	    }
}
