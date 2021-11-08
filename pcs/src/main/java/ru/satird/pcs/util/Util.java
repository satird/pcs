package ru.satird.pcs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.services.UserService;

import javax.servlet.http.HttpServletRequest;

@Component
public class Util {

    private UserService userService;

    @Autowired
    public Util(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser() {
        UserDetailsImpl sender = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserByEmail(sender.getEmail());
    }

    public static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/api/auth" + request.getContextPath();
    }
}
