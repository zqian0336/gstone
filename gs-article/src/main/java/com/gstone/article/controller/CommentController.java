package com.gstone.article.controller;

import com.gstone.article.pojo.Comment;
import com.gstone.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentSerive;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentSerive.add(comment);
        return new Result(true, StatusCode.OK, "Sent Successfully");
    }

    @RequestMapping(value = "/article/{articleid}", method = RequestMethod.GET)
    public Result findByArticleid(@PathVariable String articleid){
        return new Result(true, StatusCode.OK, "Successfully find", commentSerive.findByArticleid(articleid));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        commentSerive.deleteById(id);
        return new Result(true, StatusCode.OK, "Delete Successfully");
    }
}
