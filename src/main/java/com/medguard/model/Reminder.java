package com.medguard.model;

/** JavaBean for the "reminders" table (medicine name joined in for display). */
public class Reminder {
    private int reminderId;
    private int userId;
    private int medicineId;
    private String medicineName;
    private String frequency;   // DAILY / WEEKLY / MONTHLY
    private String remindTime;  // HH:MM
    private String detail;      // e.g. "Monday" for weekly, "Day 15" for monthly
    private boolean active;

    public int getReminderId() { return reminderId; }
    public void setReminderId(int reminderId) { this.reminderId = reminderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getRemindTime() { return remindTime; }
    public void setRemindTime(String remindTime) { this.remindTime = remindTime; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
