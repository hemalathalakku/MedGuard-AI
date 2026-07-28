-- ============================================================
--  MedGuard AI - Sample data (run AFTER schema.sql)
-- ============================================================
USE medguard;

-- Admin login  =>  admin / admin123
INSERT INTO admin (username, password) VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

-- Demo user    =>  demo@medguard.com / user1234
INSERT INTO users (full_name, email, password, age, gender, height_cm, weight_kg,
                   blood_group, emergency_contact, address)
VALUES ('Demo User', 'demo@medguard.com', '831c237928e6212bedaa4451a514ace3174562f6761f6a157a2fe5082b36e2fb', 32, 'Male', 172.0, 70.5,
        'B+', '9876543210', 'Vijayawada, Andhra Pradesh');

INSERT INTO diseases (disease_name, description) VALUES
('Heart Disease','Conditions affecting the heart'),
('Diabetes','High blood sugar disorders'),
('Kidney Disease','Reduced kidney function'),
('Liver Disease','Reduced liver function'),
('Cancer','Malignant growth conditions'),
('Asthma','Chronic airway inflammation'),
('Hypertension','High blood pressure'),
('Pregnancy','Pregnant or planning pregnancy'),
('High Cholesterol','Elevated blood lipids'),
('Thyroid Disorders','Thyroid hormone imbalance');

INSERT INTO medicines (medicine_name, generic_name, brand, manufacturer, ingredients, description,
  uses, dosage, side_effects, contraindications, warnings, pregnancy_category,
  storage_instructions, age_restrictions) VALUES
('Ibuprofen','Ibuprofen','Brufen','Abbott','ibuprofen',
 'A non-steroidal anti-inflammatory drug (NSAID).','Pain relief, fever, inflammation.',
 'As directed by a physician.','Stomach upset, dizziness.','Severe kidney disease, active stomach ulcer.',
 'May raise blood pressure; caution in heart disease.','C','Store below 25°C.','Not for children under 6 without medical advice.'),
('Paracetamol','Acetaminophen','Crocin','GSK','paracetamol',
 'A common analgesic and antipyretic.','Mild pain and fever.',
 'As directed by a physician.','Rare at normal doses.','Severe liver disease.',
 'Do not exceed the maximum daily dose.','B','Store below 30°C.','Dose depends on age and weight.'),
('Warfarin','Warfarin','Coumadin','Bristol-Myers Squibb','warfarin',
 'An anticoagulant (blood thinner).','Prevention of blood clots.',
 'Individualized; requires INR monitoring.','Bleeding, bruising.','Active bleeding, pregnancy.',
 'Many food and drug interactions.','X','Room temperature.','Adults; specialist supervision.'),
('Aspirin','Acetylsalicylic acid','Disprin','Reckitt','aspirin, acetylsalicylic acid',
 'An NSAID also used as an antiplatelet agent.','Pain, fever; low dose for heart protection.',
 'As directed by a physician.','Stomach irritation, bleeding risk.','Bleeding disorders.',
 'Avoid combining with other blood thinners.','D','Cool, dry place.','Not for children under 16.'),
('Amoxicillin','Amoxicillin','Mox','Ranbaxy','amoxicillin, penicillin',
 'A penicillin-class antibiotic.','Bacterial infections.',
 'As prescribed; complete the full course.','Nausea, rash.','Penicillin allergy.',
 'Tell your doctor about any antibiotic allergy.','B','Store below 25°C.','All ages as prescribed.'),
('Metformin','Metformin','Glycomet','USV','metformin',
 'First-line medication for type 2 diabetes.','Blood sugar control.',
 'As prescribed with meals.','Stomach upset.','Severe kidney disease.',
 'Kidney function should be monitored.','B','Room temperature.','Adults.');

-- Drug-disease rules (Disease Warning Engine)
INSERT INTO medicine_disease_warnings (medicine_id, disease_id, severity, reason) VALUES
(1, 3, 'NOT_RECOMMENDED', 'Ibuprofen may worsen kidney function.'),
(1, 7, 'CAUTION',         'Ibuprofen may increase blood pressure.'),
(1, 1, 'NOT_RECOMMENDED', 'NSAIDs are avoided in severe heart disease.'),
(2, 4, 'CAUTION',         'Use paracetamol cautiously in liver disease.'),
(4, 8, 'NOT_RECOMMENDED', 'Aspirin is generally avoided during pregnancy.'),
(6, 3, 'NOT_RECOMMENDED', 'Metformin is contraindicated in severe kidney disease.');

-- Drug-drug rules (Interaction Engine)
INSERT INTO medicine_interactions (medicine_a_id, medicine_b_id, risk_level, reason) VALUES
(3, 4, 'HIGH',     'Warfarin + Aspirin greatly increases bleeding risk.'),
(1, 4, 'MODERATE', 'Two NSAIDs together raise stomach-bleeding risk.'),
(1, 3, 'HIGH',     'Ibuprofen increases bleeding risk with Warfarin.');

-- Demo user's medical profile: has Hypertension, allergic to penicillin, takes Warfarin
INSERT INTO user_diseases (user_id, disease_id, diagnosed_on) VALUES (1, 7, '2023-05-10');
INSERT INTO allergies (user_id, allergy_name) VALUES (1, 'Penicillin');
INSERT INTO current_medicines (user_id, medicine_id, dose, frequency, duration_days, doctor_name, notes, started_on)
VALUES (1, 3, '5 mg', 'Once daily', 90, 'Dr. Rao', 'Take at the same time each day', '2025-01-15');
