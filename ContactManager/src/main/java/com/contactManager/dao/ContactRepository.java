package com.contactManager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactManager.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
	public Page<Contact> getContactByUserId(@Param("userId") Integer userId, Pageable pageable);

	@Query("SELECT c FROM Contact c WHERE c.phone = :phone")
	public Contact getContactByPhone(String phone);
	
}
