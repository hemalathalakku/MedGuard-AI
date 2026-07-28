package com.medguard.model;

/** JavaBean for one safety-check stored in medicine_history. */
public class HistoryEntry {
    private int historyId;
    private int userId;
    private int medicineId;
    private String medicineName;
    private String result;      // SAFE / CAUTION / DANGEROUS
    private String warnings;    // newline separated reasons
    private String checkedAt;

    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getWarnings() { return warnings; }
    public void setWarnings(String warnings) { this.warnings = warnings; }
    public String getCheckedAt() { return checkedAt; }
    public void setCheckedAt(String checkedAt) { this.checkedAt = checkedAt; }
}
