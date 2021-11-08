package ru.satird.pcs.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.AdVisibleDto;
import ru.satird.pcs.dto.UserDto;
import ru.satird.pcs.dto.UserInfoDto;
import ru.satird.pcs.errors.InvalidOldPasswordException;
import ru.satird.pcs.dto.payload.response.MessageResponse;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Api(description = "Контроллер пользователей")
@Slf4j
@RestController
@RequestMapping("/rest/api")
public class UserRestController {

    private final UserService userService;
    private final AdService adService;
    private final MessageSource messages;
    private final Util util;

    @Autowired
    public UserRestController(UserService userService, AdService adService, @Qualifier("messageSource") MessageSource messages, Util util) {
        this.userService = userService;
        this.adService = adService;
        this.messages = messages;
        this.util = util;
    }


    @ApiOperation(value = "Получить всех пользователей", notes = "Получить всех пользователей")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUser() {
        final List<UserDto> users = userService.showAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @ApiOperation(value = "Получить пользователя", notes = "Получить пользователя по его id")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id пользователя", required = true, dataTypeClass = Long.class, paramType = "path", example = "3"),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserInfoDto> getUserById(
            @PathVariable("id") Long id
    ) {
        log.debug("getUserById...");
        final UserInfoDto user = userService.findUserByIdAndConvertToUserInfoDto(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @ApiOperation(value = "Редактирование профиля", notes = "Изменить имя пользователя")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id пользователя", required = true, dataTypeClass = Long.class, paramType = "path", example = "1"),
            @ApiImplicitParam (name = "userDto", value = "Новый логин", required = true, dataTypeClass = UserDto.class, paramType = "body")
    })
    @PutMapping("/user/{id}")
    public ResponseEntity<UserInfoDto> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto
    ) {
        log.debug("updateUser...");
        User currentUser = util.getCurrentUser();
        if (currentUser.getId().equals(id)) {
            final UserInfoDto user = userService.updateUser(id, userDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @ApiOperation(value = "Изменить пароль", notes = "Изменить пароль пользователя")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id пользователя", required = true, dataTypeClass = Long.class, paramType = "path", example = "3"),
            @ApiImplicitParam (name = "password", value = "Новый пароль", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam (name = "oldpassword", value = "Старый пароль", required = true, dataTypeClass = String.class, paramType = "query")
    })
    @PostMapping("/user/{id}/updatePassword")
    public ResponseEntity<MessageResponse> changeUserPassword(
            HttpServletRequest request,
            @PathVariable("id") Long id,
            @RequestParam("password") String password,
            @RequestParam("oldpassword") String oldPassword
    ) {
        log.debug("changeUserPassword...");
        Locale locale = request.getLocale();
        User currentUser = util.getCurrentUser();
        final User user = userService.findUserById(id);
        if (currentUser.equals(user)) {
            if (!userService.checkIfValidOldPassword(user, oldPassword)) {
                log.error("Error method 'changeUserPassword' check the old password");
                throw new InvalidOldPasswordException();
            }
            userService.changeUserPassword(user, password);
            return new ResponseEntity<>(new MessageResponse(messages.getMessage("message.updatePasswordSuc", null, locale)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse(messages.getMessage("message.updatePasswordErr", null, locale)), HttpStatus.BAD_REQUEST);
    }


    @ApiOperation(value = "Получить объявления пользователя", notes = "Получить все объявления пользователя по его id")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id пользователя", required = true, dataTypeClass = String.class, paramType = "path", example = "3"),
    })
    @GetMapping("/user/{id}/ad")
    public ResponseEntity<List<AdVisibleDto>> userAds(
            @PathVariable("id") Long id
    ) {
        log.debug("userAds...");
        final List<AdVisibleDto> adList = adService.showAllAdByUserId(id);
        return new ResponseEntity<>(adList, HttpStatus.OK);
    }
}
