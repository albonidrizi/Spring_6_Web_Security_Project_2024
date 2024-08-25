package org.sty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Kjo annotacion tregon që kjo klasë është një klasë konfigurimi për Spring.
@EnableWebSecurity // Aktivizon sigurinë e bazuar në Spring Security për aplikacionin.
public class v5_SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtFilter jwtFilter;

    @Autowired
    public v5_SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }
    // Ky bean krijon një SecurityFilterChain që konfiguron filtrin e sigurisë për aplikacionin.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Konfigurimi i HttpSecurity për të menaxhuar autorizimin dhe autentikimin.
        return http
                .csrf(AbstractHttpConfigurer::disable) // Çaktivizo CSRF (jo e nevojshme për API-të stateless, si REST).
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register", "/login") // Lejo kërkesat në endpoint-et /register dhe /login pa autentikim.
                        .permitAll()
                        .anyRequest().authenticated()) // Kërkesat për çdo endpoint tjetër duhet të jenë të autentikuara.
                .httpBasic(Customizer.withDefaults()) // Përdor autentikimin bazik (për kërkesat që përdorin username dhe fjalëkalim).
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Menaxho sesionet si stateless (pa ruajtje të sesioneve).
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // Ndërto dhe kthe filtrin e sigurisë.

        // Mund të shtosh form login nëse dëshiron t'i ofrosh një formë hyrje:
        // http.formLogin(Customizer.withDefaults());
    }

    // Ky bean krijon një AuthenticationProvider që përdor DAO për autentikim.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Përdor BCryptPasswordEncoder për të koduar dhe verifikuar fjalëkalimet e përdoruesve.
        provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
        provider.setUserDetailsService(userDetailsService); // Lidhet me UserDetailsService për të ngarkuar detajet e përdoruesit.
        return provider; // Kthen AuthenticationProvider.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Kthen një AuthenticationManager nga konfigurimi.
    }

    // Nëse dëshiron të përdorësh përdorues në memorie për testim, mund të shërbeni këtë bean:
    // @Bean
    // public UserDetailsService userDetailsService(){
    //     UserDetails user1 = User.withDefaultPasswordEncoder()
    //             .username("albon")
    //             .password("121212")
    //             .roles("ADMIN").build();
    //
    //     UserDetails user2 = User.withDefaultPasswordEncoder()
    //             .username("ana")
    //             .password("121212")
    //             .roles("USER").build();
    //
    //     return new InMemoryUserDetailsManager(user1, user2); // Krijon dhe kthen një menaxher për përdoruesit në memorie.
    // }
}
