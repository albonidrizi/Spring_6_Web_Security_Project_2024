package org.sty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class v4_MyUserDetailsService implements UserDetailsService {

    // Injektohet repo-ja që përdoret për të marrë të dhënat e përdoruesve nga baza e të dhënave
    private final v2_UserRepo userRepo;

    @Autowired
    public v4_MyUserDetailsService(v2_UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Kjo metodë kërkon përdoruesin në bazën e të dhënave dhe kthen një UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Përdor metodën e repo-s për të gjetur përdoruesin me emrin e dhënë
        Users users = userRepo.findByUsername(username);

        // Kontrollon nëse përdoruesi ekziston; nëse jo, hedh një përjashtim
        if (users == null) {
            System.out.println("User is not Found"); // Shfaq një mesazh në console
            throw new UsernameNotFoundException("Error, user not found"); // Hedh një përjashtim për përdorues të panjohur
        }
        // Kthen një objekt UserPrincipalDetail që përmban detajet e përdoruesit
        return new v3_UserPrincipalDetail(users);
    }
}