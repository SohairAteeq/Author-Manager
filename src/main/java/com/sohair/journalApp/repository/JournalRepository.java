package com.sohair.journalApp.repository;

import com.sohair.journalApp.model.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestBody;

public interface JournalRepository extends MongoRepository<Journal, ObjectId> {
    public void deleteById(ObjectId id);

}
