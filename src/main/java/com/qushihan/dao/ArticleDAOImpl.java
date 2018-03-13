package com.qushihan.dao;

import com.qushihan.po.Article;
import com.qushihan.po.Bbsuser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ArticleDAOImpl {
    @PersistenceContext
    private EntityManager entityManager;

    // 通过主贴id  查询所有从贴
    public Map<String, Object> queryById(int id) {
        Map<String, Object> map = new HashMap<>();
        // 执行存储过程
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_1");
        storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
        storedProcedure.setParameter(1, id);
        storedProcedure.execute();
        List<Object[]> items = storedProcedure.getResultList();
        List<Article> list = new ArrayList<>();
        for (Object[] item : items) {// 映射 方式HQL
            Article article = new Article();
            article.setId(Integer.parseInt(item[0].toString()));
            article.setRootid(Integer.parseInt(item[1].toString()));
            article.setTitle(item[2].toString());
            article.setContent(item[3].toString());
            article.setUser(new Bbsuser(Integer.parseInt(item[4].toString())));
            try {
                Date date = new SimpleDateFormat("yyyy-mm-dd").parse(item[5].toString());
                article.setDatetime(new java.sql.Date(date.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(article);
        }
        // lambdas表达式写法
//        list.forEach((s)->{
//            Article a=new Article();
//            a.setId(Integer.parseInt(s[0].toString()));
//            a.setRootid(Integer.parseInt(s[1].toString()));
//            a.setTitle(s[2].toString());
//            a.setContent(s[3].toString());
//            a.setUser(new Bbsuser(Integer.parseInt(s[4].toString())));
//            try {
//                java.util.Date d=new SimpleDateFormat("yyyy-mm-dd").parse(s[5].toString());
//                a.setDatetime(new java.sql.Date(d.getTime()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            alist.add(a);
//        });
        map.put("title", storedProcedure.getOutputParameterValue(2));// 主贴title
        map.put("list", list);
        return map;
    }
}
