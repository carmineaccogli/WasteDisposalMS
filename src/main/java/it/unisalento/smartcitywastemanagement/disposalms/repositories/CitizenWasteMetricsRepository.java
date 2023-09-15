package it.unisalento.smartcitywastemanagement.disposalms.repositories;


import it.unisalento.smartcitywastemanagement.disposalms.domain.CitizenWasteMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CitizenWasteMetricsRepository extends MongoRepository<CitizenWasteMetrics, String> {


    Optional<CitizenWasteMetrics> findByCitizenID(String citizenID);

    List<CitizenWasteMetrics> findAll();

    @Query(value = "{ 'yearlyVolumes': { '$elemMatch': { 'year': ?0 } } }", fields = "{ 'yearlyVolumes.$': 1, 'id': 1, 'citizenID': 1 }")
    List<CitizenWasteMetrics> findWasteMetricsAndVolumesByYear(int yearToFind);

}
