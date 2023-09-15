package it.unisalento.smartcitywastemanagement.disposalms;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class DisposalMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisposalMsApplication.class, args);
    }

}
