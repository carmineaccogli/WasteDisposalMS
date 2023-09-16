package it.unisalento.smartcitywastemanagement.disposalms.mappers;

import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import it.unisalento.smartcitywastemanagement.disposalms.dto.WasteDisposalDTO;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.WasteDisposalMessage;
import org.bson.types.Decimal128;
import org.springframework.stereotype.Component;


@Component
public class WasteDisposalMapper {

    public WasteDisposalDTO toWasteDisposalDTO(WasteDisposal wasteDisposal) {

        WasteDisposalDTO wasteDisposalDTO = new WasteDisposalDTO();

        wasteDisposalDTO.setId(wasteDisposal.getId());
        wasteDisposalDTO.setCitizenID(wasteDisposal.getCitizenID());
        wasteDisposalDTO.setAmount(wasteDisposal.getAmount().bigDecimalValue());
        wasteDisposalDTO.setType(wasteDisposal.getType());
        wasteDisposalDTO.setPosition(wasteDisposal.getPosition());
        wasteDisposalDTO.setTimestamp(wasteDisposal.getTimestamp());

        return wasteDisposalDTO;
    }

    public WasteDisposal toWasteDisposalFromMessage(WasteDisposalMessage wasteDisposalMessage, String citizenID) {

        WasteDisposal wasteDisposal = new WasteDisposal();

        wasteDisposal.setCitizenID(citizenID);
        wasteDisposal.setAmount(new Decimal128(wasteDisposalMessage.getAmount()));
        wasteDisposal.setType(wasteDisposalMessage.getType());
        wasteDisposal.setTimestamp(wasteDisposalMessage.getTimestamp());
        wasteDisposal.setPosition(wasteDisposalMessage.getPosition());

        return wasteDisposal;
    }
}
