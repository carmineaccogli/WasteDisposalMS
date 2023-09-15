package it.unisalento.smartcitywastemanagement.disposalms.service;

import reactor.core.publisher.Mono;

public interface APIService {

    Mono<String> APICALL_validateToken(String citizenToken);
}
