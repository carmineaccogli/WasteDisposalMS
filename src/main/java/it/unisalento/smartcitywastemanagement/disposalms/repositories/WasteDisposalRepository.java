package it.unisalento.smartcitywastemanagement.disposalms.repositories;

import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WasteDisposalRepository extends MongoRepository<WasteDisposal, String> {

    List<WasteDisposal> findByCitizenIDOrderByTimestampDesc(String citizenID);

    List<WasteDisposal> findByCitizenIDOrderByTimestampDesc(String citizenID, Pageable pageable);
}
