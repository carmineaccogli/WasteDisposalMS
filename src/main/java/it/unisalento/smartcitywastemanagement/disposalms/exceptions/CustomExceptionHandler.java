package it.unisalento.smartcitywastemanagement.disposalms.exceptions;
import it.unisalento.smartcitywastemanagement.disposalms.dto.ExceptionDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Set;

/**
 *  Quando una eccezione viene sollevata, questa classe si occupa di restituire nel body
 *  un json contenente le informazioni sull'eccezione.
 *  Informazioni eccezione:
 *      - codice eccezione (fa riferimento ad un elenco di eccezioni già stilato su un documento excel)
 *      - nome eccezione (nome della Classe Java relativa all'eccezione)
 *      - descrizione eccezione
 */
@ControllerAdvice
public class CustomExceptionHandler  {


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> webClientResponseHandler(WebClientResponseException ex) {

        HttpStatusCode httpStatus = ex.getStatusCode();
        String responseBody = ex.getResponseBodyAsString();

        return ResponseEntity.status(httpStatus)
                .body(new ExceptionDTO(
                        23,
                        WebClientResponseException.class.getSimpleName(),
                        responseBody
                ));
    }

    @ExceptionHandler(CitizenNotFoundException.class)
    public ResponseEntity<Object> citizenNotFoundHandler(CitizenNotFoundException ex) {


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        7,
                        CitizenNotFoundException.class.getSimpleName(),
                        "Citizen ID not found in waste metrics"
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationHandler(
            ConstraintViolationException ex) {

        StringBuilder errorString = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            String message = violation.getMessage();
            String property = violation.getPropertyPath().toString();
            errorString.append(property).append(":").append(message).append(". ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        30,
                        ConstraintViolationException.class.getSimpleName(),
                        errorString.deleteCharAt(errorString.length() - 2).toString()
                ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFoundHandler(NoHandlerFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getBody());
    }







}

