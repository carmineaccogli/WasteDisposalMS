package it.unisalento.smartcitywastemanagement.disposalms.service;

import it.unisalento.smartcitywastemanagement.disposalms.domain.CitizenWasteMetrics;
import it.unisalento.smartcitywastemanagement.disposalms.domain.GeneratedVolumePerYear;
import it.unisalento.smartcitywastemanagement.disposalms.exceptions.CitizenNotFoundException;
import it.unisalento.smartcitywastemanagement.disposalms.repositories.CitizenWasteMetricsRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.*;


@Service
public class CitizenWasteMetricsServiceImpl implements CitizenWasteMetricsService{

    @Autowired
    CitizenWasteMetricsRepository citizenWasteMetricsRepository;


    /** FUNZIONE PER AGGIORNARE LE STATISTICHE DEI CONFERIMENTI
     *
     *   1. Find per citizenID all'interno della collection
     *   2. Controllare parametro type
     *          2.1 Se è type="MixedWaste", aggiornare il campo mixedWaste
     *          2.2 Altrimenti bisogna aggiornare il campo sortedWaste
     *   3. Save dell'entità citizenWasteMetrics modificata opportunamente
     **/

    public void updateMetrics(String type, BigDecimal amountToAdd, String citizenID) {

        // 1
        CitizenWasteMetrics citizenWasteMetrics = getWasteMetricByCitizenID(citizenID);

        // 2
        if(type.equals("Mixed waste")) {
            updateMixedWaste(citizenWasteMetrics, amountToAdd);
        }
        else {
            updateSortedWaste(citizenWasteMetrics, amountToAdd, type);
        }

        // 3
        citizenWasteMetricsRepository.save(citizenWasteMetrics);

    }


    /** FUNZIONE PER INIZIALIZZARE IL CAMPO "yearlyVolumes" DI "CitizenWasteMetrics"
     *
     *  1 Crea una nuova lista di oggetti metriche
     *  2 Creo un nuovo oggetto per l'anno corrente e inizializzo i campi
     *  3 Aggiungo l'oggetto creato alla lista
     *  4 Imposto il campo "yearlyVolumes" alla lista creata e avvalorata
     */
    private void createCurrentYearMetrics(CitizenWasteMetrics citizenWasteMetrics) {

        // 1
        List<GeneratedVolumePerYear> yearlyVolumes = new ArrayList<>();

        // 2
        GeneratedVolumePerYear generatedVolumeCurrentYear = new GeneratedVolumePerYear();
        generatedVolumeCurrentYear.setYear(Year.now().getValue());
        generatedVolumeCurrentYear.setMixedWaste(new Decimal128(0));
        generatedVolumeCurrentYear.setSortedWaste(new HashMap<>());

        // 3
        yearlyVolumes.add(generatedVolumeCurrentYear);

        // 4
        citizenWasteMetrics.setYearlyVolumes(yearlyVolumes);
    }


    /** FUNZIONE PER RICERCARE LE METRICHE PER ID CITTADINO
     *
     * 1 Controllo se esiste una metrica associata con un certo id cittadino:
     *      1.1 Se è stata trovata, ritorniamo l'oggetto avvalorato
     *      1.2 Se non è stata trovata, si crea e si inizializza un nuovo oggetto per l'anno corrente
     */
    private CitizenWasteMetrics getWasteMetricByCitizenID(String citizenID) {

        // 1
        CitizenWasteMetrics citizenWasteMetrics= null;
        Optional<CitizenWasteMetrics> optCitizenWasteMetric = citizenWasteMetricsRepository.findByCitizenID(citizenID);

        // 1.1
        if(optCitizenWasteMetric.isPresent()) {
            citizenWasteMetrics = optCitizenWasteMetric.get();
        }
        // 1.2
        else {
            citizenWasteMetrics = new CitizenWasteMetrics();
            citizenWasteMetrics.setCitizenID(citizenID);

            createCurrentYearMetrics(citizenWasteMetrics);

        }

        return citizenWasteMetrics;
    }


