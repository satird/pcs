package ru.satird.pcs.util;

public final class Views {

    public interface CommentIdText {}

    public interface CommentIdTextDate extends CommentIdText {}

    public interface FullComment extends CommentIdTextDate {}

    public interface AdBasic {}

    public interface AdBasicAndComments extends AdBasic {}

    public interface AdFull extends AdBasicAndComments {}

    public interface UserBasic {}

    public interface MessageBasic {}
}
