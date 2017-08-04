package com.hasssektor.handler.impl;

import com.hasssektor.bean.Config;
import com.hasssektor.db.UserRepository;
import com.hasssektor.handler.CrawlerHandler;
import io.swagger.client.ApiException;
import io.swagger.client.api.EntryApi;
import io.swagger.client.api.UserApi;
import io.swagger.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by umut on 4.08.2017.
 */
@Service
public class CrawlerHandlerImpl implements CrawlerHandler{
    final Logger LOGGER = LoggerFactory.getLogger(CrawlerHandlerImpl.class);

    @Autowired
    Config config;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserApi userApi;

    @Autowired
    EntryApi entryApi;

    private final Map<String,Boolean> userList = new HashMap<>();
    private final List<String> userQueue = new ArrayList<>();

    public String getApiKey() {
        return "bearer ***";
    }

    @Override
    public void crawlUsers(String initialUser) {
        userQueue.add(initialUser);
        collectUsers();
    }

    private void collectUsers(){
        String userName = userQueue.get(0);
        InlineResponse200 user = null;
        if(userName == null){
            LOGGER.info("That's it!!!!!!!!!");
            return;
        }
        LOGGER.info("Trying to get all info and related user of User : "+userName);
        try {
            user = userApi.getUserByName(userName);
        } catch (ApiException e) {
            e.printStackTrace();
            collectUsers();
            return;
        }
        List<Integer> favoritedEntries = getAllFavoritedEntries(userName);
        List<String> favoriAuthors = getAllFavoriAuthors(userName);
        addFavoritedUsersToQueue(favoritedEntries);
        addUserToQueueWithString(favoriAuthors);
        if(user != null)
            userRepository.save(user);
        userQueue.remove(0);
        collectUsers();
    }

    private void addFavoritedUsersToQueue(List<Integer> entries) {
        for (Integer favoriEntry : entries) {
            InlineResponse2002 response = null;
            try {
                response = entryApi.entryEntryIdFavoritesGet(String.valueOf(favoriEntry));
            } catch (ApiException e) {
                e.printStackTrace();
                if(e.getCode() == 401)
                    renewApiKey();
            }
            addUserToQueue(response.getAuthors());
        }
    }

    private void addUserToQueue(List<InlineResponse2001EntryAuthor> authors) {
        for (InlineResponse2001EntryAuthor author : authors) {
            if(userList.get(author.getNick()) == null) {
                userList.put(author.getNick(), true);
                userQueue.add(author.getNick());
            }
        }
        LOGGER.info("User queue size : "+userQueue.size()+" \nUser map size : "+userList.size());
    }

    private void addUserToQueueWithString(List<String> authors) {
        for (String author : authors) {
            if(userList.get(author) == null) {
                userList.put(author, true);
                userQueue.add(author);
            }
        }
        LOGGER.info("User queue size : "+userQueue.size()+" \nUser map size : "+userList.size());
    }

    private List<String> getAllFavoriAuthors(String userName) {
        LOGGER.info("Suserin tüm favori yazarları çekiliyor!");
        InlineResponse2001 page = null;
        List<String> result = new ArrayList<>();
        try {
            page = userApi.userUsernameFavoritesGet(userName,1);
        } catch (ApiException e) {
            e.printStackTrace();
            if(e.getCode() == 401) {
                renewApiKey();
                collectUsers();
                return null;
            }
        }
        while(page != null && page.getEntries().size()>0){
            addEntryAuthorsToList(result, page);
            if(page.getPageCount()>page.getPageIndex()) {
                try {
                    page = userApi.userUsernameFavoritesGet(userName, page.getPageIndex() + 1);
                } catch (ApiException e) {
                    e.printStackTrace();
                    collectUsers();
                    return null;
                }
            }
            else break;
        }
        LOGGER.info("Suserin tüm favori yazarları çekildi boyutu da ahan da şu : "+result.size());
        return result;
    }

    private void addEntryAuthorsToList(List<String> result, InlineResponse2001 page) {
        for (InlineResponse2001Entries entry : page.getEntries()) {
            result.add(entry.getEntry().getAuthor().getNick());
        }

    }

    private List<Integer> getAllFavoritedEntries(String userName) {
        LOGGER.info("Suserin favorilenen tüm entryleri çekiliyor!");
        InlineResponse2001 page = null;
        List<Integer> result = new ArrayList<>();
        try {
            page = userApi.userUsernameFavoritedGet(userName,1);
        } catch (ApiException e) {
            e.printStackTrace();
            if(e.getCode() == 401) {
                renewApiKey();
                collectUsers();
                return null;
            }
        }
        while(page != null && page.getEntries().size()>0){
            addEntryIdsToList(result, page);
            if(page.getPageCount()>page.getPageIndex()) {
                try {
                    page = userApi.userUsernameFavoritedGet(userName, page.getPageIndex() + 1);
                }
                catch (ApiException e) {
                    e.printStackTrace();
                    collectUsers();
                    return null;
                }
            }
            else
                break;
        }
        LOGGER.info("Suserin tüm favorilenen entryleri çekildi boyutu da ahan da şu : "+result.size());
        return result;
    }

    private void addEntryIdsToList(List<Integer> result, InlineResponse2001 page) {
        for (InlineResponse2001Entries entry : page.getEntries())
            result.add(entry.getEntry().getId());
    }

    private void renewApiKey() {
        userApi.getApiClient().setApiKey(getApiKey());
    }
}
