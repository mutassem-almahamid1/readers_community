package com.project.readers_community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String groupId; // معرف المجموعة
    private String userId; // معرف المستخدم الذي كتب المنشور
    private String content; // محتوى المنشور
    private List<String> likes; // قائمة بمعرفات المستخدمين الذين أعجبوا بالمنشور
    private List<String> comments; // قائمة بمعرفات التعليقات

}