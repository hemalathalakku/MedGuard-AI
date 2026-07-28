package com.medguard.model;

/** JavaBean joining current_medicines with the medicine name for display. */
public class CurrentMedicine {
    private int id;
    private int userId;
    private int medicineId;
    private String medicineName;
    private String dose;
    private String frequency;
    private Integer durationDays;
    private String doctorName;
    private String notes;
    private String startedOn;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStartedOn() { return startedOn; }
    public void setStartedOn(String startedOn) { this.startedOn = startedOn; }
}
