package it.unisalento.smartcitywastemanagement.disposalms.service;


import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import it.unisalento.smartcitywastemanagement.disposalms.repositories.WasteDisposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteDisposalServiceImpl implements WasteDisposalService{

    @Autowired
    private WasteDisposalRepository wasteDisposalRepository;


    public List<WasteDisposal> allDisposalsPerCitizen(String citizenID) {

        // query per ottenere i conferimenti relativi a un certo citizenID
        return wasteDisposalRepository.findByCitizenIDOrderByTimestampDesc(citizenID);
    }


    public List<WasteDisposal> lastDisposalsPerCitizen(String citizenID, int limit) {

        return wasteDisposalRepository.findFirstNByCitizenIDOrderByTimestampDesc(citizenID, limit);
    }



}
