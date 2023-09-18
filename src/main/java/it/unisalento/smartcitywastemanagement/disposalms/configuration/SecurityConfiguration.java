package it.unisalento.smartcitywastemanagement.disposalms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration  {


    @Bean
    public void configureGlobal(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/api/disposal/metrics/*").permitAll() // Consentire l'accesso a /public/ e suoi sottopercorsi
                .anyRequest().authenticated();// Richiedi l'autenticazione per tutte le altre richiest
    }
}

