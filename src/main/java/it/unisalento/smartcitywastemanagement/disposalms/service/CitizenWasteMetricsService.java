package it.unisalento.smartcitywastemanagement.disposalms.service;

import it.unisalento.smartcitywastemanagement.disposalms.domain.CitizenWasteMetrics;
import it.unisalento.smartcitywastemanagement.disposalms.exceptions.CitizenNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface CitizenWasteMetricsService {

    void updateMetrics(String type, BigDecimal amount, String citizenID);

    List<CitizenWasteMetrics> findAllMetrics();

    CitizenWasteMetrics findMetricsById(String citizenID) throws CitizenNotFoundException;

    Float calculateCitizenPerformance(String citizenID, int year) throws CitizenNotFoundException;


    List<CitizenWasteMetrics> findAllMetricsByYear(int year);
}
