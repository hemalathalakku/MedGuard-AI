package com.medguard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Output of the SafetyService: the overall verdict plus every reason,
 * produced by the three engines (drug-drug, drug-disease, allergy).
 */
public class SafetyResult {
    /** Verdict levels ordered by severity - ordinal comparison gives the worst. */
    public enum Level { SAFE, CAUTION, DANGEROUS }

    private Level level = Level.SAFE;
    private final List<String> reasons = new ArrayList<>();

    /** Raise the verdict if the new level is more severe than the current one. */
    public void escalate(Level newLevel, String reason) {
        if (newLevel.ordinal() > level.ordinal()) level = newLevel;
        reasons.add(reason);
    }

    public Level getLevel() { return level; }
    public List<String> getReasons() { return reasons; }

    /** Reasons joined with newlines - what gets stored in medicine_history.warnings. */
    public String reasonsAsText() { return String.join("\n", reasons); }
}
