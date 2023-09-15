package it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages;

import java.math.BigDecimal;

public class SmartBinUpdateMessage {

    private String smartBinID;

    private BigDecimal amount;


    public String getSmartBinID() {
        return smartBinID;
    }

    public void setSmartBinID(String smartBinID) {
        this.smartBinID = smartBinID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public SmartBinUpdateMessage(String smartBinID, BigDecimal amount) {
        this.smartBinID = smartBinID;
        this.amount = amount;
    }
}
