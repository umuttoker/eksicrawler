package com.hasssektor.db;

import com.hasssektor.bean.InstaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by umut on 6.08.2017.
 */
public interface InstaDataRepository extends MongoRepository<InstaData, String> {

    @Query(value = "{}", fields = "{id : 1}")
    List<InstaData> findAllUserNick();

}
