package com.gcp.spring.mysql.repository;

import com.gcp.spring.mysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, CrudRepository<User, String>{

}

