package it.unisalento.smartcitywastemanagement.disposalms.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import it.unisalento.smartcitywastemanagement.disposalms.domain.WasteDisposal;
import it.unisalento.smartcitywastemanagement.disposalms.exceptions.MessageArgumentNotValidException;
import it.unisalento.smartcitywastemanagement.disposalms.mappers.WasteDisposalMapper;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.SmartBinUpdateMessage;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.WasteDisposalMessage;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.WasteDisposalResultMessage;
import it.unisalento.smartcitywastemanagement.disposalms.repositories.WasteDisposalRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.RetryException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.io.IOException;
import java.util.Set;

@Service
public class RabbitMQ_DisposalListener {

    @Autowired
    APIService apiService;

    @Autowired
    WasteDisposalMapper wasteDisposalMapper;

    @Autowired
    WasteDisposalRepository wasteDisposalRepository;

    @Autowired
    CitizenWasteMetricsService citizenWasteMetricsService;

    @Autowired
    RabbitMQ_ConfirmDisposalProducer rabbitMQConfirmDisposalProducer;

    @Autowired
    private Validator validator;


    private static final Logger logger = LoggerFactory.getLogger(RabbitMQ_DisposalListener.class);


    /** GESTIONE FLUSSO CONFERIMENTI: listener rabbitmq in ascolto sulla coda prevista per i conferimenti inviati
     *
     * 1 Conversione del messaggio da stringa nell'apposita entità "WasteDisposalMessage"
     * 2 Validazione del messaggio in input
     * 3 Autenticazione tramite validazione del token contattando AuthMS
     *      3.1 Check Autenticazione
     *          3.1.1 Autenticazione non riuscita: Invio del messaggio di errore al clientNode sulla coda di conferma
     *          3.1.2 Autenticazione riuscita
     *      3.2 Invio dello smartBinID a smartBinMS per eseguire l'aggiornamento della capacità corrente
     *      3.3 Salvataggio nella collezione "WasteDisposal" del conferimento effettuato
     *      3.4 Comunicazione al clientNode della corretta riuscita dell'operazione sulla coda di conferma
     *      3.5 Aggiornamento delle metriche nella collezione "CitizenWasteMetrics"
     *
     *
     * @param message
     * @throws IOException
     */

    @RabbitListener(queues = {"${rabbitmq.queue.addDisposal}"})
    public void consumeMessage(@Payload String message) throws Exception {

        //1
        ObjectMapper objectMapper = new ObjectMapper();
        WasteDisposalMessage disposalMessage = objectMapper.readValue(message, WasteDisposalMessage.class);

        // 2
        String validationError = isMessageValid(disposalMessage);
        if(validationError != null) {
            throw new AmqpRejectAndDontRequeueException(validationError);
        }

        // TESTING
        logger.info(disposalMessage.getSmartBinID());
        logger.info(disposalMessage.getToken());
        logger.info("AMOUNT {}",disposalMessage.getAmount());
        logger.info("POSITION {}",disposalMessage.getPosition());
        logger.info("DATA {}",disposalMessage.getTimestamp());

        // 3
        String citizenID = apiService.APICALL_validateToken(disposalMessage.getToken()).block();

        // 3.1
        if (citizenID.isEmpty()) {

            // 3.1.1
            rabbitMQConfirmDisposalProducer.produceResultDisposal(new WasteDisposalResultMessage(WasteDisposalResultMessage.Status.ERROR,"Authentication Failed"));
            System.out.println("TOKEN NON TROVATO");

            return;
        }

        // 3.1.2
        // Autenticazione riuscita
        System.out.println("TOKEN TROVATO");


        // 3.2
        try {
            rabbitMQConfirmDisposalProducer.produceSmartBinUpdate(new SmartBinUpdateMessage(disposalMessage.getSmartBinID(), disposalMessage.getAmount()));
        }catch (RetryException e) {
            System.out.println("RABBIT MQ DOWN");
            return;
        }


        // 3.3
        WasteDisposal wasteDisposal = wasteDisposalMapper.toWasteDisposalFromMessage(disposalMessage, citizenID);
        wasteDisposalRepository.save(wasteDisposal);

        // 3.4
        rabbitMQConfirmDisposalProducer.produceResultDisposal(new WasteDisposalResultMessage(WasteDisposalResultMessage.Status.SUCCESS,"Waste disposal successfully done"));

        // 3.5
        citizenWasteMetricsService.updateMetrics(disposalMessage.getType(), disposalMessage.getAmount(), citizenID);
    }

    private String isMessageValid(WasteDisposalMessage wasteDisposalMessage) {

        Set<ConstraintViolation<WasteDisposalMessage>> violations = validator.validate(wasteDisposalMessage);

        StringBuilder errorMessage = new StringBuilder("Errori di validazione: ");
        for (ConstraintViolation<WasteDisposalMessage> violation : violations) {
            errorMessage.append(violation.getMessage()).append(", ");
        }

        errorMessage.setLength(errorMessage.length() - 2);
        String errors = errorMessage.toString();

        if (!violations.isEmpty()) {
            return errors;
        }

        return null;
    }









}
