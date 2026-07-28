package com.medguard.model;

/** JavaBean for the "diseases" master table. */
public class Disease {
    private int diseaseId;
    private String diseaseName;
    private String description;
    private String diagnosedOn;   // only filled when listed as part of a user's profile

    public int getDiseaseId() { return diseaseId; }
    public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId; }
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiagnosedOn() { return diagnosedOn; }
    public void setDiagnosedOn(String diagnosedOn) { this.diagnosedOn = diagnosedOn; }
}
