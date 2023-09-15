package it.unisalento.smartcitywastemanagement.disposalms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class APIServiceImpl implements APIService{

    @Autowired
    WebClient validateTokenWebClient;

    public Mono<String> APICALL_validateToken(String citizenToken) {

        return validateTokenWebClient.get()
                .uri("/get_citizen_id/{citizenToken}", citizenToken)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    if (error.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.just("");
                    } else {
                        throw error;
                    }
                });
    }
}
