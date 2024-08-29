package com.project.blogerse.repo;

import com.project.blogerse.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String userName);
    void deleteByUserName(String name);
}
