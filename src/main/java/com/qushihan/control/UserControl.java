package com.qushihan.control;

import com.qushihan.po.Bbsuser;
import com.qushihan.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "/user")
public class UserControl {
    @Autowired
    private UserServiceImpl service;

    @RequestMapping(value = "/message") // 拉消息
    public void poll(@RequestParam(value = "uid") String uid, HttpServletResponse resp) {
        String result = service.getMessage(Integer.parseInt(uid));
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(result);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/pagenum/{pagenum}") // 修改用户的每页行数
    public void pagenum(@PathVariable(value = "pagenum") String pagenum, HttpServletRequest req, HttpServletResponse resp) {
        Bbsuser user = (Bbsuser) req.getSession().getAttribute("user");
        user.setPagenum(Integer.parseInt(pagenum));
        service.updatePageNumById(user);
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

    @RequestMapping(value = "/judgeusername") // 注册时验证是否存在用户
    public void judgeusername(@RequestParam(value = "username") String username, HttpServletResponse resp) {
        Bbsuser user = new Bbsuser();
        user.setUsername(username);
        user = service.judgeUsername(user);
        boolean flag = (user == null) ? true : false;
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(flag);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping("/reg") //springMVC post流格式直接MultipartHttpServletRequest
    public void reg(MultipartHttpServletRequest req, HttpServletResponse resp) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        Bbsuser user = service.uploadPic(req, commonsMultipartResolver);
        user = service.save(user);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute("user");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/pic/{id}") // 把所有需要图片的位置渲染给前端
    public void pic(@PathVariable(value = "id") String id, HttpServletResponse resp) {
        Bbsuser user = service.findOne(Integer.parseInt(id));
        byte[] buffer = user.getPic();
        resp.setContentType("image/jpeg");
        try {
            OutputStream os = resp.getOutputStream();
            os.write(buffer);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/login")
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletRequest req, HttpServletResponse resp) {
        Bbsuser user = new Bbsuser(username, password);
        user = service.login(user);
        boolean flag = (user == null) ? false : true;
        if (flag) {
            // 登录成功，需要把用户放到session让他每个页都能访问
            req.getSession().setAttribute("user", user);
            // 是否使用cookie
            String sun = req.getParameter("sun");
            if (sun != null) { // 勾上
                Cookie cookie1 = new Cookie("jimkingu", user.getUsername());
                cookie1.setMaxAge(3600 * 24 * 7);
                resp.addCookie(cookie1);
                Cookie cookie2 = new Cookie("jimkingp", user.getPassword());
                cookie2.setMaxAge(3600 * 24 * 7);
                resp.addCookie(cookie2);
            }
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println(flag);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
