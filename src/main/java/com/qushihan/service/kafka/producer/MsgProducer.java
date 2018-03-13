package com.qushihan.service.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.qushihan.po.Article;
import com.qushihan.po.Message;
import com.qushihan.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component(value = "msgProducer")
public class MsgProducer {

    @Autowired
    ArticleServiceImpl service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMsg(Article article) {
        Message message = new Message();
        message.setId(service.findOne(article.getRootid()).getUser().getUserid());// 发主贴的用户ID
        message.setRid(article.getUser().getUserid());// 发回贴的用户id
        message.setSendTime(article.getDatetime());
        message.setTitle(article.getTitle());// 从贴的title
        message.setTxt(service.findOne(article.getRootid()).getTitle());// 把主贴title当成内容发送
        String topic = "reply" + message.getId();// 针对该主贴用户建立主题，现在是回帖用户
        // 监听日志选项
        kafkaTemplate.setProducerListener(new KafkaProducerListener());
        System.out.println(JSON.toJSONString(message));
        kafkaTemplate.send(topic, JSON.toJSONString(message));
    }
}
