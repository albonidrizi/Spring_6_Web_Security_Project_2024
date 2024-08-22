package org.sty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
@Configuration // Tregon që kjo klasë është një klasë konfigurimi për Spring.
@EnableWebSecurity // Aktivizon sigurinë e bazuar në Spring Security për aplikacionin.
public class v5_SecurityConfig {

    // Shërbimi që menaxhon ngarkimin e detajeve të përdoruesit
    @Autowired
    private UserDetailsService userDetailsService;

    // Ky bean konfiguron filtrin e sigurisë për aplikacionin
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Konfigurimi i HttpSecurity për të menaxhuar autorizimin dhe autentikimin
        return http
                .csrf(customizer -> customizer.disable()) // Çaktivizo CSRF (jo e nevojshme për API-të stateless)
                .authorizeHttpRequests(request -> request.anyRequest().authenticated()) // Çdo kërkesë duhet të jetë e autentikuar
                .httpBasic(Customizer.withDefaults()) // Përdor autentikim bazik
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Menaxho sesionet si stateless (pa ruajtje të sesioneve)
                .build(); // Ndërto dhe kthe filtrin e sigurisë

        // Mund të shtosh form login nëse dëshiron t'i ofrosh një formë hyrje:
        // http.formLogin(Customizer.withDefaults());
    }

    // Ky bean krijon një AuthenticationProvider që përdor DAO për autentikim
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Përdor një kriptim fjalëkalimi pa veprim (jo e sigurt për prodhim)
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));//Perdorem BCryptPasswordEncoder per te dekriptuar passwordin
        provider.setUserDetailsService(userDetailsService); // Lidhet me UserDetailsService për të ngarkuar detajet e përdoruesit
        return provider; // Kthen AuthenticationProvider
    }

    // Nëse dëshiron të përdorësh përdorues në memorie për testim, mund të shërben këtë bean:
    // @Bean
    // public UserDetailsService userDetailsService(){
    //     UserDetails user1  = User.withDefaultPasswordEncoder()
    //             .username("albon")
    //             .password("121212")
    //             .roles("ADMIN").build();
    //
    //     UserDetails user2  = User.withDefaultPasswordEncoder()
    //             .username("ana")
    //             .password("121212")
    //             .roles("USER").build();
    //
    //     return new InMemoryUserDetailsManager(user1, user2);
    // }
}