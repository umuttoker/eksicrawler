package com.hasssektor.bean;

import com.hasssektor.eksiapi.models.UserIdentifier;
import com.hasssektor.instaapi.models.UserInfo;

/**
 * Created by umut on 6.08.2017.
 */
public class InstaData {

    private int id;
    private UserIdentifier userIdentifier;
    private UserInfo instaUserInfo;
    private int followerCount;
    private int followingCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public UserInfo getInstaUserInfo() {
        return instaUserInfo;
    }

    public void setInstaUserInfo(UserInfo instaUserInfo) {
        this.instaUserInfo = instaUserInfo;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    @Override
    public String toString() {
        return "InstaData{" +
                "id='" + id + '\'' +
                ", userIdentifier=" + userIdentifier +
                ", instaUserInfo=" + instaUserInfo +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                '}';
    }
}
