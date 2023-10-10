package it.unisalento.smartcitywastemanagement.disposalms.service;


import it.unisalento.smartcitywastemanagement.disposalms.security.JwtUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class APIServiceImpl implements APIService{

    @Autowired
    WebClient validateTokenWebClient;

    @Autowired
    private JwtUtilities jwtUtilities;

    public Mono<String> APICALL_validateToken(String citizenToken) {

        final String jwtToken = jwtUtilities.generateToken();

        return validateTokenWebClient.get()
                .uri("/get_citizen_id/{citizenToken}", citizenToken)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    if (error.getStatusCode() !=HttpStatus.OK) {
                        return Mono.just("");
                    } else {
                        throw error;
                    }
                });
    }
}
