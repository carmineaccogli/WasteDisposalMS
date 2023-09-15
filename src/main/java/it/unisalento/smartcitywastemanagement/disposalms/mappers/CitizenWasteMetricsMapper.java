package it.unisalento.smartcitywastemanagement.disposalms.mappers;

import it.unisalento.smartcitywastemanagement.disposalms.domain.CitizenWasteMetrics;
import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import it.unisalento.smartcitywastemanagement.disposalms.dto.CitizenWasteMetricsDTO;
import it.unisalento.smartcitywastemanagement.disposalms.dto.WasteDisposalDTO;
import org.springframework.stereotype.Component;


@Component
public class CitizenWasteMetricsMapper {


    public CitizenWasteMetricsDTO toWasteMetricsDTO(CitizenWasteMetrics citizenWasteMetrics) {

        CitizenWasteMetricsDTO wasteMetricsDTO = new CitizenWasteMetricsDTO();

        wasteMetricsDTO.setCitizenID(citizenWasteMetrics.getCitizenID());
        wasteMetricsDTO.setYearlyVolumes(citizenWasteMetrics.getYearlyVolumes());

        return wasteMetricsDTO;
    }

}
