package com.qushihan.dao;

import com.qushihan.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IArticleDAO extends CrudRepository<Article, Integer> {
    @Query("select c from Article c where rootid = :rid")
    Page<Article> findAll(Pageable pageable, @Param("rid") Integer rid);

    @Modifying
    @Query("delete from Article where id = :id or rootid = :id")
    void deleteZT(@Param("id") Integer id);

    @Modifying
    @Query("delete from Article where id = :id and rootid = :rootid")
    void deleteCT(@Param("id") Integer id, @Param("rootid") Integer rootid);

    @Override
    Article save(Article article);

    // 根据发从贴的rootid找到id = rootid的帖子
    @Query("select c from Article c where id = :rootid")
    Article findOne(@Param("rootid") Integer rootid);
}
