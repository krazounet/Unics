package unics.Enum;

public enum ConstraintType {

	FACTION,
	COUT,
	POSITION,
	HAND,
	PRESENSE;
	
	
	
	public boolean applyTarget() {
		return switch (this) {
		case FACTION,COUT
				-> true;
		
		default -> false;
		};
		
	}
}
