package com.hasssektor.db;

import com.hasssektor.eksiapi.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by umut on 4.08.2017.
 */
public interface UserRepository extends MongoRepository<User, String> {

    public User findByUserInfo_UserIdentifier_Nick(String firstName);

    @Query(value = "{}", fields = "{userInfo.userIdentifier.nick : 1}")
    public List<User> findAllExcludeAllExceptNick();

    @Query(value = "{userInfo.ınstagramProfileUrl: {$exists: true}}")
    List<User> findUsersWhoHasInstagram();

    @Query(value = "{userInfo.twitterScreenName: {$exists: true}}")
    List<User> findUsersWhoHasTwitter();

    @Query(value = "{userInfo.entryCounts : {$exists : false}}")
    List<User> getWhoDoesNotHaveEntryCount();   //modeli dogru kurmazsan boyle tekrar yaparsin iste

    void deleteByUserInfoUserIdentifierNick(String nick);

}