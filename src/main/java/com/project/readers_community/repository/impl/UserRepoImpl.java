package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.enums.Status;
import com.project.readers_community.model.document.User;
import com.project.readers_community.repository.UserRepo;
import com.project.readers_community.repository.mongo.UserRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public class UserRepoImpl implements UserRepo {

    @Autowired
    private UserRepoMongo repoMongo;

//    @Autowired
//    private BookRepo bookRepo;

    @Override
    public User save(User user) {
        return repoMongo.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return repoMongo.saveAll(users);
    }

    @Override
    public Optional<User> getByIdIfPresent(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public User getById(String id) {
        return repoMongo.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getByIdAndStatusNotDeleted(String id) {
        return repoMongo.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public boolean getByUsernameIfPresent(String username) {
        return repoMongo.findByUsernameAndStatus(username, Status.ACTIVE).isPresent();
    }

    @Override
    public User getByUsername(String username) {
        return repoMongo.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Optional<User> getByUsernameIgnoreStatus(String username) {
        return repoMongo.findByUsername(username);
    }

    @Override
    public Optional<User> getByEmailIgnoreStatus(String email) {
        return repoMongo.findByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        return repoMongo.findByEmailAndStatusNot(email, Status.DELETED)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getByRefreashToken(String refreshToken) {
        return repoMongo.findByRefreshTokenAndStatus(refreshToken, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public boolean getByEmailIfPresent(String email) {
        return repoMongo.findByEmailAndStatus(email, Status.ACTIVE).isPresent();
    }

    @Override
    public List<User> getAll() {
        return repoMongo.findAllByStatusNot(Status.DELETED);
    }

    @Override
    public Page<User> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatusNot(Status.DELETED, pageRequest);
    }

    @Override
    public List<User> getAllByIdIn(List<String> ids) {
        return repoMongo.findAllByIdInAndStatus(ids, Status.ACTIVE);
    }

//    @Override
//    public List<User> getAllFollowingById(String id) {
//        return repoMongo.findAllFollowingById(id);
//    }
//
//    @Override
//    public List<User> getAllFollowersById(String id) {
//        return repoMongo.findAllFollowersById(id);
//    }
//
//
//    @Override
//    public void addBookToFinishedList(String userId, String bookId) {
//        User user = getById(userId);
//        Book book = bookRepo.getById(bookId);
//        if (!user.getFinishedBooks().contains(bookId)) {
//            user.getFinishedBooks().add(bookId);
//            book.setReaderCount(book.getReaderCount() + 1);
//            repoMongo.save(user);
//            bookRepo.save(book);
//        }
//    }
//
//    @Override
//    public void addBookToWantToReadList(String userId, String bookId) {
//        User user = getById(userId);
//        if (!user.getWantToReadBooks().contains(bookId)) {
//            user.getWantToReadBooks().add(bookId);
//            repoMongo.save(user);
//        }
//    }
//
//    @Override
//    public void addBookToCurrentlyReadingList(String userId, String bookId) {
//        User user = getById(userId);
//        if (!user.getCurrentlyReadingBooks().contains(bookId)) {
//            user.getCurrentlyReadingBooks().add(bookId);
//            repoMongo.save(user);
//        }
//    }


    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(User user) {
        repoMongo.delete(user);
    }
}