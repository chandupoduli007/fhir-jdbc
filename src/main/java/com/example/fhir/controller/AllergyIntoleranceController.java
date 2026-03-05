package com.example.fhir.controller;

import ca.uhn.fhir.parser.IParser;
import com.example.fhir.mapper.AllergyIntoleranceMapper;
import com.example.fhir.repository.AllergyIntoleranceRepository;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fhir")
public class AllergyIntoleranceController {

    private final AllergyIntoleranceRepository repository;
    private final AllergyIntoleranceMapper mapper;
    private final IParser fhirParser;

    public AllergyIntoleranceController(
            AllergyIntoleranceRepository repository,
            AllergyIntoleranceMapper mapper,
            IParser fhirParser) {
        this.repository = repository;
        this.mapper = mapper;
        this.fhirParser = fhirParser;
    }

    // SEARCH
    @GetMapping(value = "/AllergyIntolerance",
            produces = "application/fhir+json")
    public ResponseEntity<String> search(
            @RequestParam(required = false) String patient,
            @RequestParam(required = false) String clinicalStatus,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String criticality) {

        List<Map<String, Object>> rows = repository.search(
                patient, clinicalStatus, category, criticality
        );

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(rows.size());

        for (Map<String, Object> row : rows) {
            AllergyIntolerance resource = mapper.toFhir(row);
            bundle.addEntry()
                    .setFullUrl("AllergyIntolerance/" + resource.getIdElement().getIdPart())
                    .setResource(resource);
        }

        return ResponseEntity.ok(
                fhirParser.encodeResourceToString(bundle)
        );
    }
}