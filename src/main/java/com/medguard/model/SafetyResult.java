
package com.medguard.model;

import java.util.ArrayList;
import java.util.List;

public class SafetyResult {

    public enum Level {
        NOT_VERIFIED,
        SAFE,
        CAUTION,
        DANGEROUS
    }

    private Level level = Level.NOT_VERIFIED;

    private final List<String> reasons = new ArrayList<>();

    public void escalate(Level newLevel, String reason) {

        if (priority(newLevel) > priority(level)) {
            level = newLevel;
        }

        reasons.add(reason);
    }

    private int priority(Level level) {
        switch (level) {
            case NOT_VERIFIED: return 0;
            case SAFE: return 1;
            case CAUTION: return 2;
            case DANGEROUS: return 3;
            default: return 0;
        }
    }

    public Level getLevel() {
        return level;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String reasonsAsText() {
        return String.join("\n", reasons);
    }
}
