package ru.satird.pcs.mapper;

import org.mapstruct.Mapper;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.UserDto;
import ru.satird.pcs.dto.UserInfoDto;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDto mapUserDto(User user);
    User mapUser(UserDto userDto);
    List<User> mapUserList(List<UserDto> userDtoList);
    List<UserDto> mapUserDtoList(List<User> userList);
    UserInfoDto mapUserInfoDto(User user);
    User mapFromUserInfoDtoToUser(UserInfoDto userInfoDto);
}
