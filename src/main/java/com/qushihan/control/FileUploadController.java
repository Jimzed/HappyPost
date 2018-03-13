package com.qushihan.control;

import com.qushihan.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 在static/editor/plugins/image/image.js的第17行左右 和 multiimage.js文件201行加上要上传的地址
@Controller
@RequestMapping(value = "/kindupload")
public class FileUploadController {

    @Autowired
    private ArticleServiceImpl service;

    /**
     * 单个文件上传具体实现方法;
     *
     * @return
     */
    @RequestMapping(value = "/up")
    public void ok(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String s = service.uploadPic(req);
        PrintWriter out = null;
        out = resp.getWriter();
        out.print(s);
        out.flush();
        out.close();
    }
}
