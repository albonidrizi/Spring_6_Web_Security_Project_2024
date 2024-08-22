package org.sty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sty.config.Users;
import org.sty.config.v2_UserRepo;

@Service
public class UserService {

    @Autowired
    v2_UserRepo repo;

    public Users registerUser(Users user) {
        return repo.save(user);
    }
}
