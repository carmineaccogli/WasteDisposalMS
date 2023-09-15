package it.unisalento.smartcitywastemanagement.disposalms.domain;


import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("wasteDisposal")
public class WasteDisposal {

    @Id
    private String id;

    private String citizenID;

    private String type;

    private GeoJsonPoint position;

    private Date timestamp;

    private Decimal128 amount;




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

    public Decimal128 getAmount() {
        return amount;
    }

    public void setAmount(Decimal128 amount) {
        this.amount = amount;
    }
}
