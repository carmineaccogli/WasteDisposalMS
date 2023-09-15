package it.unisalento.smartcitywastemanagement.disposalms.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.SmartBinUpdateMessage;
import it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages.WasteDisposalResultMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQ_ConfirmDisposalProducer_Impl implements RabbitMQ_ConfirmDisposalProducer{

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routingKey.confirmRouting}")
    private String confirmRoutingKey;



    @Value("${rabbitmq.routingKey.smartBinUpdate}")
    private String smartBinUpdateRoutingKey;


    @Value("${rabbitmq.exchange.directType}")
    private String directExchange;

    /** RABBITMQ PRODUCER PER LA CONFERMA AL CLIENTNODE DELL'AVVENUTO CONFERIMENTO
     *
     * Vedere info funzione sotto
     * @param jsonMessage
     */

    public void produceResultDisposal(WasteDisposalResultMessage jsonMessage) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 1
            String messageString = objectMapper.writeValueAsString(jsonMessage);

            // 2
            MessageProperties properties = new MessageProperties();
            properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // 3
            Message message = new Message(messageString.getBytes(),properties);

            // 4
            rabbitTemplate.convertAndSend(directExchange, confirmRoutingKey, message);

        }catch (JsonProcessingException e) {
            System.out.println("ERRORE DURANTE L'INVIO DELLA CONFERMA");
        }
    }


    /** RABBITMQ PRODUCER PER LA COMUNICAZIONE CON SMARTBIN_MS
     *
     * 1 Conversione da oggetto JSON a stringa
     * 2 Abilitazione della persistenza dei messaggi che verranno salvati su disco in caso di down o riavvio di rabbitMQ
     *   (in modo che smartBinMS non perda nessun aggiornamento)
     * 3 Creazione del messaggio con la proprietà di persistenza settata
     * 4 Invio del messaggio alla coda apposita
     *
     * @param jsonMessage
     */


    public void produceSmartBinUpdate(SmartBinUpdateMessage jsonMessage) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 1
            String messageString = objectMapper.writeValueAsString(jsonMessage);

            // 2
            MessageProperties properties = new MessageProperties();
            properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            // 3
            Message message = new Message(messageString.getBytes(),properties);

            // 4
            rabbitTemplate.convertAndSend(directExchange, smartBinUpdateRoutingKey, message);

        }catch (JsonProcessingException e) {
            System.out.println("ERRORE DURANTE L'INVIO DELL'AGGIORNAMENTO A SMARTBIN");
        }

    }


}
