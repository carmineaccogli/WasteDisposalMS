package it.unisalento.smartcitywastemanagement.disposalms.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("citizenWasteMetrics")
public class CitizenWasteMetrics {

    @Id
    private String id;

    @Indexed(unique = true)
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
