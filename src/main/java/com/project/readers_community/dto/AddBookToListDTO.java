package com.project.readers_community.dto;

import lombok.Data;

@Data
public class AddBookToListDTO {
    private String bookTitle;   // عنوان الكتاب بدلاً من المعرف
    private String listType;    // نوع القائمة: "wantToRead", "currentlyReading", "finishedReading"

    public AddBookToListDTO(String bookTitle, String listType) {
        this.bookTitle = bookTitle;
        this.listType = listType;
    }

    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getListType() {
        return listType;
    }
    public void setListType(String listType) {
        this.listType = listType;
    }



}