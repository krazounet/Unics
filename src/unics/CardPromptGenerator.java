package unics;



import unics.Enum.AbilityType;
import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;

public class CardPromptGenerator {

    private final Card card;

    public CardPromptGenerator(Card card) {
        this.card = card;
    }

    // ─────────────────────────────────────────────
    // 🎨 API PUBLIQUE
    // ─────────────────────────────────────────────

    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();

        prompt.append("single subject, one main focus. fantasy illustration ,artwork illustration only, full bleed artwork, ");

        appendCardType(prompt);
        appendFactionStyle(prompt);
        appendRoleAndStats(prompt);
        appendKeywords(prompt);
        appendEffects(prompt);
        appendGlobalStyle(prompt);
        prompt.append(
        	    "epic lighting, dramatic composition, " +
        	    "high detail, digital painting, " +
        	    "fantasy illustration, " +
        	    "artwork illustration only, full bleed artwork, " +
        	    "single subject, one main focus, " +
        	    "illustration, not a photograph, not a mockup, not a layout, " +
        	    "clean background, centered subject, " +
        	    "no frame, no borders, no UI elements, " +
        	    "no text, no watermark, no logo"
        	);

        return prompt.toString().trim();
    }

    // ─────────────────────────────────────────────
    // 🧩 SECTIONS DU PROMPT
    // ─────────────────────────────────────────────

    private void appendCardType(StringBuilder sb) {
        switch (card.getCardType()) {
            case UNIT -> sb.append("full body character concept art, ");
            case STRUCTURE -> sb.append("massive structure or fortress, ");
            case ACTION -> sb.append("dynamic magical or technological effect, no character visible, ");
		default -> throw new IllegalArgumentException("Unexpected value: " + card.getCardType());
        }
    }

    private void appendFactionStyle(StringBuilder sb) {
        sb.append(getFactionVisualStyle(card.getFaction())).append(", ");
    }

    private void appendRoleAndStats(StringBuilder sb) {
        if (card.getCardType() != CardType.UNIT) return;

        Integer atk = card.getAttack();
        Integer def = card.getDefense();

        if (atk != null && def != null) {
            if (atk >= def + 3) {
                sb.append("aggressive stance, attacking pose, ");
            } else if (def >= atk + 3) {
                sb.append("defensive posture, shielded, ");
            } else {
                sb.append("balanced warrior stance, ");
            }
        }
    }

    private void appendKeywords(StringBuilder sb) {
        if (card.getKeywords() == null || card.getKeywords().isEmpty()) return;

        for (Keyword keyword : card.getKeywords()) {
            sb.append(keywordToVisual(keyword)).append(", ");
        }
    }

    private void appendEffects(StringBuilder sb) {
        if (card.getEffects() == null || card.getEffects().isEmpty()) return;

        for (CardEffect effect : card.getEffects()) {
            sb.append(effectToVisual(effect.getAbility())).append(", ");
        }
    }

    private void appendGlobalStyle(StringBuilder sb) {
    	sb.append(
    		    "epic lighting, dramatic composition, " +
    		    "high detail, digital painting, " +
    		    "fantasy illustration, " +
    		    "artwork illustration only, full bleed artwork, " +
    		    "clean background, centered subject, " +
    		    "no frame, no borders, no UI elements, " +
    		    "no text, no watermark, no logo"
    		);

    }

    // ─────────────────────────────────────────────
    // 🎭 VISUELS PAR FACTION
    // ─────────────────────────────────────────────

    private String getFactionVisualStyle(Faction faction) {
        return switch (faction) {
            case ASTRAL ->
                "cosmic energy, glowing stars, ethereal light, celestial motifs";
            case MECHANICAL ->
                "steampunk machinery, metal plates, gears, industrial design";
            case ORGANIC ->
                "living plants, roots, nature magic, organic textures";
            case NOMAD ->
                "desert scavenger aesthetic, improvised gear, nomadic technology";
            case OCCULT ->
                "dark magic, arcane symbols, shadows, forbidden rituals";
        };
    }

    // ─────────────────────────────────────────────
    // 🔑 KEYWORDS → VISUEL
    // ─────────────────────────────────────────────
    public static String keywordToVisual(Keyword keyword) {
        return switch (keyword) {

            // ───────────── Combat / impact ─────────────

            case TRAMPLE ->
                "overflowing attack energy spreading beyond the target, massive impact passing through the enemy";

            case FRAPPE_IMMEDIATE ->
                "sudden aggressive assault stance, motion blur, instant strike at the moment of appearance";

            case FURTIF ->
                "attack emerging from shadows, target caught off guard, defensive lines bypassed";
            case FIRST_STRIKE ->
            	"attack from nowhere, very fast attack that surprised ennemy";

            // ───────────── Défense / survie ─────────────

            case BOUCLIER ->
                "glowing energy shield protecting a nearby allied unit";

            case DEFENSIF ->
                "defensive stance, shield raised, guarding a narrow passage, protective posture";

            case INSAISISSABLE ->
                "partially intangible form, difficult to target, blurred or phased contours";

            // ───────────── Temporalité / état ─────────────

            case INSTABLE ->
                "unstable energy, glowing cracks, form on the verge of disintegration";

            case PERSISTANT ->
                "unyielding presence, firmly anchored or mechanically locked in place";

            case UNIQUE ->
                "iconic and singular appearance, heroic design or legendary artifact";

            // ───────────── Mobilité ─────────────

            case MOBILE ->
                "fluid movement between positions, motion trails suggesting tactical repositioning";

            // ───────────── Immunités (factions) ─────────────

            case IMMUNITE_CONTRE_ASTRAL ->
                "protective barrier absorbing cosmic or stellar energy";

            case IMMUNITE_CONTRE_ORGANIC ->
                "reinforced shell resisting spores, roots, and biological matter";

            case IMMUNITE_CONTRE_NOMAD ->
                "locked systems preventing agile maneuvers and mobile tactics";

            case IMMUNITE_CONTRE_MECHANICAL ->
                "insulation fields disrupting gears and mechanical energy";

            case IMMUNITE_CONTRE_OCCULT ->
                "runic seals or sacred light blocking occult magic";
		default -> throw new IllegalArgumentException("Unexpected value: " + keyword);
        };
    }



    // ─────────────────────────────────────────────
    // ⚡ EFFECTS → AMBIANCE
    // ─────────────────────────────────────────────

    public static String effectToVisual(AbilityType ability) {
        return switch (ability) {

            // ──────────────── Damage ────────────────

            case DAMAGE_UNIT_ENEMY ->
                "violent attack directed at an enemy unit, visible physical or energy impact";

            case DAMAGE_UNIT_ALLY ->
                "unstable energy backlash or sacrifice harming an allied unit";

            case DAMAGE_PC_ENEMY ->
                "direct assault targeting the enemy stronghold, explosion or long-range strike";

            case DAMAGE_PC_SELF ->
                "energy backlash or overload damaging himself";

            // ──────────────── Healing ────────────────

            case HEAL_PC ->
                "flow of restorative energy reinforcing the player's forces";

            // ──────────────── Stats ────────────────

            case BUFF ->
                "visible empowerment of a unit, enhanced aura, reinforced equipment or amplified energy";

            case DEBUFF_ENEMY ->
                "visible weakening of an enemy unit, drained energy or broken stance";

            case DEBUFF_ALLY ->
                "temporary negative effect on an allied unit, overload or imposed constraint";

            // ──────────────── Cards ────────────────

            case DRAW ->
                "emerging symbols, glowing data streams, or new resources materializing";

            case DESTROY_UNIT_ENEMY ->
                "brutal destruction of an enemy unit, explosion or targeted disintegration";

            case DESTROY_STRUCTURE_ENEMY ->
                "collapse or sabotage of an enemy structure";

            // ──────────────── Movement ────────────────

            case MOVE_ALLY ->
                "tactical repositioning of an allied unit across the battlefield";

            case MOVE_ENEMY ->
                "forced displacement or expulsion of an enemy unit";

            // ──────────────── Energy ────────────────

            case ENERGY_GAIN_SELF ->
                "accumulation of energy, glowing reserves or system recharge";

            case ENERGY_GAIN_ENEMY ->
                "energy flowing outward toward the enemy, unintended energy transfer";

            case ENERGY_LOSS_SELF ->
                "energy drain or voluntary exhaustion weakening the player";

            case ENERGY_LOSS_ENEMY ->
                "energy siphon draining the enemy's reserves";

            case DISCARD_SELF ->
                "";
            case DISCARD_ENEMY ->
            "";
		default -> throw new IllegalArgumentException("Unexpected value: " + ability);
        };
    }


}
