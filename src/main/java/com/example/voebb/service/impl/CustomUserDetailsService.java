package com.example.voebb.service.impl;

import com.example.voebb.exception.EmailAlreadyUsedException;
import com.example.voebb.exception.UserNotFoundException;
import com.example.voebb.model.dto.user.UserDTO;
import com.example.voebb.model.dto.user.UserRegistrationDTO;
import com.example.voebb.model.dto.user.UserUpdateDTO;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.CustomUserRole;
import com.example.voebb.repository.CustomUserRepo;
import com.example.voebb.repository.CustomUserRoleRepo;
import com.example.voebb.service.CustomUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, CustomUserService {

    private final CustomUserRepo userRepo;
    private final CustomUserRoleRepo customUserRoleRepo;
    private final PasswordEncoder passwordEncoder;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser;

        if (isValidEmail(username)) {
            customUser = userRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + username));
        } else if (isValidPhone(username)) {
            String normalizedPhone = normalizePhone(username);
            customUser = userRepo.findByPhoneNumber(normalizedPhone)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone number not found: " + normalizedPhone));
        } else {
            throw new UsernameNotFoundException("Invalid login identifier: " + username);
        }

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

    private boolean isValidPhone(String username) {
        return username.matches("^\\+49\\d{10,11}$");
    }

    private boolean isValidEmail(String username) {
        return username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    private String normalizePhone(String input) {
        // Input: +4917612345678 -> Output: +49-176-12345678
        String digits = input.replace("+49", "");
        String providerCode = digits.substring(0, 3);
        String subscriberNumber = digits.substring(3);
        return "+49-" + providerCode + "-" + subscriberNumber;
    }
    @Transactional
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepo.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepo.findByPhoneNumber(normalizePhone(userRegistrationDTO.getPhoneNumber())).isPresent()) {
            throw new EmailAlreadyUsedException("Phone number already in use");
        }
        CustomUserRole role = customUserRoleRepo.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_CLIENT not found"));

        CustomUser customUser = new CustomUser();

        customUser.setEmail(userRegistrationDTO.getEmail());
        customUser.setPhoneNumber(normalizePhone(userRegistrationDTO.getPhoneNumber()));
        customUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        customUser.setFirstName(userRegistrationDTO.getFirstName());
        customUser.setLastName(userRegistrationDTO.getLastName());
        customUser.setEnabled(true);
        customUser.setRoles(Set.of(role));

        userRepo.save(customUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserUpdateDTO getUserDTOByUsername(String username) {
        CustomUser user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return toUpdateDto(user);
    }

    @Override
    public void updateUserInfo(UserUpdateDTO userDto,
                               String oldEmail,
                               HttpServletRequest request,
                               HttpServletResponse response){
        boolean loginCredentialsChange = false;
        CustomUser existingUser = userRepo.findByEmail(oldEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + oldEmail + " not found"));

        if(!passwordEncoder.matches(userDto.getOldPassword(), existingUser.getPassword())){
            throw new RuntimeException("Passwords are not matching");
        }

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());

        if (!userDto.getEmail().equals(oldEmail)) {
            existingUser.setEmail(userDto.getEmail());
            loginCredentialsChange = true;
        }

        String normalizedPhone = normalizePhone(userDto.getPhoneNumber());
        if (!normalizedPhone.equals(existingUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(normalizedPhone);
            loginCredentialsChange = true;
        }

        userRepo.save(existingUser);

        if(loginCredentialsChange){
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Override
    public UserDTO getUserById(Long id) {
        CustomUser user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return toDto(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDto) {
        CustomUser user = toEntity(userDto);
        CustomUser saved = userRepo.save(user);
        return toDto(saved);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDto) {
        CustomUser existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        Set<CustomUserRole> userRoles = userDto.roleIds().stream()
                .map(roleId -> customUserRoleRepo.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());

        existingUser.setFirstName(userDto.firstName());
        existingUser.setLastName(userDto.lastName());
        existingUser.setEmail(userDto.email());
        existingUser.setPhoneNumber(normalizePhone(userDto.phoneNumber()));
        existingUser.setEnabled(userDto.enabled());
        existingUser.setBorrowedProductsCount(userDto.borrowedBooksCount());

        if (userDto.password() != null && !userDto.password().isBlank()) {
            existingUser.setPassword(userDto.password());
        }

        existingUser.setRoles(userRoles);

        CustomUser updatedUser = userRepo.save(existingUser);
        return toDto(updatedUser);
    }

    @Override
    public void enableUser(Long id) {
        CustomUser user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setEnabled(true);
        userRepo.save(user);
    }

    @Override
    public void disableUser(Long id) {
        CustomUser user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setEnabled(false);
        userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    // Mapping methods
    private UserDTO toDto(CustomUser user) {
        List<Long> roleIds = user.getRoles().stream()
                .map(CustomUserRole::getId)
                .toList();

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                null, // Do not expose password
                user.getFirstName(),
                user.getLastName(),
                user.isEnabled(),
                user.getBorrowedProductsCount(),
                roleIds
        );
    }

    private CustomUser toEntity(UserDTO dto) {
        Set<CustomUserRole> userRoles = dto.roleIds().stream()
                .map(roleId -> customUserRoleRepo.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());

        CustomUser user = new CustomUser();
        user.setId(dto.id()); // Optional: depending on whether you're creating or updating
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(normalizePhone(dto.phoneNumber()));
        user.setEnabled(dto.enabled());
        user.setBorrowedProductsCount(dto.borrowedBooksCount());
        user.setPassword(dto.password());
        user.setRoles(userRoles);
        return user;
    }


    private UserUpdateDTO toUpdateDto(CustomUser user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
