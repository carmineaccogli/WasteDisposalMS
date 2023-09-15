package it.unisalento.smartcitywastemanagement.disposalms.restcontrollers;

import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import it.unisalento.smartcitywastemanagement.disposalms.dto.WasteDisposalDTO;
import it.unisalento.smartcitywastemanagement.disposalms.mappers.WasteDisposalMapper;
import it.unisalento.smartcitywastemanagement.disposalms.service.WasteDisposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/disposal")
public class WasteDisposalRestController {

    @Autowired
    private WasteDisposalService wasteDisposalService;

    @Autowired
    private WasteDisposalMapper wasteDisposalMapper;

    // API per ottenere la lista di tutti i conferimenti per ID cittadino (ordinamento per data)

    @RequestMapping(value="/citizen/{citizenID}", method = RequestMethod.GET)
    public ResponseEntity<List<WasteDisposalDTO>> getAllDisposalsByCitizen(@PathVariable("citizenID") String citizenID) {

        List<WasteDisposal> results = wasteDisposalService.allDisposalsPerCitizen(citizenID);

        List<WasteDisposalDTO> citizenDisposals = fromWasteDisposalToDTOArray(results);

        if (citizenDisposals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(citizenDisposals);
    }

    // API per ottenere gli ultimi 4 conferimenti per ID cittadino (ordinamento per data)
    @RequestMapping(value="/citizen/{citizenID}/last/{limit}", method = RequestMethod.GET)
    public ResponseEntity<List<WasteDisposalDTO>> getLastDisposalsByCitizen(@PathVariable("citizenID") String citizenID,@PathVariable("limit") int limit) {

        List<WasteDisposal> results = wasteDisposalService.lastDisposalsPerCitizen(citizenID, limit);

        List<WasteDisposalDTO> citizenLastDisposals = fromWasteDisposalToDTOArray(results);

        if (citizenLastDisposals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(citizenLastDisposals);
    }





    private List<WasteDisposalDTO> fromWasteDisposalToDTOArray(List<WasteDisposal> entityDisposals) {
        List<WasteDisposalDTO> result = new ArrayList<>();

        for(WasteDisposal disposal: entityDisposals) {
            WasteDisposalDTO wasteDisposalDTO = wasteDisposalMapper.toWasteDisposalDTO(disposal);
            result.add(wasteDisposalDTO);
        }
        return result;
    }

}
