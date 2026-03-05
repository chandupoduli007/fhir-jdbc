package com.example.fhir.mapper;

import ca.uhn.fhir.model.api.annotation.Description;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.util.Map;

@Component
public class AllergyIntoleranceMapper {

    public AllergyIntolerance toFhir(Map<String, Object> row) {

        AllergyIntolerance ai = new AllergyIntolerance();

        // 1. Set ID
        ai.setId((String) row.get("id"));

        // 2. Set Clinical Status
        String clinicalStatus = (String) row.get("clinical_status");
        ai.setClinicalStatus(new CodeableConcept()
                .addCoding(new Coding()
                        .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
                        .setCode(clinicalStatus)
                )
        );

        // 3. Set Category
        String category = (String) row.get("category");
        if (category != null) {
            ai.addCategory(
                    AllergyIntolerance.AllergyIntoleranceCategory.fromCode(category)
            );
        }

        // 4. Set Criticality
        String criticality = (String) row.get("criticality");
        if (criticality != null) {
            ai.setCriticality(
                    AllergyIntolerance.AllergyIntoleranceCriticality.fromCode(criticality)
            );
        }

        // 5. Set Code
        ai.setCode(new CodeableConcept()
                .addCoding(new Coding()
                        .setSystem((String) row.get("code_system"))
                        .setCode((String) row.get("code_code"))
                        .setDisplay((String) row.get("code_display"))
                )
        );

        // 6. Set Patient Reference
        ai.setPatient(new Reference(
                (String) row.get("patient_ref")
        ));

        // 7. Set Recorded Date
        if (row.get("recorded_date") != null) {
            ai.setRecordedDate(
                    Date.valueOf(row.get("recorded_date").toString())
            );
        }

        // 8. Set Note
        String note = (String) row.get("note");
        if (note != null) {
            ai.addNote(new Annotation().setText(note));
        }

        return ai;
    }
}