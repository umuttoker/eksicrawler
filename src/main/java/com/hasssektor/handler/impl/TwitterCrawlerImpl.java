package com.hasssektor.handler.impl;

import com.hasssektor.bean.Config;
import com.hasssektor.bean.InstaData;
import com.hasssektor.bean.TopicEntity;
import com.hasssektor.bean.TwitterData;
import com.hasssektor.db.TopicRepository;
import com.hasssektor.db.TwitterRepository;
import com.hasssektor.db.UserRepository;
import com.hasssektor.eksiapi.models.User;
import com.hasssektor.handler.TwitterCrawler;
import com.hasssektor.util.TopicApiUtil;
import com.hasssektor.util.UserApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by umut on 9.08.2017.
 */
@Service
public class TwitterCrawlerImpl  implements TwitterCrawler{

    final Logger LOGGER = LoggerFactory.getLogger(TwitterCrawlerImpl.class);

    @Autowired
    TwitterRepository twitterRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Twitter twitter;


    @Override
    public void crawlUserData() {
        List<User> usersWhoHasTwitter = userRepository.findUsersWhoHasTwitter();
        if(usersWhoHasTwitter == null || usersWhoHasTwitter.size() < 1){
            LOGGER.info("Hic bir kullanici twitter seyapmamis.");
            return;
        }
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : usersWhoHasTwitter)
            userMap.put(user.getUserInfo().getUserIdentifier().getId(),user);

        List<TwitterData> allUsersId = twitterRepository.findAllUserIds();

        LOGGER.info("Db de "+allUsersId.size()+" kadar twitter profil bilgisi varmis.");
        for (TwitterData twitterData : allUsersId)
            userMap.remove(twitterData.getId());

        if(userMap.size()>0)
            LOGGER.info("Toplanacak twitter profil sayisi : " + userMap.size());

        for (User user : userMap.values()) {
            collectTwitterData(user);
            try {
                Thread.sleep(1000);     //900 api call limitasyonu var 15dk icin
            } catch (InterruptedException e) {
                LOGGER.error("uyuyamadik :((((",e);
            }
        }
        LOGGER.info("Bitti iste...");
    }

    private void collectTwitterData(User user) {
        LOGGER.info("Twitter bilgisi sunun icin isteniyor : "+user.getUserInfo().getTwitterScreenName());
        TwitterProfile userProfile = null;
        try {
            userProfile = twitter.userOperations().getUserProfile(user.getUserInfo().getTwitterScreenName());
        }catch (ResourceNotFoundException e){
            LOGGER.error("Boyle bi user yok twitter Screen name : " + user.getUserInfo().getTwitterScreenName());
            return;
        }catch (Exception e){
            LOGGER.error("Fuck that shit bi hata!");
        }
        if(userProfile == null){
            LOGGER.error("Bir sekilde bu twitter profili bulunamadı twitter Screen name : " + user.getUserInfo().getTwitterScreenName());
            return;
        }
        TwitterData twitterData = new TwitterData();
        twitterData.setId(user.getUserInfo().getUserIdentifier().getId());
        twitterData.setNick(user.getUserInfo().getUserIdentifier().getNick());
        twitterData.setTwitterProfile(userProfile);
        twitterRepository.save(twitterData);
    }
}
