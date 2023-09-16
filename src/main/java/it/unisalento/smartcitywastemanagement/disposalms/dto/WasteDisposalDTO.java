package it.unisalento.smartcitywastemanagement.disposalms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unisalento.smartcitywastemanagement.disposalms.utility.GeoJsonDeserializer;
import it.unisalento.smartcitywastemanagement.disposalms.utility.GeoJsonSerializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.util.Date;

public class WasteDisposalDTO {

    private String id;

    private String citizenID;

    private String type;

    @JsonSerialize(using =GeoJsonSerializer.class)
    @JsonDeserialize(using =GeoJsonDeserializer.class)
    private GeoJsonPoint position;

    private Date timestamp;

    private BigDecimal amount;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
