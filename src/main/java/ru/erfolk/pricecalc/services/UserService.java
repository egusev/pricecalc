package ru.erfolk.pricecalc.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.erfolk.pricecalc.actors.Actor;

/**
 * Created by eugene on 20.02.17.
 */
public interface UserService extends UserDetailsService {
    Actor loadUserByUsername(String var1) throws UsernameNotFoundException;
}
