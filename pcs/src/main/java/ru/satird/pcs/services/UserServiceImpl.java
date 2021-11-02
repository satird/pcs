package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.ERole;
import ru.satird.pcs.domains.Role;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.errors.UserNotFoundException;
import ru.satird.pcs.payload.request.SignupRequest;
import ru.satird.pcs.repositories.UserRepository;
import ru.satird.pcs.repositories.VerificationTokenRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_NOT_FOUND = "Error: Role is not found.";
    private UserRepository userRepository;
    private VerificationTokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenRepository(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String s) {
        return userRepository.findByName(s).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + s));
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User getUser(String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public List<User> showAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, String name) {
        final Optional<User> changedUser = userRepository.findById(id);
        if (changedUser.isPresent()) {
            changedUser.get().setName(name);
            return userRepository.save(changedUser.get());
        } else {
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User findUserById(Long senderId) {
        return userRepository.findById(senderId).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + senderId + " не найден"));
    }

    @Override
    public Boolean existsUserByUsername(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User createNewUser(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRating(5f);
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleService.findRoleByName(ERole.ROLE_USER)
                    .orElseThrow(RuntimeException::new);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findRoleByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleService.findRoleByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleService.findRoleByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return user;
    }
}
