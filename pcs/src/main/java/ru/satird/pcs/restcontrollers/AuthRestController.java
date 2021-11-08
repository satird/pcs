package ru.satird.pcs.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import ru.satird.pcs.config.jwt.JwtUtils;
import ru.satird.pcs.domains.RefreshToken;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.domains.VerificationToken;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.errors.TokenRefreshException;
import ru.satird.pcs.dto.payload.request.LogOutRequest;
import ru.satird.pcs.dto.payload.request.LoginRequest;
import ru.satird.pcs.dto.payload.request.SignupRequest;
import ru.satird.pcs.dto.payload.request.TokenRefreshRequest;
import ru.satird.pcs.dto.payload.response.JwtResponse;
import ru.satird.pcs.dto.payload.response.MessageResponse;
import ru.satird.pcs.dto.payload.response.TokenRefreshResponse;
import ru.satird.pcs.registration.OnRegistrationCompleteEvent;
import ru.satird.pcs.services.RefreshTokenService;
import ru.satird.pcs.services.RefreshTokenServiceImpl;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Api(description = "Контроллер авторизации")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenServiceImpl;
    private final MessageSource messages;

    @Autowired
    public AuthRestController(UserService userService,
                              AuthenticationManager authenticationManager,
                              JwtUtils jwtUtils, ApplicationEventPublisher eventPublisher,
                              RefreshTokenService refreshTokenServiceImpl,
                              @Qualifier("messageSource") MessageSource messages) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.eventPublisher = eventPublisher;
        this.refreshTokenServiceImpl = refreshTokenServiceImpl;
        this.messages = messages;
    }

    @ApiOperation(value = "Войти", notes = "Войти в приложение")
    @ApiImplicitParams({
            @ApiImplicitParam (name = "loginRequest", value = "Введите логин(email) и пароль",
                    required = true, dataType = "ru.satird.pcs.dto.payload.request.LoginRequest", paramType = "body")
    })
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        log.debug("authenticateUser...");
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
        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @ApiOperation(value = "Зарегистрироваться", notes = "Зарегистрироваться в приложении")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "signUpRequest", value = "Логин пользователя, email, привелегия пользователя, пароль",
                    required = true, dataType = "ru.satird.pcs.dto.payload.request.SignupRequest", paramType = "body")
    })
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(
            HttpServletRequest request,
            @Valid @RequestBody SignupRequest signUpRequest) {
        log.debug("registerUser...");
        Locale locale = request.getLocale();
        String message;
        if (Boolean.TRUE.equals(userService.existsUserByUsername(signUpRequest.getUsername()))) {
            message = messages.getMessage("message.regError.username", null, locale);
            log.warn("Matching usernames");
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        }
        if (Boolean.TRUE.equals(userService.existsUserByEmail(signUpRequest.getEmail()))) {
            message = messages.getMessage("message.regError.email", null, locale);
            log.warn("Matching email");
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(signUpRequest);
        userService.saveRegisteredUser(user);
        message = messages.getMessage("message.regSuccLink", null, locale);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), Util.getAppUrl(request)));
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @ApiOperation(value = "Обновить access токен", notes = "Получить новый токен авторизации по refresh token")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "request", value = "Объект с refresh token",
                    required = true, dataType = "ru.satird.pcs.dto.payload.request.TokenRefreshRequest", paramType = "body")
    })
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        log.debug("refreshtoken...");
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenServiceImpl.findByToken(requestRefreshToken)
                .map(refreshTokenServiceImpl::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @ApiOperation(value = "Подтверждение email", notes = "Перейти по ссылке для подтверждения email")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "token", value = "Токен подтверждения email",
                    required = true, dataTypeClass = String.class, paramType = "query", example = "8c3f7bfb-f2f5-45bc-aa90-7b92b7e49d33")
    })
    @GetMapping("/registrationConfirm")
    public ResponseEntity<MessageResponse> confirmRegistration(
            HttpServletRequest request,
            @RequestParam("token") String token
    ) {
        log.debug("confirmRegistration...");
        Locale locale = request.getLocale();
        VerificationToken verificationToken = userService.getVerificationToken(token);
        String message;
        if (verificationToken == null) {
            log.warn("verificationToken is null");
            message = messages.getMessage("auth.message.invalidToken", null, locale);
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.NOT_FOUND);
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            log.warn("verificationToken is expired");
            message = messages.getMessage("auth.message.expired", null, locale);
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.REQUEST_TIMEOUT);
        }
        user.setActive(true);
        userService.saveRegisteredUser(user);
        message = messages.getMessage("message.accountVerified", null, locale);
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

    @ApiOperation(value = "Выйти из аккаунта", notes = "Выйти из аккаунта")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "logOutRequest", value = "Объект с user id",
                    required = true, dataType = "ru.satird.pcs.dto.payload.request.LogOutRequest", paramType = "body")
    })
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(
            @Valid @RequestBody LogOutRequest logOutRequest
    ) {
        log.debug("logoutUser...");
        final int i = refreshTokenServiceImpl.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful! " + i));
    }
}
