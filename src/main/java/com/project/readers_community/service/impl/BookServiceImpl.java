package com.project.readers_community.service.impl;

import com.project.readers_community.mapper.BookMapper;
import com.project.readers_community.mapper.helper.AssistantHelper;
import com.project.readers_community.model.common.MessageResponse;
import com.project.readers_community.model.document.Book;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.model.dto.request.BookRequest;
import com.project.readers_community.model.dto.response.BookResponse;
import com.project.readers_community.repository.BookRepo;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.service.BookService;
import com.project.readers_community.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CategoryService categoryService;

    private final String apiKey = "AIzaSyCCybPucK-_tphMJf6fowwNaLLFw-FY7sE";
    private final String baseUrl = "https://www.googleapis.com/books/v1/volumes";

    @Override
    public List<Book> searchBooksByCategory(String category) {
        String url = baseUrl + "?q=subject:" + category + "&key=" + apiKey + "&maxResults=40";
        BookApiResponse response = restTemplate.getForObject(url, BookApiResponse.class);

        if (response == null || response.getItems() == null) {
            return List.of();
        }

        List<Book> books = Arrays.stream(response.getItems())
                .map(item -> {
                    VolumeInfo volumeInfo = item.getVolumeInfo();
                    return Book.builder()
                            .id(item.getId())
                            .title(volumeInfo.getTitle())
                            .author(Arrays.toString(volumeInfo.getAuthors() != null ? volumeInfo.getAuthors() : new String[]{}))
                            .description(volumeInfo.getDescription())
                            .coverImage(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null)
                            .createdAt(LocalDateTime.now())
                            .status(Status.ACTIVE)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        books.forEach(this::save);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getCreatedAt() == null) book.setCreatedAt(LocalDateTime.now());
        return bookRepo.save(book);
    }


    @Override
    public BookResponse create(BookRequest request) {
        Book book = BookMapper.mapToDocument(request);
        this.categoryService.getById(book.getCategory());
        Book savedBook = bookRepo.save(book);
        return BookMapper.mapToResponse(savedBook);
    }

    @Override
    public BookResponse getById(String id) {
        Book book = bookRepo.getById(id);
        return BookMapper.mapToResponse(book);
    }

    @Override
    public BookResponse getByTitle(String name) {
        Book book = bookRepo.getByTitle(name);
        return BookMapper.mapToResponse(book);
    }

    @Override
    public List<BookResponse> getByAll() {
        List<Book> books = bookRepo.getAll();
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getByAllByIdIn(List<String> ids) {
        List<Book> books = bookRepo.getAllIdIn(ids);
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponse> getByAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books = bookRepo.getAllPage(pageRequest);
        return books.map(BookMapper::mapToResponse);
    }

    @Override
    public MessageResponse update(String id, BookRequest request) {
        Book book = bookRepo.getById(id);
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        book.setCoverImage(request.getCoverImageUrl() != null ? request.getCoverImageUrl().trim() : null);
        if (request.getCategory() != null && !request.getCategory().equals(book.getCategory())) {
            this.categoryService.getById(request.getCategory());
            book.setCategory(request.getCategory());
        }
        book.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return AssistantHelper.toMessageResponse("Book updated successfully.");
    }

    @Override
    public MessageResponse softDeleteById(String id) {
        Book book = bookRepo.getById(id);
        book.setStatus(Status.DELETED);
        book.setDeletedAt(LocalDateTime.now());
        Book updatedBook = bookRepo.save(book);
        return AssistantHelper.toMessageResponse("Book deleted successfully.");
    }

    @Override
    public MessageResponse softDeleteByCategoryId(String id) {
        List<Book> books = bookRepo.getAllByCategory(id);
        books.forEach(book -> {
            book.setStatus(Status.DELETED);
            book.setDeletedAt(LocalDateTime.now());
        });
        bookRepo.saveAll(books);
        return AssistantHelper.toMessageResponse("Books deleted successfully.");
    }

    @Override
    public MessageResponse hardDeleteById(String id) {
        Book book = bookRepo.getById(id);
        bookRepo.deleteById(id);
        return AssistantHelper.toMessageResponse("Book deleted successfully.");
    }

    @Override
    public List<BookResponse> getByCategory(String category) {
        List<Book> books = bookRepo.getAllByCategory(category);
        return books.stream()
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse updateBookReviewAndRating(String bookId, int reviewCount, double ratingTotal) {
        Book book = bookRepo.getById(bookId);
        book.setReviewCount(reviewCount);
        book.setAvgRating(reviewCount > 0 ? (ratingTotal / reviewCount) : 0);
        bookRepo.save(book);
        return AssistantHelper.toMessageResponse("Review updated successfully.");
    }

    @Override
    public List<BookResponse> getTopRatedBooks() {
        // هذا مثال بسيط، قد تحتاج إلى منطق أكثر تعقيدًا للتقييم
        // على سبيل المثال، يمكنك استخدام صيغة تجمع بين التقييم وعدد القراء وعدد المراجعات
        List<Book> books = bookRepo.getAll();
        return books.stream()
                .sorted(Comparator.comparing(Book::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Book::getReaderCount, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Book::getReviewCount, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getPersonalizedRecommendations(String userId) {
        User user = userRepo.getById(userId);
        List<String> finishedBookIds = user.getFinishedBooks();

        if (finishedBookIds == null || finishedBookIds.isEmpty()) {
            return getTopRatedBooks(); // إذا لم يقرأ المستخدم أي كتب، أرجع أفضل الكتب
        }

        Set<String> readBookCategories = new HashSet<>();
        for (String bookId : finishedBookIds) {
            Book book = bookRepo.getByIdIfPresent(bookId).orElse(null);
            if (book != null && book.getCategory() != null) {
                readBookCategories.add(book.getCategory());
            }
        }

        if (readBookCategories.isEmpty()) {
            return getTopRatedBooks(); // إذا لم يتم العثور على فئات، أرجع أفضل الكتب
        }

        List<Book> recommendedBooks = new ArrayList<>();
        for (String categoryId : readBookCategories) {
            // ابحث عن الكتب في هذه الفئة التي لم يقرأها المستخدم بعد
            // قد تحتاج إلى دالة مخصصة في BookRepo لهذا الغرض
            List<Book> booksInCategory = bookRepo.getAllByCategory(categoryId);
            for (Book book : booksInCategory) {
                if (!finishedBookIds.contains(book.getId())) {
                    recommendedBooks.add(book);
                }
            }
        }

        // إزالة التكرارات وفرز النتائج (مثلاً حسب التقييم)
        return recommendedBooks.stream()
                .distinct()
                .sorted(Comparator.comparing(Book::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getTrendingBooksThisMonth() {
        // يتطلب هذا منطقًا لتحديد "الرواج".
        // أحد الطرق هو البحث عن المراجعات التي تم إنشاؤها هذا الشهر.
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        // قد تحتاج إلى دالة مخصصة في ReviewRepo للحصول على المراجعات ضمن نطاق زمني
        // List<Review> reviewsThisMonth = reviewRepo.findAllByCreatedAtBetweenAndStatus(startOfMonth, endOfMonth, Status.ACTIVE);
        // ثم قم بتجميع الكتب من هذه المراجعات وفرزها حسب عدد المراجعات أو أي معيار آخر.

        // تنفيذ مبسط مؤقت: إرجاع الكتب الأعلى تقييمًا كعنصر نائب
        // يجب استبدال هذا بمنطق التتبع الفعلي
        List<Book> books = bookRepo.getAll();
        return books.stream()
                .sorted(Comparator.comparing(Book::getReviewCount, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Book::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10) // على سبيل المثال، أفضل 10 كتب رائجة
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getFriendRecommendations(String userId) {
        User currentUser = userRepo.getById(userId);
        List<String> friendIds = currentUser.getFollowing();

        if (friendIds == null || friendIds.isEmpty()) {
            return new ArrayList<>(); // لا يوجد أصدقاء، لا توجد توصيات
        }

        Set<String> recommendedBookIds = new HashSet<>();
        for (String friendId : friendIds) {
            User friend = userRepo.getByIdIfPresent(friendId).orElse(null);
            if (friend != null && friend.getFinishedBooks() != null) {
                recommendedBookIds.addAll(friend.getFinishedBooks());
            }
        }

        // إزالة الكتب التي قرأها المستخدم الحالي بالفعل
        if (currentUser.getFinishedBooks() != null) {
            recommendedBookIds.removeAll(currentUser.getFinishedBooks());
        }

        if (recommendedBookIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> recommendedBooks = bookRepo.getAllByIdIn(new ArrayList<>(recommendedBookIds));

        // يمكنك فرز هذه الكتب بناءً على عدد الأصدقاء الذين قرأوها أو تقييمها العام
        return recommendedBooks.stream()
                .sorted(Comparator.comparing(Book::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(BookMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    private static class BookApiResponse {
        private Item[] items;

        public Item[] getItems() {
            return items;
        }
    }
    private static class Item {
        private String id;
        private VolumeInfo volumeInfo;
        private Status status=Status.ACTIVE;
        private LocalDateTime createdAt;

        public String getId() {
            return id;
        }

        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }
    }

    private static class VolumeInfo {
        private String title;
        private String[] authors;
        private String description;
        private ImageLinks imageLinks;

        public String getTitle() {
            return title;
        }

        public String[] getAuthors() {
            return authors;
        }

        public String getDescription() {
            return description;
        }

        public ImageLinks getImageLinks() {
            return imageLinks;
        }
    }

    private static class ImageLinks {
        private String thumbnail;

        public String getThumbnail() {
            return thumbnail;
        }
    }
}
