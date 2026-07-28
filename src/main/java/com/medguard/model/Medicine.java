package com.medguard.model;

/** JavaBean mapping one row of the "medicines" table. */
public class Medicine {
    private int medicineId;
    private String medicineName;
    private String genericName;
    private String brand;
    private String manufacturer;
    private String ingredients;
    private String description;
    private String uses;
    private String dosage;
    private String sideEffects;
    private String contraindications;
    private String warnings;
    private String pregnancyCategory;
    private String storageInstructions;
    private String ageRestrictions;

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUses() { return uses; }
    public void setUses(String uses) { this.uses = uses; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getSideEffects() { return sideEffects; }
    public void setSideEffects(String sideEffects) { this.sideEffects = sideEffects; }
    public String getContraindications() { return contraindications; }
    public void setContraindications(String contraindications) { this.contraindications = contraindications; }
    public String getWarnings() { return warnings; }
    public void setWarnings(String warnings) { this.warnings = warnings; }
    public String getPregnancyCategory() { return pregnancyCategory; }
    public void setPregnancyCategory(String pregnancyCategory) { this.pregnancyCategory = pregnancyCategory; }
    public String getStorageInstructions() { return storageInstructions; }
    public void setStorageInstructions(String storageInstructions) { this.storageInstructions = storageInstructions; }
    public String getAgeRestrictions() { return ageRestrictions; }
    public void setAgeRestrictions(String ageRestrictions) { this.ageRestrictions = ageRestrictions; }
}