    private Optional<GeneratedVolumePerYear> searchMetricsByCurrentYear(List<GeneratedVolumePerYear> yearlyVolumes) {

        // 1. Ricerca per anno corrente
        Optional<GeneratedVolumePerYear> currentYearMetric = yearlyVolumes.stream()
                .filter(yearMetric -> yearMetric.getYear() == Year.now().getValue())
                .findFirst();

        return currentYearMetric;
    }




    /** FUNZIONE PER AGGIORNARE IL CAMPO "mixedWaste" di "CitizenWasteMetrics"
     *
     *  1. Ricerca dell'oggetto metriche relative all'anno corrente nella list "yearlyVolumes":
     *          1.1 Se è presente, aggiorno il campo "mixedWaste" sommandoci al valore precedente quello del conferimento
     *          1.2 Se non è presente, creo un nuovo oggetto GeneratedVolumePerYear settando:
     *              "year=YearCurrent", "mixedWaste=amountConferimento" e "sortedWaste=mapVuota"
     */
    private void updateMixedWaste(CitizenWasteMetrics citizenWasteMetrics, BigDecimal amountToAdd) {

        // 1
        Optional<GeneratedVolumePerYear> optCurrentYearMetric = searchMetricsByCurrentYear(citizenWasteMetrics.getYearlyVolumes());

        // 1.1
        if(optCurrentYearMetric.isPresent()) {

            GeneratedVolumePerYear currentYearMetric = optCurrentYearMetric.get();

            BigDecimal oldMixedWasteValue = currentYearMetric.getMixedWaste().bigDecimalValue();
            BigDecimal newMixedWasteValue = oldMixedWasteValue.add(amountToAdd);

            currentYearMetric.setMixedWaste(new Decimal128(newMixedWasteValue));

            int index =citizenWasteMetrics.getYearlyVolumes().indexOf(currentYearMetric);
            citizenWasteMetrics.getYearlyVolumes().set(index, currentYearMetric);

        // 1.2
        }else {

            GeneratedVolumePerYear newYearMetric = new GeneratedVolumePerYear();

            newYearMetric.setYear(Year.now().getValue());
            newYearMetric.setMixedWaste(new Decimal128(amountToAdd));
            newYearMetric.setSortedWaste(new HashMap<>());

            citizenWasteMetrics.getYearlyVolumes().add(newYearMetric);
        }
    }

    /** FUNZIONE PER AGGIORNARE IL CAMPO "sortedWaste" di "CitizenWasteMetrics"
     *  1. Ricerca dell'oggetto metriche relative all'anno corrente nella list "yearlyVolumes":
     *          1.1 Se è presente, controlliamo se il type indicato esiste nella map di sortedWaste:
     *              1.1.1 Se esiste, aggiorniamo il campo sommandoci al valore precedente quello del conferimento
     *              1.1.2 Se non esiste, aggiungiamo un campo con type e amount come specificato
     *          1.2 Se non è presente, creo un nuovo oggetto GeneratedVolumePerYear settando:
     *              "year=YearCurrent", "mixedWaste=0" e "sortedWaste=map<type,amount>"
     */

    private void updateSortedWaste(CitizenWasteMetrics citizenWasteMetrics, BigDecimal amountToAdd, String type) {

        // 1
        Optional<GeneratedVolumePerYear> optCurrentYearMetric = searchMetricsByCurrentYear(citizenWasteMetrics.getYearlyVolumes());

        // 1.1
        if(optCurrentYearMetric.isPresent()) {

            GeneratedVolumePerYear currentYearMetric = optCurrentYearMetric.get();

            // 1.1.1
            if(currentYearMetric.getSortedWaste().containsKey(type)) {

                BigDecimal oldValue = currentYearMetric.getSortedWaste().get(type).bigDecimalValue();
                BigDecimal newValue = oldValue.add(amountToAdd);

                currentYearMetric.getSortedWaste().put(type, new Decimal128(newValue));

            // 1.1.2
            }else {
                currentYearMetric.getSortedWaste().put(type,new Decimal128(amountToAdd));
            }

            int index =citizenWasteMetrics.getYearlyVolumes().indexOf(currentYearMetric);
            citizenWasteMetrics.getYearlyVolumes().set(index, currentYearMetric);


        // 1.2
        }else {

            GeneratedVolumePerYear newYearMetric = new GeneratedVolumePerYear();

            newYearMetric.setYear(Year.now().getValue());
            newYearMetric.setMixedWaste(new Decimal128(0));
            newYearMetric.setSortedWaste(new HashMap<>());

            newYearMetric.getSortedWaste().put(type,new Decimal128(amountToAdd));

            citizenWasteMetrics.getYearlyVolumes().add(newYearMetric);

        }
    }


