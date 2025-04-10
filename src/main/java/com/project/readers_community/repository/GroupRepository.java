package com.project.readers_community.repository;

import com.project.readers_community.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
}
