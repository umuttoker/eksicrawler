package com.hasssektor.db;

import com.hasssektor.bean.TwitterData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by umut on 10.08.2017.
 */
public interface TwitterRepository extends MongoRepository<TwitterData, String> {

    @Query(value = "{}", fields = "{id : 1}")
    List<TwitterData> findAllUserIds();
}
