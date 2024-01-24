package it.unisalento.smartcitywastemanagement.disposalms.security;

import java.util.List;

public class SecurityConstants {
    public static final String JWT_SECRET = "seedseedseedseedseedseedseedseedseedseedseed"; // sarà utilizzato per l'algoritmo di firma

    public static final String THIS_MICROSERVICE ="http://disposalManagementService:8083";

    public static final String ISSUER ="http://disposalManagementService:8083";

    public static final List<String> AUDIENCE = List.of("http://loginService:8080");

    public static final String SUBJECT = "disposalMS";

    public static final String ROLE = "MICROSERVICE-COMMUNICATION";

}
