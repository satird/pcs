package ru.satird.pcs.services;

import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.CommentDto;
import ru.satird.pcs.dto.CommentVisibleDto;

import java.util.List;

public interface CommentService {

    CommentVisibleDto createComment(Ad ad, CommentDto comment, User user);
    List<CommentVisibleDto> showAlCommentsByAd(Long id);
}
