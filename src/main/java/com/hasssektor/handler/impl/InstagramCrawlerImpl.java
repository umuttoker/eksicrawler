package com.hasssektor.handler.impl;

import com.hasssektor.bean.Config;
import com.hasssektor.bean.InstaData;
import com.hasssektor.db.InstaDataRepository;
import com.hasssektor.db.UserRepository;
import com.hasssektor.eksiapi.models.User;
import com.hasssektor.handler.InstagramCrawler;
import com.hasssektor.instaapi.models.UserInfo;
import com.hasssektor.util.InstaApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *   bu insta api için https://github.com/umuttoker/eksiapi-java deki insta.yaml i build etmek lazım pom daki artifact id si falan da ona göre değiştirilmeli
 *
 */



/**
 * Created by umut on 6.08.2017.
 */
@Service
public class InstagramCrawlerImpl implements InstagramCrawler{
    Logger LOGGER = LoggerFactory.getLogger(InstagramCrawlerImpl.class);

    @Autowired
    Config config;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InstaDataRepository instaDataRepository;

    @Autowired
    InstaApiUtil instaApiUtil;

    @Override
    public void crawlUserData() {
        List<User> usersWhoHasInstagram = userRepository.findUsersWhoHasInstagram();
        if(usersWhoHasInstagram == null || usersWhoHasInstagram.size() < 1){
            LOGGER.info("Hic bir kullanici instagramini seyapmamis.");
            return;
        }
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : usersWhoHasInstagram)
            userMap.put(user.getUserInfo().getUserIdentifier().getId(),user);

        List<InstaData> allUsersId = instaDataRepository.findAllUserNick();

        LOGGER.info("Db de "+allUsersId.size()+" kadar insagram profil bilgisi varmis.");
        for (InstaData instaData : allUsersId)
            userMap.remove(instaData.getId());

        if(userMap.size()>0)
            LOGGER.info("Toplanacak instagram profil sayisi : " + userMap.size());

        for (User user : userMap.values()) {
            collectInstaData(user);
        }
        LOGGER.info("Bitti iste...");
    }

    private void collectInstaData(User user) {
        UserInfo userInfo = null;
        userInfo = instaApiUtil.getUserInfo(user.getUserInfo().getInstagramScreenName());
        if(userInfo==null)
            return;
        InstaData instaData = new InstaData();
        instaData.setFollowerCount(user.getFollowerCount());
        instaData.setFollowerCount(user.getFollowingsCount());
        instaData.setId(user.getUserInfo().getUserIdentifier().getId());
        instaData.setInstaUserInfo(userInfo);
        instaDataRepository.save(instaData);
    }
}
