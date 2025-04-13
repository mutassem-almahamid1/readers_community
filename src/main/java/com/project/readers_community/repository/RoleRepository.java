package com.project.readers_community.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRoleName(String roleName);
}
