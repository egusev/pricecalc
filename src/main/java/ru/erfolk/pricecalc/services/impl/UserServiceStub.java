package ru.erfolk.pricecalc.services.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.erfolk.pricecalc.actors.Actor;
import ru.erfolk.pricecalc.entities.User;
import ru.erfolk.pricecalc.services.UserService;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by eugene on 20.02.17.
 */
@Service
public class UserServiceStub implements UserService {

    private static final User[] USERS = {
            new User("user1", "password", 10),
            new User("user2", "password", 20),
            new User("user3", "password", 50),
            new User("user4", "password", 10000),
    };

    private static final Map<String, Actor> USER_MAP = Arrays.stream(USERS).collect(Collectors.toMap(User::getUsername, Actor::new));

    @Override
    public Actor loadUserByUsername(String s) throws UsernameNotFoundException {
        return USER_MAP.get(s);
    }
}
