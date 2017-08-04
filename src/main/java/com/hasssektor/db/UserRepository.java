package com.hasssektor.db;

import io.swagger.client.model.InlineResponse200;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by umut on 4.08.2017.
 */
public interface UserRepository extends MongoRepository<InlineResponse200, String> {

    public InlineResponse200 findByUserInfo_UserIdentifier_Nick(String firstName);

}