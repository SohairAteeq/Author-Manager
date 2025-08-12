package com.sohair.journalApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Journal {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
}
