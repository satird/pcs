package ru.satird.pcs.services;

import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.dto.UserDto;
import ru.satird.pcs.dto.UserInfoDto;
import ru.satird.pcs.dto.payload.request.SignupRequest;

import java.util.List;

public interface UserService {

    User findByUsername(String s);
    void createVerificationTokenForUser(User user, String token);
    void saveRegisteredUser(User user);
    VerificationToken getVerificationToken(String token);
    List<UserDto> showAllUser();
    void saveUser(User user);
    UserInfoDto updateUser(Long id, UserDto userDto);
    User findUserByEmail(String email);
    boolean checkIfValidOldPassword(User user, String oldPassword);
    void changeUserPassword(User user, String password);
    User findUserById(Long senderId);
    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
    User createNewUser(SignupRequest signUpRequest);
    UserInfoDto findUserByIdAndConvertToUserInfoDto(Long id);
}
