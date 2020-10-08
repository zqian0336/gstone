package com.gstone.article.dao;

import com.gstone.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentDao extends MongoRepository<Comment, String> {
    public List<Comment> findByArticleid(String articleid);

}
