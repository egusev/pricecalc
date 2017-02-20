package ru.erfolk.pricecalc.actors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.erfolk.pricecalc.entities.User;

import java.util.Collection;
import java.util.Collections;

@Slf4j
public class Actor implements UserDetails {

    @Getter
    private User user;

    private long lastRequest = 0;

    private int requests;

    public Actor(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

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

    public synchronized boolean canProcess() {
        long current = System.currentTimeMillis();
        if (lastRequest / 1000 != current / 1000) {
            log.debug("Reset the request count");

            resetCounter();
            lastRequest = current;
        }

        return ++requests <= user.getRequestPerSecond();
    }

    public synchronized void resetCounter() {
        requests = 0;
    }
}
