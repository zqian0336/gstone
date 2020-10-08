package com.gstone.discussion.dao;

import com.gstone.discussion.pojo.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscussionDao extends MongoRepository<Discussion, String> {
    //parameter一定是pojo的内容，不然mongo对不上
    public Page<Discussion> findByParentid(String parentid, Pageable pageable);
}
