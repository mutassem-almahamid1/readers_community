package com.project.readers_community.service;

import com.project.readers_community.entity.Book;
import com.project.readers_community.entity.User;
import com.project.readers_community.repository.BookRepository;
import com.project.readers_community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getRecommendations(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // جمع العناوين من قوائم القراءة
        Set<String> userBookTitles = new HashSet<>();
        userBookTitles.addAll(user.getWantToRead());
        userBookTitles.addAll(user.getCurrentlyReading());
        userBookTitles.addAll(user.getFinishedReading());

        // جلب الكتب بناءً على العناوين
        List<Book> userBooks = bookRepository.findByTitleIn(userBookTitles);
        Set<String> userCategories = userBooks.stream()
                .flatMap(book -> book.getCategories().stream())
                .collect(Collectors.toSet());

        // جلب كتب بناءً على التصنيفات (باستثناء الكتب التي يمتلكها المستخدم)
        List<Book> recommendedBooks = bookRepository.findByCategoriesIn(userCategories)
                .stream()
                .filter(book -> !userBookTitles.contains(book.getTitle())) // مقارنة بالعناوين
                .collect(Collectors.toList());

        // إضافة كتب من المتابعين (إذا كان هناك متابعون)
        List<User> following = userRepository.findAllById(user.getFollowing());
        Set<String> followingBookTitles = following.stream()
                .flatMap(f -> {
                    Set<String> titles = new HashSet<>();
                    titles.addAll(f.getWantToRead());
                    titles.addAll(f.getCurrentlyReading());
                    titles.addAll(f.getFinishedReading());
                    return titles.stream();
                })
                .filter(title -> !userBookTitles.contains(title))
                .collect(Collectors.toSet());

        recommendedBooks.addAll(bookRepository.findByTitleIn(followingBookTitles));

        // ترتيب حسب متوسط التقييم
        recommendedBooks.sort((b1, b2) -> Double.compare(b2.getAvgRating(), b1.getAvgRating()));

        return recommendedBooks.stream().limit(10).collect(Collectors.toList());
    }
}