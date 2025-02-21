package com.epicode.Progetto21.controller;

import com.epicode.Progetto21.entities.User;
import com.epicode.Progetto21.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        userRepository.save(user);
        return ResponseEntity.ok("Utente registrato con successo!");
    }



    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login effettuato!");
    }
}
