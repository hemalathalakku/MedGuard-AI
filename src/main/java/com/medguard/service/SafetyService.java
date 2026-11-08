
package com.medguard.service;

import com.medguard.dao.*;
import com.medguard.model.CurrentMedicine;
import com.medguard.model.Medicine;
import com.medguard.model.SafetyResult;
import com.medguard.model.SafetyResult.Level;

import java.util.List;
import java.util.Locale;

public class SafetyService {

    private final InteractionDAO interactionDAO = new InteractionDAO();
    private final DiseaseWarningDAO warningDAO = new DiseaseWarningDAO();
    private final AllergyDAO allergyDAO = new AllergyDAO();
    private final CurrentMedicineDAO currentDAO = new CurrentMedicineDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();

    public SafetyResult check(int userId, Medicine candidate) {

        SafetyResult result = new SafetyResult();

        runInteractionEngine(userId, candidate, result);
        runDiseaseEngine(userId, candidate, result);
        runAllergyEngine(userId, candidate, result);

        // Missing reference data must never be treated as SAFE.
        if (interactionDAO.findAll().isEmpty()) {
            result.escalate(Level.NOT_VERIFIED,
                    "Drug interaction reference data is not available.");
        }

        if (warningDAO.findAll().isEmpty()) {
            result.escalate(Level.NOT_VERIFIED,
                    "Medicine-disease warning reference data is not available.");
        }

        if (candidate.getIngredients() == null
                || candidate.getIngredients().isBlank()) {
            result.escalate(Level.NOT_VERIFIED,
                    "Medicine ingredient information is missing.");
        } else {
            result.escalate(Level.NOT_VERIFIED,
                    "Allergy matching uses limited ingredient text and is not a complete allergen verification.");
        }

        if (result.getReasons().isEmpty()) {
            result.escalate(Level.NOT_VERIFIED,
                    "Available information is insufficient to verify medicine safety.");
        }

        historyDAO.save(
                userId,
                candidate.getMedicineId(),
                result.getLevel().name(),
                result.reasonsAsText()
        );

        return result;
    }

    private void runInteractionEngine(
            int userId, Medicine candidate, SafetyResult result) {

        List<CurrentMedicine> current = currentDAO.findByUser(userId);

        for (CurrentMedicine cm : current) {

            if (cm.getMedicineId() == candidate.getMedicineId()) {
                result.escalate(Level.CAUTION,
                        "You are already taking " + cm.getMedicineName()
                        + " - avoid double dosing.");
                continue;
            }

            InteractionDAO.Rule rule =
                    interactionDAO.findBetween(
                            candidate.getMedicineId(),
                            cm.getMedicineId()
                    );

            if (rule != null) {
                Level level = "HIGH".equals(rule.risk)
                        ? Level.DANGEROUS : Level.CAUTION;

                result.escalate(level,
                        "Interacts with your current medicine "
                        + cm.getMedicineName()
                        + " (" + rule.risk + " risk): "
                        + rule.reason);
            }
        }
    }

    private void runDiseaseEngine(
            int userId, Medicine candidate, SafetyResult result) {

        for (DiseaseWarningDAO.Rule rule :
                warningDAO.findForMedicineAndUser(
                        candidate.getMedicineId(), userId)) {

            Level level = "NOT_RECOMMENDED".equals(rule.severity)
                    ? Level.DANGEROUS : Level.CAUTION;

            result.escalate(level,
                    "Because you have " + rule.disease
                    + ": " + rule.reason);
        }
    }

    private void runAllergyEngine(
            int userId, Medicine candidate, SafetyResult result) {

        String ingredients = candidate.getIngredients();

        if (ingredients == null || ingredients.isBlank()) {
            return;
        }

        String haystack = ingredients.toLowerCase(Locale.ROOT);

        for (String allergy : allergyDAO.namesByUser(userId)) {

            if (allergy != null && !allergy.isBlank()
                    && haystack.contains(
                            allergy.trim().toLowerCase(Locale.ROOT))) {

                result.escalate(Level.DANGEROUS,
                        "Contains \"" + allergy.trim()
                        + "\" which is in your allergy list.");
            }
        }
    }
}
