package com.gstone.search.service;

import com.gstone.search.dao.ArticleSearchDao;
import com.gstone.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;


    public void add(Article article){
        articleSearchDao.save(article);
    }

    public Page<Article> findByTitleLike(String keywords, int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        //让title和content都有keyword
        return articleSearchDao.findByTitleOrContentLike(keywords, keywords, pageRequest);
    }
}
