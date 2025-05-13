package com.project.readers_community.model.dto.response;

import com.project.readers_community.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private String id;
    private String userId;
//    private String username;
    private String bookId;
//    private String bookTitle;
    private String content;
    private int rating;
    private Set<String> likedBy;
    private int likeCount;
//    private boolean likedByCurrentUser; // هل المستخدم الحالي أعجب بالمراجعة
//    private List<String> commentsId;
    private Status status;
    private LocalDateTime createdAt;
}