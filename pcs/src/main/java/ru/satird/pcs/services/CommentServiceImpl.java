package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.repositories.CommentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(Ad ad, Comment comment, User user) {
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> showAlComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> showAlCommentsByAd(Long id) {
        return commentRepository.findAllByAd_Id(id);
    }
}
