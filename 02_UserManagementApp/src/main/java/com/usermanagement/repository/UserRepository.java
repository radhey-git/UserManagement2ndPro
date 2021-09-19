package com.usermanagement.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usermanagement.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Serializable >{

	public User findByUserEmailAndUserPwd(String email,String pwd);
	
}


