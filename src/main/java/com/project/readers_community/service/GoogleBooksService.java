package com.project.readers_community.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.readers_community.entity.Book;
import com.project.readers_community.dto.GoogleBooksResponse;
import com.project.readers_community.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleBooksService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${google.books.api.key}")
    private String apiKey;

    /**
     * جلب كتاب من Google Books API بناءً على استعلام معين
     * @param query استعلام البحث (مثل العنوان أو ISBN)
     * @param addedBy معرف المستخدم الذي يضيف الكتاب
     * @return الكتاب المحفوظ أو الموجود مسبقًا
     */
    public Book fetchBookFromGoogle(String query, String addedBy) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            GoogleBooksResponse response = objectMapper.readValue(jsonResponse, GoogleBooksResponse.class);
            if (response.getItems() != null && !response.getItems().isEmpty()) {
                GoogleBooksResponse.VolumeInfo volumeInfo = response.getItems().get(0).getVolumeInfo();

                // إنشاء كائن Book جديد
                Book book = new Book();
                book.setTitle(volumeInfo.getTitle());
                book.setAuthor(volumeInfo.getAuthors() != null ? String.join(", ", volumeInfo.getAuthors()) : "غير معروف");
                book.setCategory(volumeInfo.getCategories() != null ? volumeInfo.getCategories().get(0) : null); // التصنيف الرئيسي
                book.setDescription(volumeInfo.getDescription());
                book.setCategories(volumeInfo.getCategories() != null ? volumeInfo.getCategories() : new ArrayList<>());
                book.setPublisher(volumeInfo.getPublisher());
                book.setPublishedDate(parsePublishedDate(volumeInfo.getPublishedDate()));
                book.setIsbn(getIsbnFromIdentifiers(volumeInfo.getIndustryIdentifiers()));
                book.setCoverImage(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null);
                book.setAddedBy(addedBy); // تعيين معرف المستخدم الذي أضاف الكتاب
                book.setReviewCount(0); // القيمة الافتراضية
                book.setAvgRating(0.0); // القيمة الافتراضية

                // التحقق مما إذا كان الكتاب موجودًا مسبقًا باستخدام ISBN
                Book existingBook = bookRepository.findByIsbn(book.getIsbn());
                if (existingBook == null) {
                    return bookRepository.save(book);
                }
                return existingBook;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // إرجاع null إذا لم يتم العثور على كتاب
    }

    /**
     * تحويل تاريخ النشر من String إلى LocalDate
     */
    private LocalDate parsePublishedDate(String publishedDate) {
        if (publishedDate != null && !publishedDate.isEmpty()) {
            try {
                // Google Books قد يعيد التاريخ بصيغ مختلفة (مثل "2023" أو "2023-05-01")
                if (publishedDate.length() == 4) {
                    return LocalDate.parse(publishedDate + "-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
                }
                return LocalDate.parse(publishedDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * استخراج ISBN من قائمة المعرفات
     */
    private String getIsbnFromIdentifiers(List<GoogleBooksResponse.IndustryIdentifier> identifiers) {
        if (identifiers != null) {
            for (GoogleBooksResponse.IndustryIdentifier identifier : identifiers) {
                if ("ISBN_13".equals(identifier.getType())) {
                    return identifier.getIdentifier();
                } else if ("ISBN_10".equals(identifier.getType())) {
                    return identifier.getIdentifier();
                }
            }
        }
        return null;
    }
}