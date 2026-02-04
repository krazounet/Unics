package unics.snapshot;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import unics.Enum.AbilityType;
import unics.Enum.Keyword;
import unics.Enum.EffectConstraint;
import unics.Enum.TargetType;
import unics.Enum.TriggerType;

public final class EffectSnapshot {

    // ===== DECLENCHEUR =====
    public final TriggerType trigger;
    public final Keyword conditionKeyword; // nullable

    // ===== ACTION =====
    public final AbilityType ability;
    public final Integer value; // nullable

    // ===== CIBLAGE =====
    public final TargetType targetType;
    public final Set<EffectConstraint> constraints;

    public final String french;
    
    
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public EffectSnapshot(
        @JsonProperty("trigger") TriggerType trigger,
        @JsonProperty("conditionKeyword") Keyword conditionKeyword,
        @JsonProperty("ability") AbilityType ability,
        @JsonProperty("value") Integer value, // 👈 CRUCIAL
        @JsonProperty("targetType") TargetType targetType,
        @JsonProperty("constraints") Set<EffectConstraint> constraints,
        @JsonProperty("french") String french
    ) {
        this.trigger = trigger;
        this.conditionKeyword = conditionKeyword;
        this.ability = ability;
        this.value = value;
        this.targetType = targetType;
        this.constraints = constraints != null ? Set.copyOf(constraints) : Set.of();
        this.french = french;
    };
}
