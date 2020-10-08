package com.gstone.discussion.controller;

import com.gstone.discussion.pojo.Discussion;
import com.gstone.discussion.service.DiscussionService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",discussionService.findAll());
    }

    @RequestMapping(value="/{discussionId}",method=RequestMethod.GET)
    public Result findById(@PathVariable String discussionId){
        return new Result(true,StatusCode.OK,"查询成功",discussionService.findById(discussionId));
    }

    @RequestMapping(method=RequestMethod.POST)
    public Result save(@RequestBody Discussion discussion){
        discussionService.save(discussion);
        return new Result(true,StatusCode.OK,"保存成功");
    }

    @RequestMapping(value="/{discussionId}",method=RequestMethod.PUT)
    public Result update(@PathVariable String discussionId,@RequestBody Discussion discussion){
        discussion.set_id(discussionId);
        discussionService.update(discussion);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @RequestMapping(value="/{discussionId}",method=RequestMethod.DELETE)
    public Result delete(@PathVariable String discussionId){
        discussionService.deleteById(discussionId);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    @RequestMapping(value="/comment/{parentId}/{page}/{size}",method=RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentId,@PathVariable int page, @PathVariable int size){
        Page<Discussion> pageData = discussionService.findByParentid(parentId,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Discussion>(pageData.getTotalElements(),pageData.getContent()));
    }






    @RequestMapping(value="/thumbup/{discussionId}",method=RequestMethod.PUT)
    public Result thumbup(@PathVariable String discussionId){

        //判断当前用户是否已经点赞，但是现在没有做认证，所以暂时把userid写死
        String userid = "111";
        //判断当前用户是否已经点赞
        if (redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            return new Result(false,StatusCode.REPERROR,"不能重复点赞");
        }
        discussionService.thumbup(discussionId);
        redisTemplate.opsForValue().set("thumbup_"+userid,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
