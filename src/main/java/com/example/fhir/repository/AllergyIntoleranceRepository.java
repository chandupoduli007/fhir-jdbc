package com.example.fhir.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AllergyIntoleranceRepository {

    private  final JdbcTemplate jdbcTemplate;

    public AllergyIntoleranceRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> search(
            String patient,
            String clinicalStatus,
            String category,
            String criticality){
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM allergy_intolerance WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if(patient !=null){
            sql.append("AND patient_ref = ?");
            params.add(patient);
        }

        if(clinicalStatus !=null){
            sql.append("AND clinical_status = ?");
            params.add(clinicalStatus);
        }

        if(category !=null){
            sql.append("AND category = ?");
            params.add(category);
        }

        if(criticality !=null){
            sql.append("AND criticality = ?");
            params.add(criticality);
        }
        return jdbcTemplate.queryForList(
                sql.toString(),params.toArray()
        );

    }
}
