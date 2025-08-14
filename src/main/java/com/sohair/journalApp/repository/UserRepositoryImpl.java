package com.sohair.journalApp.repository;

import com.sohair.journalApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findAllUsers() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(
                "^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9._%+-]+(?:\\.[A-Za-z0-9._%+-]+)*@" +
                        "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$",
                "i"
        ));
        query.addCriteria(Criteria.where("sentimentAnalysisEnabled").is(true));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
