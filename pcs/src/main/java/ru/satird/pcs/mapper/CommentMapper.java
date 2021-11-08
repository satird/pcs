package ru.satird.pcs.mapper;

import org.mapstruct.Mapper;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.dto.CommentDto;
import ru.satird.pcs.dto.CommentVisibleDto;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment mapComment(CommentVisibleDto commentVisibleDto);
    CommentVisibleDto mapCommentVisibleDto(Comment comment);
    List<Comment> mapCommentList(List<CommentVisibleDto> commentVisibleDtoList);
    List<CommentVisibleDto> mapCommentVisibleDtoList(List<Comment> commentList);
    Comment mapComment(CommentDto commentDto);
    CommentDto mapCommentDto(Comment comment);
}
