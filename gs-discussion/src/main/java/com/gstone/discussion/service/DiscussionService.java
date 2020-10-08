package com.gstone.discussion.service;

import com.gstone.discussion.dao.DiscussionDao;
import com.gstone.discussion.pojo.Discussion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiscussionService {

    @Autowired
    private DiscussionDao discussionDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Discussion> findAll(){
        return discussionDao.findAll();
    }

    public Discussion findById(String id){
        return discussionDao.findById(id).get();
    }


    public void save(Discussion discussion){
        discussion.set_id(idWorker.nextId()+"");
        discussion.setPublishtime(new Date());        // 发布日期
        discussion.setVisits(0);      // 浏览量
        discussion.setShare(0);       // 分享数
        discussion.setThumbup(0);     // 点赞数
        discussion.setComment(0);     // 回复数
        discussion.setState("1");     // 状态

        // 如果当前的吐槽有父节点，那么父节点加一
        if (discussion.getParentid() != null && !"".equals(discussion.getParentid())) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(discussion.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "discussion");
        }
        discussionDao.save(discussion);
    }
    public void update(Discussion discussion){
        discussionDao.save(discussion);
    }
    public void deleteById(String id){
        discussionDao.deleteById(id);
    }

    public Page<Discussion> findByParentid(String parentid, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return discussionDao.findByParentid(parentid, pageable);
    }



    public void thumbup(String dicussionId){
//        //方式一：效率有问题
//        Discussion discussion = discussionDao.findById(discussionId).get();
//        discussion.setThumbup((spit.getThumbup()==null ?0 : spit.getThumbup()) + 1);
//        discussionDao.save(spit);
        //方式二,使用原生mongo命令实现自增 db.discussion.update({"_id":"1"}.{$inc:{thumbup:NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("1"));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"discussion");


    }



}
