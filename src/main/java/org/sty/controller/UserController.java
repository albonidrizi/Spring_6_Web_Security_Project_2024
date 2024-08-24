package org.sty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sty.config.Users;
import org.sty.service.UserService;

@RestController // Kjo tregon që kjo klasë do të trajtojë kërkesat HTTP dhe do të kthejë përgjigje në formatin JSON.
public class UserController {

    @Autowired
    public UserService service; // Shërbimi i përdoruesit për logjikën e biznesit të përdoruesve.

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11); // Përdorimi i BCrypt për të koduar fjalëkalimet me një forcë të specifikuar (11).

    @PostMapping("/register") // Kjo metodë do të trajtojë kërkesat POST në endpoint-in /register.
    public Users registerUser(@RequestBody Users user) {
        user.setPassword(encoder.encode(user.getPassword())); // Kodifikon fjalëkalimin e përdoruesit.
        return service.registerUser(user); // Ruaj përdoruesin në bazën e të dhënave.
    }

    @PostMapping("/login") // Kjo metodë do të trajtojë kërkesat POST në endpoint-in /login.
    public String login(@RequestBody Users users){
        return service.verify(users); // Verifikon përdoruesin dhe kthen një JWT nëse autentikimi është i suksesshëm.
    }
}
