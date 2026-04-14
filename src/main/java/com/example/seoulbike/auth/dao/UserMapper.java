package com.example.seoulbike.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.seoulbike.auth.model.Signup;
import com.example.seoulbike.auth.model.User;

@Mapper
public interface UserMapper {
	User findByUserId(String userId);

	void insertUser(Signup dto);

	void updateUser(Signup dto);

	void deleteUser(String userId);

	void updatePassword(@Param("userId") String userId, @Param("password") String password);

	String getPassword(String userId);
}
