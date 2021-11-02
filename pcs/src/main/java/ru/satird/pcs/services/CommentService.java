package ru.satird.pcs.services;

import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.domains.User;

import java.util.List;

public interface CommentService {

    Comment createComment(Ad ad, Comment comment, User user);
    List<Comment> showAlComments();
    List<Comment> showAlCommentsByAd(Long id);
}
