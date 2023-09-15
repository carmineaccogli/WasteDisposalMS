package it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unisalento.smartcitywastemanagement.disposalms.utility.GeoJsonDeserializer;
import it.unisalento.smartcitywastemanagement.disposalms.utility.GeoJsonSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WasteDisposalMessage {


    @NotBlank(message = "smartBinID required")
    private String smartBinID;

    @NotBlank(message = "type required")
    private String type;

    @NotNull(message = "position required")
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    @JsonSerialize(using = GeoJsonSerializer.class)
    private GeoJsonPoint position;

    @NotNull(message = "timestamp required")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date timestamp;

    @NotNull(message = "amount required")
    private BigDecimal amount;

    @NotNull(message = "token required")
    private String token;


    public String getSmartBinID() {
        return smartBinID;
    }

    public void setSmartBinID(String smartBinID) {
        this.smartBinID = smartBinID;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
