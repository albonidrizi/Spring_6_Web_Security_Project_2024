package org.sty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sty.config.Users;
import org.sty.service.UserService;

@RestController
public class UserController {

    @Autowired
    public UserService service;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);


    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return service.registerUser(user);
    }



}
