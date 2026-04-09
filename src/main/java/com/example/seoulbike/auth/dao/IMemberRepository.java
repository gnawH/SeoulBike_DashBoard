package com.example.seoulbike.auth.dao;

import java.util.List;

import com.example.seoulbike.auth.model.Member;

public interface IMemberRepository {
    void insertMember(Member member); // 회원가입 (관리자만?)
    Member selectMember(String userid); // 로그인
    List<Member> selectAllMembers(); // 전체 회원 조회 (관리자만?)
    void updateMember(Member member); // 회원 정보 수정 (유저 가능)
    void deleteMember(Member member); // 회원 탈퇴 (관리자만)
    String getPassword(String userid); // 비밀번호 조회

}
