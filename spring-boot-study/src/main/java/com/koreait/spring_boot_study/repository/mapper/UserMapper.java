package com.koreait.spring_boot_study.repository.mapper;

import com.koreait.spring_boot_study.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    // 회원가입 - User 엔티티 전달
    int addUser(User user);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(int userId);

    // 마이페이지용 추가
    int updateUserInfo(@Param("userId") int userId,
                       @Param("name") String name,
                       @Param("email") String email);
    int updatePassword(@Param("userId") int userId,
                       @Param("password") String password);
}