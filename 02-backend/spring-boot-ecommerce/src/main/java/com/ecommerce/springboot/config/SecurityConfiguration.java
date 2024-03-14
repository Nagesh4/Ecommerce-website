package com.ecommerce.springboot.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //protect endpoint /api/orders
        http.authorizeHttpRequests(requests ->
                            requests.requestMatchers("/api/orders/**").authenticated().anyRequest().permitAll())
                            .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        //add cors filters
        http.cors(Customizer.withDefaults());

        //add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        //non-empty response body for 401
        Okta.configureResourceServer401ResponseBody(http);

        //we are not using Cookies for session tracking >> disable CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();

    }
}
