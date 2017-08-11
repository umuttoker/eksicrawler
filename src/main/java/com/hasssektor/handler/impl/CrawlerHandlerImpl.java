package com.hasssektor.handler.impl;

import com.hasssektor.bean.TopicEntity;
import com.hasssektor.db.TopicRepository;
import com.hasssektor.db.UserRepository;
import com.hasssektor.eksiapi.models.Entry;
import com.hasssektor.eksiapi.models.Topic;
import com.hasssektor.eksiapi.models.User;
import com.hasssektor.handler.CrawlerHandler;
import com.hasssektor.util.TopicApiUtil;
import com.hasssektor.util.UserApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *   eksi api için https://github.com/umuttoker/eksiapi-java deki eksi-api.yaml i build etmek lazım pom daki artifact id si aynı olmalı
 *
 */

/**
 * Created by umut on 4.08.2017.
 */
@Service
public class CrawlerHandlerImpl implements CrawlerHandler{
    final Logger LOGGER = LoggerFactory.getLogger(CrawlerHandlerImpl.class);

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserApiUtil userApiUtil;

    @Autowired
    TopicApiUtil topicApiUtil;

    private final Map<String,Boolean> userList = new HashMap<>();
    private final List<String> unkownUserList = new ArrayList<>();

    @Override
    public void startCrawling(String topicId) {     // multi thread yapsak bizim için iyi ama ekşi için kötü olabilir
        TopicEntity lastTopic = topicRepository.findFirstByOrderByIdDesc();
        int topicIdAsInt = Integer.parseInt(topicId);
        if(lastTopic != null) {
            LOGGER.info("Databasedeki son baslik : "+lastTopic.getId()+"  burdan toplamaya devam ediliyor");
            fillUserMap();
            topicIdAsInt = lastTopic.getId() + 1;
        }
        else {
            LOGGER.info("Bu baslikdan : "+topicId+"toplamaya baslaniyor");
        }
        for(int i = topicIdAsInt; i < 5431854; i++){        //son baslik id si girilse iyi olur
            collectTopic(i);
        }
        LOGGER.info("Bittii...");
    }

    @Override
    public void updateUserData() {      // ilk başta modelde entryCounts yoktu ondan böyle saçmalamak zorunda kaldık
        List<User> users = userRepository.getWhoDoesNotHaveEntryCount();
        List<String> failedUsers = new ArrayList<>();
        int i = 0;
        LOGGER.info("Tam "+ users.size()+" user bilgisi guncellenecek!");
        for (User user : users) {
            LOGGER.info("User bilgisi isteniyor "+(++i)+"/"+users.size()+" User nick : "+user.getUserInfo().getUserIdentifier().getNick());
            User user1 = userApiUtil.getUser(user.getUserInfo().getUserIdentifier().getNick());
            if(user1 == null){
                LOGGER.error("User : "+user.getUserInfo().getUserIdentifier().getNick()+" bilgisi alinamadi atlaniyo!");
                failedUsers.add(user.getUserInfo().getUserIdentifier().getNick());
                continue;
            }
            userRepository.deleteByUserInfoUserIdentifierNick(user.getUserInfo().getUserIdentifier().getNick());
            userRepository.save(user1);
        }
        LOGGER.info("Bitti ve hatali user listesi soyle : "+failedUsers);
    }

    private void fillUserMap() {
        List<User> users = userRepository.findAllExcludeAllExceptNick();
        for (User user : users) {
            userList.put(user.getUserInfo().getUserIdentifier().getNick(), true);
        }
        LOGGER.info("User map dolduruldu tam tamina bu kadar user var : "+users.size());
    }

    private void collectTopic(int i) {
        Topic topic = topicApiUtil.getTopic(i, 1);
        if(topic == null) {
            LOGGER.error("Biseyler ters gitti ve topic cekilemedi topicId : "+i);
            return;
        }
        TopicEntity topicEntity = new TopicEntity(topic);
        List<String> users = new ArrayList<>();
        while(topic != null){
            addUsersToList(topic.getEntries(), users);
            if(topic.getPageCount()>topic.getPageIndex())
                topic = topicApiUtil.getTopic(i,topic.getPageIndex()+1);
            else
                break;
        }
        if(users.size()>0)
            getAndSaveUsers(users);
        topicRepository.save(topicEntity);
    }

    private void getAndSaveUsers(List<String> users) {
        LOGGER.info("Userlar kaydedilmeye calisilcak user sayisi : "+users.size());
        for (String user : users) {
            User user1 = userApiUtil.getUser(user);
            if(user1 == null)
                unkownUserList.add(user);
            else
                userRepository.save(user1);
        }
    }

    private void addUsersToList(List<Entry> entries, List<String> users) {
        for (Entry entry : entries) {
            if(userList.get(entry.getAuthor().getNick()) == null){
                userList.put(entry.getAuthor().getNick(),true);
                users.add(entry.getAuthor().getNick());
            }
        }
    }
}
