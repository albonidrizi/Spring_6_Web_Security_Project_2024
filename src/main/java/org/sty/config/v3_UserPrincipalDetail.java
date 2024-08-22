package org.sty.config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class v3_UserPrincipalDetail implements UserDetails {

    // Përdoruesi që përmban detajet e autentikimit
    private Users users;

    // Konstruktori që pranon një objekt Users
    public v3_UserPrincipalDetail(Users users) {
        this.users = users;
    }

    // Kthen rolet/autoritetet e përdoruesit
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER")); // Kthen një autoritet 'USER'
    }

    // Kthen fjalëkalimin e përdoruesit
    @Override
    public String getPassword() {
        return users.getPassword();
    }

    // Kthen emrin e përdoruesit
    @Override
    public String getUsername() {
        return users.getUsername();
    }

    // Metoda të tjera të implementuara për UserDetails, që mund të përfshijnë vlefshmërinë e llogarisë, mbylljen, skadimin etj.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
