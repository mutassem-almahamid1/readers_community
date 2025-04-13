package com.project.readers_community.dto.book_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTORequest {
    private String title;
    private String author;
    private String category;
    private String description;
    private List<String> categories = new ArrayList<>();
    private String publisher;
    private LocalDate publishedDate;
    private String coverImage;
    private String addedBy;
    private String isbn;
}
