package unics.Enum;


	public enum CardType {
		
	    UNIT(1),        // unité classique
	    STRUCTURE(2),   // impact plus durable ou passif
	    ACTION(3),      // effet instantané fort
	    TOKEN(0);       // token ou carte promo, pas de coût relatif

	    private final int typeValue;

	    CardType(int value) {
	        this.typeValue = value;
	    }

	    public int getTypeValue() {
	        return typeValue;
	    }
	    
	    
}
