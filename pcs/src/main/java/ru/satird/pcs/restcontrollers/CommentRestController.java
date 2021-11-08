package ru.satird.pcs.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.CommentDto;
import ru.satird.pcs.dto.CommentVisibleDto;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.services.CommentService;
import ru.satird.pcs.services.UserService;

import java.util.List;

@Api(description = "Контроллер комментариев")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/rest/api")
public class CommentRestController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentRestController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @ApiOperation(value = "Получить комментарии", notes = "Получить комментарии под объявлением")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "3"),
    })
    @GetMapping("/ad/{id}/comment")
    public ResponseEntity<List<CommentVisibleDto>> getAllCommentsByAd(
            @PathVariable("id") Long id
    ) {
        log.debug("getAllCommentsByAd...");
        final List<CommentVisibleDto> commentList = commentService.showAlCommentsByAd(id);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @ApiOperation(value = "Оставить комментарий", notes = "Оставить комментарий под объявлением")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "2"),
            @ApiImplicitParam (name = "commentDto", value = "Объект с текстом комментария", required = true, dataType = "ru.satird.pcs.dto.CommentDto", paramType = "body")
    })
    @PostMapping("/ad/{id}/comment")
    public ResponseEntity<CommentVisibleDto> createComment(
            @PathVariable("id") Ad ad,
            @RequestBody CommentDto commentDto
    ) {
        log.debug("createComment...");
        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String email = userDetails.getEmail();
        User currentUser = userService.findUserByEmail(email);
        final CommentVisibleDto newComment = commentService.createComment(ad, commentDto, currentUser);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}
