package com.dksys.biz.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dksys.biz.user.vo.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(String id);
	
	Optional<User> findByEmail(String email);
	
}