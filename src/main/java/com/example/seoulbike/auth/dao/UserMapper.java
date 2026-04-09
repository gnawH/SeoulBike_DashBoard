package com.example.seoulbike.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.seoulbike.auth.model.SignupDto;
import com.example.seoulbike.auth.model.User;

@Mapper
public interface UserMapper {
	User findByUserId(String userId);
	void insertMember(SignupDto dto);
	}

