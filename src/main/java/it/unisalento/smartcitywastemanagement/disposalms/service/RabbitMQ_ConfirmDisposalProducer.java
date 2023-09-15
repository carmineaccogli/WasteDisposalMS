package it.unisalento.smartcitywastemanagement.disposalms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.SmartBinUpdateMessage;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.WasteDisposalResultMessage;

public interface RabbitMQ_ConfirmDisposalProducer {

    void produceResultDisposal(WasteDisposalResultMessage message);

    void produceSmartBinUpdate(SmartBinUpdateMessage jsonMessage);
}
