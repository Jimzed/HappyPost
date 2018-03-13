package com.qushihan.control;

import com.alibaba.fastjson.JSON;
import com.qushihan.po.Article;
import com.qushihan.po.Bbsuser;
import com.qushihan.po.PageBean;
import com.qushihan.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/article")
public class ArticleControl {

    @Autowired
    private ArticleServiceImpl service;

    @RequestMapping(value = "/delc")
    private void delc(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("cid");
        String rootid = req.getParameter("id");
        service.deleteCT(Integer.parseInt(id), Integer.parseInt(rootid));
        queryreply(req, resp);
    }

    @RequestMapping(value = "/queryreply")
    private void queryreply(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        Map<String, Object> map = service.queryById(Integer.parseInt(id));
//        {
//            "title":"如何判断一个人真有钱还是装的?",
//                "list":[]
//        }
        String json = JSON.toJSONString(map, true);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        try {
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/add")
    private void add(HttpServletRequest req, HttpServletResponse resp) {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String rootid = req.getParameter("rootid");
        String userid = req.getParameter("userid");
        Article article = new Article();
        article.setRootid(Integer.parseInt(rootid));
        article.setTitle(title);
        article.setContent(content);
        article.setDatetime(new Date(System.currentTimeMillis()));
        article.setUser(new Bbsuser(Integer.parseInt(userid)));
        if (service.save(article) != null) {
            if (rootid.equals("0")) { // 主贴
                RequestDispatcher dispatcher = null;
                dispatcher = req.getRequestDispatcher("/");
                try {
                    dispatcher.forward(req, resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // 从贴
                queryreply(req, resp);
            }
        }
    }

    @RequestMapping(value = "/delZT/{id}")
    private void delZT(@PathVariable(value = "id") String id, HttpServletRequest req, HttpServletResponse resp) {
        service.deleteZT(Integer.parseInt(id));
        RequestDispatcher dispatcher = null;
        dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/queryall/{page}")
    private String queryall(@PathVariable(value = "page") String page, HttpServletRequest req, HttpServletResponse resp, Map map1) {
        // page为当前页
        // 每页数据个数
        int pagenum = 5;
        Bbsuser user = (Bbsuser) req.getSession().getAttribute("user");
        if (user != null) {
            pagenum = user.getPagenum();
        }
        // 排序
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(Integer.parseInt(page) - 1, pagenum, sort);
        Page<Article> pa = service.findAll(pageable, 0); // rid=0 默认查主贴
        PageBean pb = new PageBean();
        pb.setCurPage(Integer.parseInt(page));
        pb.setMaxPage(pa.getTotalPages());
        pb.setMaxRowCount(pa.getTotalElements());
        pb.setRowsPerpage(pagenum);
        pb.setData(pa.getContent());
        map1.put("pb", pb);
        return "show";
    }
}
