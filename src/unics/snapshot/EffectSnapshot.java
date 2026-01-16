package unics.snapshot;

import java.util.Set;

import unics.Enum.AbilityType;
import unics.Enum.Keyword;
import unics.Enum.TargetConstraint;
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
    public final Set<TargetConstraint> constraints;

    public EffectSnapshot(
        TriggerType trigger,
        Keyword conditionKeyword,
        AbilityType ability,
        Integer value,
        TargetType targetType,
        Set<TargetConstraint> constraints
    ) {
        this.trigger = trigger;
        this.conditionKeyword = conditionKeyword;
        this.ability = ability;
        this.value = value;
        this.targetType = targetType;
        this.constraints = Set.copyOf(constraints);
    }
}
