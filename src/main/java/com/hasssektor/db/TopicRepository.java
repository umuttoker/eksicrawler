package com.hasssektor.db;

import com.hasssektor.bean.TopicEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by umut on 4.08.2017.
 */
public interface TopicRepository extends MongoRepository<TopicEntity, String> {

    public TopicEntity findFirstByOrderByIdDesc();
}
