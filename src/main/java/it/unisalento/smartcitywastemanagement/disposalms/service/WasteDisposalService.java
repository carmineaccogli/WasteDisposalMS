package it.unisalento.smartcitywastemanagement.disposalms.service;

import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;

import java.util.List;

public interface WasteDisposalService {

    List<WasteDisposal> allDisposalsPerCitizen(String citizenID);

    List<WasteDisposal> lastDisposalsPerCitizen(String citizenID, int limit);
}
