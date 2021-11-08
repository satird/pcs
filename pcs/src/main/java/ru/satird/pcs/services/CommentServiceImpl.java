package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.CommentDto;
import ru.satird.pcs.dto.CommentVisibleDto;
import ru.satird.pcs.mapper.CommentMapper;
import ru.satird.pcs.repositories.CommentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentVisibleDto createComment(Ad ad, CommentDto commentDto, User user) {
        Comment comment = commentMapper.mapComment(commentDto);
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        final Comment createdComment = commentRepository.save(comment);
        return commentMapper.mapCommentVisibleDto(createdComment);
    }

    @Override
    public List<CommentVisibleDto> showAlCommentsByAd(Long id) {
        return commentMapper.mapCommentVisibleDtoList(commentRepository.findAllByAd_Id(id));
    }
}
