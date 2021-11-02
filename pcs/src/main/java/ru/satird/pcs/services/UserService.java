package ru.satird.pcs.services;

import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.payload.request.SignupRequest;

import java.util.List;

public interface UserService {

    User findByUsername(String s);

    void createVerificationTokenForUser(User user, String token);

    void saveRegisteredUser(User user);

    VerificationToken getVerificationToken(String token);

    User getUser(String verificationToken);

    VerificationToken generateNewVerificationToken(String existingToken);

    List<User> showAllUser();

    void saveUser(User user);

    User updateUser(Long id, String name);

    User findUserByEmail(String email);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    void changeUserPassword(User user, String password);

    User findUserById(Long senderId);

    Boolean existsUserByUsername(String username);

    Boolean existsUserByEmail(String email);

    User createNewUser(SignupRequest signUpRequest);
}
