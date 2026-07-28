package com.medguard.service;

import com.medguard.dao.*;
import com.medguard.model.CurrentMedicine;
import com.medguard.model.Medicine;
import com.medguard.model.SafetyResult;
import com.medguard.model.SafetyResult.Level;

import java.util.List;
import java.util.Locale;

/**
 * The heart of MedGuard AI (Modules 8, 9, 10).
 * Runs three engines against one candidate medicine:
 *   1. Drug-Drug Interaction Engine  - candidate vs the user's current medicines
 *   2. Disease Warning Engine        - candidate vs the user's diseases
 *   3. Allergy Checker               - candidate ingredients vs the user's allergies
 * Every finding escalates the overall verdict (SAFE -> CAUTION -> DANGEROUS)
 * and adds a plain-language reason. The result is stored in medicine_history.
 */
public class SafetyService {

    private final InteractionDAO interactionDAO = new InteractionDAO();
    private final DiseaseWarningDAO warningDAO = new DiseaseWarningDAO();
    private final AllergyDAO allergyDAO = new AllergyDAO();
    private final CurrentMedicineDAO currentDAO = new CurrentMedicineDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();

    /** Runs all three engines, saves the check to history, returns the result. */
    public SafetyResult check(int userId, Medicine candidate) {
        SafetyResult result = new SafetyResult();

        runInteractionEngine(userId, candidate, result);
        runDiseaseEngine(userId, candidate, result);
        runAllergyEngine(userId, candidate, result);

        if (result.getReasons().isEmpty()) {
            result.getReasons().add("No conflicts found with your current medicines, diseases or allergies.");
        }

        historyDAO.save(userId, candidate.getMedicineId(),
                        result.getLevel().name(), result.reasonsAsText());
        return result;
    }

    /** Engine 1: drug-drug rules between the candidate and each current medicine. */
    private void runInteractionEngine(int userId, Medicine candidate, SafetyResult result) {
        List<CurrentMedicine> current = currentDAO.findByUser(userId);
        for (CurrentMedicine cm : current) {
            if (cm.getMedicineId() == candidate.getMedicineId()) {
                result.escalate(Level.CAUTION,
                        "You are already taking " + cm.getMedicineName() + " - avoid double dosing.");
                continue;
            }
            InteractionDAO.Rule rule = interactionDAO.findBetween(candidate.getMedicineId(), cm.getMedicineId());
            if (rule != null) {
                Level level = "HIGH".equals(rule.risk) ? Level.DANGEROUS : Level.CAUTION;
                result.escalate(level, "Interacts with your current medicine "
                        + cm.getMedicineName() + " (" + rule.risk + " risk): " + rule.reason);
            }
        }
    }

    /** Engine 2: drug-disease rules for diseases in the user's medical profile. */
    private void runDiseaseEngine(int userId, Medicine candidate, SafetyResult result) {
        for (DiseaseWarningDAO.Rule rule :
                warningDAO.findForMedicineAndUser(candidate.getMedicineId(), userId)) {
            Level level = "NOT_RECOMMENDED".equals(rule.severity) ? Level.DANGEROUS : Level.CAUTION;
            result.escalate(level, "Because you have " + rule.disease + ": " + rule.reason);
        }
    }

    /** Engine 3: substring match of the user's allergies against the ingredient list. */
    private void runAllergyEngine(int userId, Medicine candidate, SafetyResult result) {
        String ingredients = candidate.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) return;
        String haystack = ingredients.toLowerCase(Locale.ROOT);
        for (String allergy : allergyDAO.namesByUser(userId)) {
            if (allergy != null && !allergy.isBlank()
                    && haystack.contains(allergy.trim().toLowerCase(Locale.ROOT))) {
                result.escalate(Level.DANGEROUS,
                        "Contains \"" + allergy.trim() + "\" which is in your allergy list.");
            }
        }
    }
}
