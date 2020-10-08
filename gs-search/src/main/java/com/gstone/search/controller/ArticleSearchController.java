package com.gstone.search.controller;

import com.gstone.search.pojo.Article;
import com.gstone.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    @RequestMapping(method= RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK, "Success");
    }

    @RequestMapping(value="/search/{keywords}/{page}/{size}",method= RequestMethod.GET)
    public Result findByTitleLike(@PathVariable String keywords, @PathVariable int page, @PathVariable int size){
        Page<Article> articlePage = articleSearchService.findByTitleLike(keywords,page,size);
        return new Result(true, StatusCode.OK, "查询成功",
                new PageResult<Article>(articlePage.getTotalElements(),
                        articlePage.getContent())); }

}
