package com.example.seoulbike.auth.dao;

import java.util.List;

import com.example.seoulbike.auth.model.User;
public interface IUserRepository {
    void insertUser(User user); // 회원가입
    User selectUser(String userid); // 로그인
    List<User> selectAllUsers(); // 전체 회원 조회
    void updateUser(User user); // 회원 정보 수정
    void deleteUser(User user); // 회원 탈퇴
    String getPassword(String userid); // 비밀번호 조회

}
