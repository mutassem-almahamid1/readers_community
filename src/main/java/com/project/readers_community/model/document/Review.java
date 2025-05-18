package com.project.readers_community.model.document;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    private String id;
    private String user;
    private String book;
    private String content;  // optional
    private int rating;  // required
    @Builder.Default
    private Set<String> likedBy = new HashSet<>(); // المستخدمون الذين أعجبوا بالمراجعة
    private int likeCount;

    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
//    private List<String> comments = new ArrayList<>(); // قائمة التعليقات على المراجعة
}




