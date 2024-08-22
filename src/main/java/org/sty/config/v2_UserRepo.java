package org.sty.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface v2_UserRepo extends JpaRepository<Users, Integer> {
    // Kjo metodë kthen një objekt Users bazuar në fushën 'username'
    Users findByUsername(String username);
}
