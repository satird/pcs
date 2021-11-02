package ru.satird.pcs.restcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.RefreshToken;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.errors.TokenRefreshException;
import ru.satird.pcs.payload.request.LogOutRequest;
import ru.satird.pcs.payload.request.LoginRequest;
import ru.satird.pcs.payload.request.SignupRequest;
import ru.satird.pcs.payload.request.TokenRefreshRequest;
import ru.satird.pcs.payload.response.JwtResponse;
import ru.satird.pcs.payload.response.MessageResponse;
import ru.satird.pcs.payload.response.TokenRefreshResponse;
import ru.satird.pcs.registration.OnRegistrationCompleteEvent;
import ru.satird.pcs.security.jwt.JwtUtils;
import ru.satird.pcs.services.RefreshTokenService;
import ru.satird.pcs.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private ApplicationEventPublisher eventPublisher;
    private RefreshTokenService refreshTokenService;
    private MessageSource messages;

    @Autowired
    public void setMessages(@Qualifier("messageSource") MessageSource messages) {
        this.messages = messages;
    }

    @Autowired
    public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        logger.debug("authenticateUser...");
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(
            HttpServletRequest request,
            @Valid @RequestBody SignupRequest signUpRequest) {
        logger.debug("registerUser...");
        Locale locale = request.getLocale();
        String message;
        if (Boolean.TRUE.equals(userService.existsUserByUsername(signUpRequest.getUsername()))) {
            message = messages.getMessage("message.regError.username", null, locale);
            logger.warn("Matching usernames");
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        }
        if (Boolean.TRUE.equals(userService.existsUserByEmail(signUpRequest.getEmail()))) {
            message = messages.getMessage("message.regError.email", null, locale);
            logger.warn("Matching email");
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(signUpRequest);
        userService.saveRegisteredUser(user);
        message = messages.getMessage("message.regSuccLink", null, locale);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), getAppUrl(request)));
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        logger.debug("refreshtoken...");
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<MessageResponse> confirmRegistration(
            HttpServletRequest request,
            @RequestParam("token") String token
    ) {
        logger.debug("confirmRegistration...");
        Locale locale = request.getLocale();
        VerificationToken verificationToken = userService.getVerificationToken(token);
        String message;
        if (verificationToken == null) {
            logger.warn("verificationToken is null");
            message = messages.getMessage("auth.message.invalidToken", null, locale);
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.NOT_FOUND);
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            logger.warn("verificationToken is expired");
            message = messages.getMessage("auth.message.expired", null, locale);
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.REQUEST_TIMEOUT);
        }
        user.setActive(true);
        userService.saveRegisteredUser(user);
        message = messages.getMessage("message.accountVerified", null, locale);
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(
            @Valid @RequestBody LogOutRequest logOutRequest
    ) {
        logger.debug("logoutUser...");
        final int i = refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful! " + i));
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/api/auth" + request.getContextPath();
    }
}
