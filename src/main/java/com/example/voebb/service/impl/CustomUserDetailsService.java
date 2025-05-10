package com.example.voebb.service.impl;

import com.example.voebb.model.dto.user.UserRegistrationDTO;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.CustomUserRole;
import com.example.voebb.repository.CustomUserRepo;
import com.example.voebb.repository.CustomUserRoleRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepo userRepo;
    private final CustomUserRoleRepo customUserRoleRepo;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepo customUserRepo;

    public CustomUserDetailsService(CustomUserRepo userRepo, CustomUserRoleRepo customUserRoleRepo, PasswordEncoder passwordEncoder, CustomUserRepo customUserRepo) {
        this.userRepo = userRepo;
        this.customUserRoleRepo = customUserRoleRepo;
        this.passwordEncoder = passwordEncoder;
        this.customUserRepo = customUserRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> authorities = customUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new User(
                customUser.getEmail(),
                customUser.getPassword(),
                customUser.isEnabled(),
                true, true, true,
                authorities
        );
    }

    public void createUser(UserRegistrationDTO userRegistrationDTO) {

        CustomUserRole role = customUserRoleRepo.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_CLIENT not found"));

        CustomUser customUser = new CustomUser();

        customUser.setEmail(userRegistrationDTO.email());
        customUser.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        customUser.setFirstName(userRegistrationDTO.firstName());
        customUser.setLastName(userRegistrationDTO.lastName());
        customUser.setEnabled(true);
        customUser.setRoles(Set.of(role));

        customUserRepo.save(customUser);
    }
}
