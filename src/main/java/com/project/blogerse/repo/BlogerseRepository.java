package com.project.blogerse.repo;

import com.project.blogerse.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogerseRepository extends MongoRepository<BlogEntity,
        String> {}
