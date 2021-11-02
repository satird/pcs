package ru.satird.pcs.restcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.errors.InvalidOldPasswordException;
import ru.satird.pcs.payload.response.MessageResponse;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.util.Views;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/rest/api")
public class UserRestController {

    private UserService userService;
    private AdService adService;
    private MessageSource messages;

    @Autowired
    public void setAdService(AdService adService) {
        this.adService = adService;
    }

    @Autowired
    public void setMessages(@Qualifier("messageSource") MessageSource messages) {
        this.messages = messages;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUser() {
        final List<User> users = userService.showAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("id") Long id
    ) {
        logger.debug("getUserById...");
        final User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(
            HttpServletRequest request,
            @PathVariable("id") Long id,
            @RequestParam(value = "name") String name
    ) {
        logger.debug("updateUser...");
        if (name == null || name.isEmpty() || !request.getParameterMap().containsKey("name")) {
            logger.error("Error method 'updateUser' check the name");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = userService.updateUser(id, name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user/{id}/updatePassword")
    public ResponseEntity<MessageResponse> changeUserPassword(
            Locale locale,
            @PathVariable("id") Long id,
            @RequestParam("password") String password,
            @RequestParam("oldpassword") String oldPassword
    ) {
        logger.debug("changeUserPassword...");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findUserById(userDetails.getId());
        final User user = userService.findUserById(id);
        if (currentUser.equals(user)) {
            if (!userService.checkIfValidOldPassword(user, oldPassword)) {
                logger.error("Error method 'changeUserPassword' check the old password");
                throw new InvalidOldPasswordException();
            }
            userService.changeUserPassword(user, password);
            return new ResponseEntity<>(new MessageResponse(messages.getMessage("message.updatePasswordSuc", null, locale)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Only an authorized user can change their password"), HttpStatus.BAD_REQUEST);
    }

    @JsonView(Views.AdBasic.class)
    @GetMapping("/user/{id}/ad")
    public ResponseEntity<List<Ad>> userAds(
            @PathVariable("id") Long id
    ) {
        logger.debug("userAds...");
        final List<Ad> adList = adService.showAllAdByUserId(id);
        return new ResponseEntity<>(adList, HttpStatus.OK);
    }
}
