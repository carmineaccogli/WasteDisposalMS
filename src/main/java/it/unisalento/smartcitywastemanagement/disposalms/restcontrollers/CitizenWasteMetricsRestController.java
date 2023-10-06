package it.unisalento.smartcitywastemanagement.disposalms.restcontrollers;


import it.unisalento.smartcitywastemanagement.disposalms.domain.CitizenWasteMetrics;
import it.unisalento.smartcitywastemanagement.disposalms.dto.CitizenWasteMetricsDTO;
import it.unisalento.smartcitywastemanagement.disposalms.dto.ResponseDTO;
import it.unisalento.smartcitywastemanagement.disposalms.exceptions.CitizenNotFoundException;
import it.unisalento.smartcitywastemanagement.disposalms.mappers.CitizenWasteMetricsMapper;
import it.unisalento.smartcitywastemanagement.disposalms.service.CitizenWasteMetricsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/api/disposal/metrics")
@Validated
public class CitizenWasteMetricsRestController {

    @Autowired
    CitizenWasteMetricsService citizenWasteMetricsService;

    @Autowired
    CitizenWasteMetricsMapper citizenWasteMetricsMapper;

    @RequestMapping(value = "/{citizenID}/performance/{year}", method = RequestMethod.GET)
    public ResponseEntity<ResponseDTO> getCitizenPerformance(@PathVariable("citizenID") String citizenID, @PathVariable("year") @NotNull @Min(2000) @Max(2100) int year)  {

        Float performance = citizenWasteMetricsService.calculateCitizenPerformance(citizenID, year);
        if(performance == null)
            return new ResponseEntity<>(new ResponseDTO("year requested: "+year,""), HttpStatus.OK);

        return new ResponseEntity<>(new ResponseDTO("year requested: "+year, "Performance: "+performance), HttpStatus.OK);
    }

    @RequestMapping(value="/", method=RequestMethod.GET)
    public ResponseEntity<List<CitizenWasteMetricsDTO>> getAllCitizensMetrics() {

        List<CitizenWasteMetrics> results = citizenWasteMetricsService.findAllMetrics();

        List<CitizenWasteMetricsDTO> allMetrics = fromMetricToDTOArray(results);

        if(allMetrics.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(allMetrics);
    }

    @RequestMapping(value="/year/{year}", method=RequestMethod.GET)
    public ResponseEntity<List<CitizenWasteMetricsDTO>> getAllCitizensMetricsByYear(@PathVariable("year") @Min(2000) @Max(2100) int year) {

        List<CitizenWasteMetrics> results = citizenWasteMetricsService.findAllMetricsByYear(year);

        List<CitizenWasteMetricsDTO> allMetrics = fromMetricToDTOArray(results);

        if(allMetrics.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(allMetrics);
    }


    @RequestMapping(value = "/citizen/{citizenID}", method=RequestMethod.GET)
    public ResponseEntity<CitizenWasteMetricsDTO> getMetricsById(@PathVariable("citizenID") String citizenID)  {

        CitizenWasteMetrics citizenWasteMetrics = citizenWasteMetricsService.findMetricsById(citizenID);

        if( citizenWasteMetrics == null) {
            return ResponseEntity.noContent().build();
        }else {
            CitizenWasteMetricsDTO result = citizenWasteMetricsMapper.toWasteMetricsDTO(citizenWasteMetrics);
            return ResponseEntity.ok(result);
        }
    }


    private List<CitizenWasteMetricsDTO> fromMetricToDTOArray(List<CitizenWasteMetrics> entityWasteMetrics) {
        List<CitizenWasteMetricsDTO> result = new ArrayList<>();

        for(CitizenWasteMetrics wasteMetrics: entityWasteMetrics) {
            CitizenWasteMetricsDTO wasteMetricsDTO = citizenWasteMetricsMapper.toWasteMetricsDTO(wasteMetrics);
            result.add(wasteMetricsDTO);
        }
        return result;
    }

}
