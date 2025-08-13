package com.sohair.journalApp.repository;

import com.sohair.journalApp.model.JournalConfigEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigJournalAppRepository extends MongoRepository<JournalConfigEntity, String> {

}
