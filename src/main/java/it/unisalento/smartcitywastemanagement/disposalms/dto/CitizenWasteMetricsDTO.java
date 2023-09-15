package it.unisalento.smartcitywastemanagement.disposalms.dto;

import it.unisalento.smartcitywastemanagement.disposalms.domain.GeneratedVolumePerYear;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

public class CitizenWasteMetricsDTO {

    private String id;
    private String citizenID;
    private List<GeneratedVolumePerYear> yearlyVolumes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public List<GeneratedVolumePerYear> getYearlyVolumes() {
        return yearlyVolumes;
    }

    public void setYearlyVolumes(List<GeneratedVolumePerYear> yearlyVolumes) {
        this.yearlyVolumes = yearlyVolumes;
    }
}