    public List<CitizenWasteMetrics> findAllMetrics() {

        return citizenWasteMetricsRepository.findAll();
    }


    public List<CitizenWasteMetrics> findAllMetricsByYear(int year) {
        return citizenWasteMetricsRepository.findWasteMetricsAndVolumesByYear(year);
    }



    public CitizenWasteMetrics findMetricsById(String citizenID)  {

        CitizenWasteMetrics citizenWasteMetrics = null;


        Optional<CitizenWasteMetrics> optCitizenWasteMetrics = citizenWasteMetricsRepository.findByCitizenID(citizenID);
        if(!optCitizenWasteMetrics.isPresent())
            return null;

        citizenWasteMetrics = optCitizenWasteMetrics.get();

        return citizenWasteMetrics;
    }

    /** FUNZIONE PER IL CALCOLO DELLE PERFORMANCE DI UN CITTADINO RELATIVE A UN CERTO ANNO
     * 1 Find by Id cittadino dell'oggetto relativo di metrica
     * 2 Selezione della metrica relativa all'anno indicato
     * 3 Se l'anno indicato non è stato trovato restituiamo null in quanto non ci sono dati per calcolare la performance
     * 4 Calcolo il totale sommando il campo mixedWaste e tutte le key(tipi) presenti in sortedWaste, e il valore di
     *   sortedWaste come differenza tra il totale e Mixed
     * 5 Calcolo la performance, considerando i due casi estremi:
     *      5.1 Se mixedWaste è 0 e la sortedWaste non è 0, la performance è del 100%
     *      5.2 Altrimenti se sortedWaste è 0 e mixedWaste non è 0, la performance è dello 0%
     *      5.3 Altrimenti è pari a mixedWaste/totalWaste e formatto la percentuale in modo corretto
     *          (2 cifre decimali massimo)
     *
     * @param citizenID
     * @return
     * @throws CitizenNotFoundException
     */


    public Float calculateCitizenPerformance(String citizenID, int targetYear)  {

        // 1
        CitizenWasteMetrics citizenWasteMetrics = findMetricsById(citizenID);
        if(citizenWasteMetrics == null)
            return null;

        // 2
        GeneratedVolumePerYear targetYearMetric = null;

        for (GeneratedVolumePerYear yearMetric: citizenWasteMetrics.getYearlyVolumes()) {
            if(yearMetric.getYear() == targetYear) {
                targetYearMetric = yearMetric;
                break;
            }
        }

        // 3
        if(targetYearMetric == null) {
            return null;
        }

        // 4
        BigDecimal totalWaste = calculateTotalWaste(targetYearMetric);
        BigDecimal mixedWaste = targetYearMetric.getMixedWaste().bigDecimalValue();
        BigDecimal sortedWaste = totalWaste.subtract(mixedWaste);

        // 5
        Float performance = 0.00f;

        // 5.1
        if(mixedWaste.equals(BigDecimal.ZERO) && !sortedWaste.equals(BigDecimal.ZERO)) {
            performance = 1.00f;
        // 5.2
        } else if(sortedWaste.equals(BigDecimal.valueOf(0.0)) && !mixedWaste.equals(BigDecimal.ZERO)) {
            performance = 0.00f;
        // 5.3
        } else {
            performance = mixedWaste.divide(totalWaste, 2, RoundingMode.UP).floatValue();
        }

        return performance;

    }



    private BigDecimal calculateTotalWaste(GeneratedVolumePerYear yearMetric) {
        BigDecimal mixedWaste = yearMetric.getMixedWaste().bigDecimalValue();
        BigDecimal sortedWaste = BigDecimal.ZERO;

        for (Decimal128 sortedValue : yearMetric.getSortedWaste().values()) {
            sortedWaste = sortedWaste.add(sortedValue.bigDecimalValue());
        }

        return mixedWaste.add(sortedWaste);
    }

}
