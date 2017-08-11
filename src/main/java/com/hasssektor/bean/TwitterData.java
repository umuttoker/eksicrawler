package com.hasssektor.bean;

import org.springframework.social.twitter.api.TwitterProfile;

/**
 * Created by umut on 10.08.2017.
 */
public class TwitterData {

    TwitterProfile twitterProfile;

    int id;

    String nick;

    public TwitterProfile getTwitterProfile() {
        return twitterProfile;
    }

    public void setTwitterProfile(TwitterProfile twitterProfile) {
        this.twitterProfile = twitterProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "TwitterData{" +
                "twitterProfile=" + twitterProfile +
                ", id='" + id + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
