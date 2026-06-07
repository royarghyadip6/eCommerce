package com.ecommerce.user.repository;

import com.ecommerce.user.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I_UsersReposiroty extends MongoRepository<Users, String> {

}
