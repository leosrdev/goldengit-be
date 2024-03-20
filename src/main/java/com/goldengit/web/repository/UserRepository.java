package com.goldengit.web.repository;

import com.goldengit.web.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
