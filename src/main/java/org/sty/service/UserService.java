package org.sty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.sty.config.Users;
import org.sty.config.v2_UserRepo;

@Service // Kjo tregon që kjo klasë është një shërbim Spring që mund të injektohet në të tjera komponente.
public class UserService {

    @Autowired
    v2_UserRepo repo; // Depoja për ndërveprimin me bazën e të dhënave të përdoruesve.

    @Autowired
    private JWTService jwtService; // Shërbimi për krijimin e JWT-ve.

    @Autowired
    AuthenticationManager authManager; // Menaxheri i autentikimit për të verifikuar kredencialet e përdoruesve.

    public Users registerUser(Users user) {
        return repo.save(user); // Ruaj përdoruesin në bazën e të dhënave.
    }

    public String verify(Users users) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        users.getUsername(), users.getPassword())); // Përpiqet të autentikojë përdoruesin.

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(users.getUsername()); // Nëse autentikimi është i suksesshëm, kthen një JWT.
        }
        return "fail"; // Nëse autentikimi dështon, kthen "fail".
    }
}
