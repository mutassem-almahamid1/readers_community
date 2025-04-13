package com.project.readers_community.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    private String id;
    private String name;
    private String description;
    private Status status;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

}
