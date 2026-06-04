package com.ecommerce.user.repository;

import com.ecommerce.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I_UsersReposiroty extends JpaRepository<Users, Long> {

}
