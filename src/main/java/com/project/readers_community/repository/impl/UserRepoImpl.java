package com.project.readers_community.repository.impl;

import com.project.readers_community.handelException.exception.NotFoundException;
import com.project.readers_community.model.document.Status;
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
    public Optional<User> getByUsernameIfPresent(String username) {
        return repoMongo.findByUsernameAndStatus(username, Status.ACTIVE).or(() -> {;
            throw new NotFoundException("User not found");
        });
    }


    @Override
    public Optional<User> getByUsername(String username) {
        return repoMongo.findByUsername(username).or(() -> {;
            throw new NotFoundException("User not found");
        });
    }

    @Override
    public List<User> getAll() {
        return repoMongo.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Page<User> getAllPage(PageRequest pageRequest) {
        return repoMongo.findAllByStatus(Status.ACTIVE, pageRequest);
    }

    @Override
    public List<User> getAllFollowingById(String id) {
        return repoMongo.findAllFollowingById(id);
    }

    @Override
    public List<User> getAllFollowersById(String id) {
        return repoMongo.findAllFollowersById(id);
    }

    @Override
    public void deleteById(String id) {
        repoMongo.deleteById(id);
    }

    @Override
    public void delete(User user) {
        repoMongo.delete(user);
    }
}