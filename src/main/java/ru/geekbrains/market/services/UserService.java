package ru.geekbrains.market.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.geekbrains.market.entities.SystemUser;
import ru.geekbrains.market.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String username);

    boolean save(SystemUser systemUser);

    void deleteById(Long id);

}
