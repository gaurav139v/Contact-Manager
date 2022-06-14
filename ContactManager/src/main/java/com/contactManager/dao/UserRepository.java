package com.contactManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactManager.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
