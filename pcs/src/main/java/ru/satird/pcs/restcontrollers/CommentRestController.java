package ru.satird.pcs.restcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.CommentDto;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.services.CommentService;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.util.Views;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/rest/api")
public class CommentRestController {

    private CommentService commentService;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/ad/{id}/comment")
    @JsonView(Views.CommentIdTextDate.class)
    public ResponseEntity<List<Comment>> getAllCommentsByAd(
            @PathVariable("id") Long id
    ) {
        logger.debug("getAllCommentsByAd...");
        final List<Comment> commentList = commentService.showAlCommentsByAd(id);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @PostMapping("/ad/{id}/comment")
    @JsonView(Views.CommentIdTextDate.class)
    public ResponseEntity<Comment> createComment(
            @PathVariable("id") Ad ad,
            @RequestBody CommentDto comment
    ) {
        logger.debug("createComment...");
        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String email = userDetails.getEmail();
        User currentUser = userService.findUserByEmail(email);
        final Comment newComment = commentService.createComment(ad, convertToCommentEntity(comment), currentUser);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    private Comment convertToCommentEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
