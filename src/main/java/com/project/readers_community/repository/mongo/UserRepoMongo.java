package com.project.readers_community.repository.mongo;

import com.project.readers_community.model.document.Status;
import com.project.readers_community.model.document.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepoMongo extends MongoRepository<User, String> {

    Optional<User> findByIdAndStatus(String id, Status status);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndStatus(String username, Status status);

    List<User> findAllByStatus(Status status);

    Page<User> findAllByStatus(Status status, PageRequest pageRequest);


}
