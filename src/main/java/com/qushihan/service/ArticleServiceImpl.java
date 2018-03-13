package com.qushihan.service;

import com.alibaba.fastjson.JSONObject;
import com.qushihan.dao.ArticleDAOImpl;
import com.qushihan.dao.IArticleDAO;
import com.qushihan.po.Article;
import com.qushihan.service.kafka.producer.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ArticleServiceImpl {
    @Autowired
    private IArticleDAO dao1;

    @Autowired
    private ArticleDAOImpl dao2;

    @Autowired
    private MsgProducer msgProducer;

    private Map<String, String> types = new HashMap<String, String>();

    public ArticleServiceImpl() {
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    public Page<Article> findAll(Pageable pageable, int rid) {
        return dao1.findAll(pageable, rid);
    }

    public void deleteZT(int id) {
        dao1.deleteZT(id);
    }

    public void deleteCT(Integer id, Integer rootid) {
        dao1.deleteCT(id, rootid);
    }

    public Map<String, Object> queryById(int id) {
        return dao2.queryById(id);
    }

    public Article findOne(int rootid) {
        return dao1.findOne(rootid);
    }

    public String uploadPic(HttpServletRequest req) { // 上传图片
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        // 设置上传参数
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setResolveLazily(true); // 必须上传完毕才解析
        commonsMultipartResolver.setMaxInMemorySize(4096 * 1024); // 设置交换空间缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024);
        commonsMultipartResolver.setMaxUploadSize(2 * 1024 * 1024);
        if (commonsMultipartResolver.isMultipart(req)) {
            // 将键值对req转化为多部分request
            MultipartHttpServletRequest request = (MultipartHttpServletRequest)req;
            MultipartFile file = request.getFile("imgFile"); // 把图片的value流取出来
            String type = file.getContentType(); // 取得上传文件类型
            if (types.containsKey(type)) { // 是那4种类型之一
                // 得到file:/C:/Users/JimKing/IdeaProjects/BBS/target/classes/
                String s3 = this.getClass().getClassLoader().getResource("").toString();
                // 取得文件上传的目的目录 image
                String dir = req.getParameter("dir");
                // 得到上传后文件名字，唯一名称
                String id = UUID.randomUUID().toString();
                // file:/C:/Users/JimKing/IdeaProjects/BBS/target/classes/static/editor/upload/image/93b7d267-d87e-4193-b422-e1f57e5d24e4.jpg
                String newFileName = s3 + "static/editor/upload/" + dir + "/" + id + types.get(type);
                // 得到 C:/Users/JimKing/IdeaProjects/BBS/target/classes/static/editor/upload/image/93b7d267-d87e-4193-b422-e1f57e5d24e4.jpg
                newFileName = newFileName.substring(6);
                File imageFile = new File(newFileName); // 把图片存入image文件夹
                // 将二进制流图片file 传到imageFile上
                try {
                    file.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 形成在编辑器页面显示的url地址 http://localhost:8081
                String tpath = req.getRequestURL().toString();
                tpath = tpath.substring(0, tpath.lastIndexOf("/"));
                tpath = tpath.substring(0, tpath.lastIndexOf("/"));
                // http://localhost:8081/editor/upload/image/
                String path = tpath + "/static/editor/upload/" + dir + "/"; //最终显示在编辑器中图片路径
                JSONObject obj = new JSONObject();
                obj.put("error", 0);//无错误
                obj.put("url", path + id + types.get(type));// 使用json格式把上传文件信息传递到前端
                return obj.toJSONString();
            }
        }
        return "";
    }

    public Article save(Article article) {
        // 先保存再发消息
        article = dao1.save(article);
        if (article.getRootid() != 0) {// 从贴
            msgProducer.sendMsg(article);
        }
        return article;
    }
}
