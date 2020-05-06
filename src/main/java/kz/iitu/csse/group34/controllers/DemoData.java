package kz.iitu.csse.group34.controllers;

import kz.iitu.csse.group34.entities.Roles;
import kz.iitu.csse.group34.entities.Users;
import kz.iitu.csse.group34.repositories.RolesRepository;
import kz.iitu.csse.group34.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DemoData {

    @Autowired
    private RolesRepository repo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Roles admin = new Roles(1L,"ROLE_ADMIN");
        Roles user = new Roles(2L,"ROLE_USER");
        repo.save(admin);
        repo.save(user);
        Set<Roles> set = new HashSet<>();
        set.add(admin);
        }
}