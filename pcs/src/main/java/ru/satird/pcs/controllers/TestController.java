package ru.satird.pcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.services.UserService;

import java.util.Calendar;
import java.util.Locale;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class TestController {

    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LOGIN_PAGE = "redirect:/api/auth/login?lang=";
    private UserService userService;
    private MessageSource messages;

    @Autowired
    public void setMessages(@Qualifier("messageSource") MessageSource messages) {
        this.messages = messages;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        User byUsername = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return "<h1 style='margin: 5rem; color: skyblue;'>Hello " + byUsername.getName() + " , Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() + "</h1>" +
                "</br><a href='/logout'>Выйти</a>";
    }
}
