package com.qushihan.service;

import com.alibaba.fastjson.JSON;
import com.qushihan.dao.IUserDAO;
import com.qushihan.po.Bbsuser;
import com.qushihan.po.Message;
import com.qushihan.service.kafka.consumer.MsgConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class UserServiceImpl {

    @Autowired
    private IUserDAO dao;

    @Autowired
    private MsgConsumer msgConsumer;

    private Map<String,String> types=new HashMap<String,String>();

    public UserServiceImpl(){
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    public Bbsuser login(Bbsuser user){
        return dao.login(user.getUsername(),user.getPassword());
    }

    public Bbsuser save(Bbsuser user){
        return dao.save(user);
    }

    public Bbsuser judgeUsername(Bbsuser user){
        return dao.judgeUsername(user.getUsername());
    }

    public Bbsuser findOne(int id){
        return dao.findOne(id);
    }

    public void updatePageNumById(Bbsuser user){
        dao.updatePageNumById(user.getPagenum(),user.getUserid());
    }

    /*
        commonsMultipartResolver.isMultipart 判断键值对还是流
        commonsMultipartResolver.resolveMultipart 将键值对req转化为多部分request
        commonsMultipartResolver.set* 设置上传图片各种参数
                设置好了才能用 file.transferTo(imageFile)
     */
    public Bbsuser uploadPic(MultipartHttpServletRequest req,CommonsMultipartResolver commonsMultipartResolver) { // 上传头像
        Bbsuser user = null;
        // 设置上传参数
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setResolveLazily(true); // 必须上传完毕才解析
        commonsMultipartResolver.setMaxInMemorySize(4096 * 1024); // 设置交换空间缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024);
        commonsMultipartResolver.setMaxUploadSize(2 * 1024 * 1024);
        if (commonsMultipartResolver.isMultipart(req)) {
            // 将键值对req转化为多部分request
            MultipartFile file = req.getFile("file0"); // 把图片的value流取出来
            String type = file.getContentType(); // 取得上传文件类型
            if (types.containsKey(type)) { // 是那4种类型之一
                File imageFile = new File("C:\\Users\\JimKing\\IdeaProjects\\BBS\\src\\main\\resources\\static\\upload"+File.separator+req.getSession().getId()+types.get(type)); // 把图片存入upload文件夹
                // 取得正常字段  内容
                String username = req.getParameter("reusername");
                String password = req.getParameter("repassword");
                user = new Bbsuser(username, password);
                user.setPicPath(imageFile.getPath());
                user.setPagenum(5);
                // 将二进制流图片file 传到imageFile上
                try {
                    file.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 把头像数据放到po中
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    user.setPic(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public String getMessage(int uid){
        List<Message> list = msgConsumer.consumerMsg(uid);
        return JSON.toJSONString(list);
    }
}
