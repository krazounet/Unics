package aiGenerated;


import unics.Enum.AbilityType;
import unics.Enum.CardType;
import unics.Enum.Faction;
import unics.Enum.Keyword;
import unics.snapshot.CardSnapshot;
import unics.snapshot.EffectSnapshot;

public final class CardSnapshotPromptGenerator {

    private final CardSnapshot snapshot;
    private final RenderProfile profile;

    public CardSnapshotPromptGenerator(CardSnapshot snapshot, RenderProfile profile) {
        this.snapshot = snapshot;
        this.profile = profile;
    }

    // ─────────────────────────────────────────────
    // 🎨 API PUBLIQUE
    // ─────────────────────────────────────────────

    public String generatePrompt() {
        StringBuilder prompt = new StringBuilder();

        // Intention visuelle figée (snapshot)
        prompt.append(snapshot.illustrationPromptBase).append(", ");

        appendCardType(prompt);
        appendFactionStyle(prompt);
        appendRoleAndStats(prompt);
        appendKeywords(prompt);
        appendEffects(prompt);
        appendGlobalStyle(prompt);
        // Style & contraintes IA (RenderProfile)
        prompt.append(profile.stylePrompt).append(", ");
        prompt.append(profile.negativePrompt);

        return prompt.toString().trim();
    }

   

    private void appendFactionStyle(StringBuilder sb) {
        sb.append(getFactionVisualStyle(snapshot.faction)).append(", ");
    }

    private void appendRoleAndStats(StringBuilder sb) {
        if (snapshot.type != CardType.UNIT) return;

        int atk = snapshot.attack;
        int def = snapshot.health; // ou defense si tu renommes

        if (atk >= def + 3) {
            sb.append("aggressive stance, attacking pose, ");
        } else if (def >= atk + 3) {
            sb.append("defensive posture, shielded, ");
        } else {
            sb.append("balanced warrior stance, ");
        }
    }

    private void appendKeywords(StringBuilder sb) {
        if (snapshot.keywords == null || snapshot.keywords.isEmpty()) return;

        for (Keyword keyword : snapshot.keywords) {
            sb.append(keywordToVisual(keyword)).append(", ");
        }
    }

    private void appendEffects(StringBuilder sb) {
        if (snapshot.effects == null || snapshot.effects.isEmpty()) return;

        for (EffectSnapshot effect : snapshot.effects) {
            sb.append(effectToVisual(effect.ability)).append(", ");
        }
    }

      

    
    
 // ─────────────────────────────────────────────
    // 🧩 SECTIONS DU PROMPT
    // ─────────────────────────────────────────────

    private void appendCardType(StringBuilder sb) {
        switch (snapshot.type) {
            case UNIT -> sb.append("full body character concept art, ");
            case STRUCTURE -> sb.append("massive structure or fortress, ");
            case ACTION -> sb.append("dynamic magical or technological effect, no character visible, ");
		default -> throw new IllegalArgumentException("Unexpected value: " + snapshot.type);
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
                
            case SANGUINAIRE ->
            	"blood-infused energy, crimson glow around the character, empowered by recent violence, aggressive and relentless aura";
            case INCIBLABLE ->
    		"";
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
            "volatile energy being sacrificed or released, fragments dissolving into the void";

        case DISCARD_ENEMY ->
            "enemy resources collapsing or burning away, stolen or destroyed tactical options";

        case TAP_ALLY ->
        "allied unit restrained or exhausted, glowing restraints or powered-down stance";

    case UNTAP_ALLY ->
        "allied unit reactivated, energy surging back, ready stance restored";

    case TAP_ENEMY ->
        "enemy unit immobilized or suppressed, energy locks or disabling field";

    case UNTAP_ENEMY ->
        "enemy unit freed from restraints, systems reactivating or stance recovering";
    case RETURN_TO_HAND->
    "enemy unit being extracted from the battlefield by a teleportation beam";
    
            
		default -> throw new IllegalArgumentException("Unexpected value: " + ability);
        };
    }
    
    
    
}

