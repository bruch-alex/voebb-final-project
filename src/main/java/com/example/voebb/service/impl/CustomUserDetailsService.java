package com.example.voebb.service.impl;

import com.example.voebb.exception.UserNotFoundException;
import com.example.voebb.model.dto.ItemActivityDTO;
import com.example.voebb.model.dto.user.UserDTO;
import com.example.voebb.model.dto.user.UserRegistrationDTO;
import com.example.voebb.model.dto.user.UserUpdateDTO;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.CustomUserRole;
import com.example.voebb.repository.CustomUserRepo;
import com.example.voebb.repository.CustomUserRoleRepo;
import com.example.voebb.service.BorrowService;
import com.example.voebb.service.CustomUserService;
import com.example.voebb.service.ReservationService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, CustomUserService {

    private final CustomUserRepo userRepo;
    private final CustomUserRoleRepo customUserRoleRepo;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepo customUserRepo;
    private final PasswordEncoder encoder;
    private final ReservationService reservationService;
    private final BorrowService borrowService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = getCustomUserByUsername(username);

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
        return username.matches("^\\+[0-9]{10,15}$");
    }

    private boolean isValidEmail(String username) {
        return username.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private CustomUser getCustomUserByUsername(String username) {
        if (isValidEmail(username)) {
            return userRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + username));
        } else if (isValidPhone(username)) {
            return userRepo.findByPhoneNumber(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone number not found: " + username));
        } else {
            throw new UsernameNotFoundException("Invalid login identifier: " + username);
        }
    }

    @Override
    public List<ItemActivityDTO> getItemActivitiesByUsername(String username) {
        CustomUser customUser = getCustomUserByUsername(username);

        List<ItemActivityDTO> activityDTOS = new ArrayList<>(
                reservationService.getFilteredReservations(customUser.getId(), null, null, null).stream()
                        .map(getReservationDTO -> new ItemActivityDTO(
                                getReservationDTO.id(),
                                "Reservation",
                                null,
                                getReservationDTO.itemTitle(),
                                getReservationDTO.itemId(),
                                getReservationDTO.startDate(),
                                getReservationDTO.dueDate()
                        ))
                        .toList());

        activityDTOS.addAll(
                borrowService.getFilteredBorrowings(customUser.getId(), null, null, "Active", null).stream()
                        .map(getBorrowingsDTO -> new ItemActivityDTO(
                                getBorrowingsDTO.borrowId(),
                                "Borrow",
                                getBorrowingsDTO.extendsCount(),
                                getBorrowingsDTO.itemTitle(),
                                getBorrowingsDTO.itemId(),
                                getBorrowingsDTO.startDate(),
                                getBorrowingsDTO.dueDate()
                        ))
                        .toList());

        return activityDTOS;
    }

    @Override
    public Boolean isBorrowingExpiresSoon(String username) {
        CustomUser customUser = getCustomUserByUsername(username);

        return borrowService.getFilteredBorrowings(
                        customUser.getId(), null, null, "Active", null)
                .stream()
                .anyMatch(borrow ->
                        LocalDate.now().plusDays(3).isAfter(borrow.dueDate())
                        || LocalDate.now().plusDays(3).isEqual(borrow.dueDate()));
    }

    @Transactional
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {
        CustomUserRole role = customUserRoleRepo.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_CLIENT not found"));

        CustomUser customUser = new CustomUser();

        customUser.setEmail(userRegistrationDTO.getEmail());
        customUser.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        customUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        customUser.setFirstName(userRegistrationDTO.getFirstName());
        customUser.setLastName(userRegistrationDTO.getLastName());
        customUser.setEnabled(true);
        customUser.setRoles(Set.of(role));

        customUserRepo.save(customUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserUpdateDTO getUserUpdateDTOByUsername(String username) {
        CustomUser user = getCustomUserByUsername(username);
        return toUpdateDto(user);
    }

    @Override
    public UserDTO getUserDTOByUsername(String username) {
        CustomUser user = getCustomUserByUsername(username);
        return toDto(user);
    }

    @Override
    public void updateUserInfo(UserUpdateDTO userDto,
                               String oldEmail,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        boolean loginCredentialsChange = false;
        CustomUser existingUser = userRepo.findByEmail(oldEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + oldEmail + " not found"));

        if (!encoder.matches(userDto.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Passwords are not matching");
        }

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());

        if (!userDto.getEmail().equals(oldEmail)) {
            existingUser.setEmail(userDto.getEmail());
            loginCredentialsChange = true;
        }

        if (!userDto.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(userDto.getPhoneNumber());
            loginCredentialsChange = true;
        }

        if (userDto.getNewPassword() != null && !userDto.getNewPassword().isBlank()) {
            existingUser.setPassword(encoder.encode(userDto.getNewPassword()));
        }

        userRepo.save(existingUser);

        if (loginCredentialsChange) {
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
        existingUser.setPhoneNumber(userDto.phoneNumber());
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
        user.setPhoneNumber(dto.phoneNumber());
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
