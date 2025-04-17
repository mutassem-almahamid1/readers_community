package com.project.readers_community.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String avatar;
    private List<String> members; // قائمة بمعرفات المستخدمين
    private List<String> posts; // قائمة بمعرفات المنشورات

}