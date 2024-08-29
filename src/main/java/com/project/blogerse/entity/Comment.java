package com.project.blogerse.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
@NoArgsConstructor
public class Comment {

    @Id
    private String id;

    private String content;

    @DBRef
    private BlogEntity blog; // Reference to the BlogEntity

    private String author;

    private LocalDateTime createdAt = LocalDateTime.now();
}
