package com.koreait.spring_boot_study.service;

import com.koreait.spring_boot_study.dto.req.UpdatePasswordReqDto;
import com.koreait.spring_boot_study.dto.req.UpdateUserReqDto;
import com.koreait.spring_boot_study.dto.res.UserResDto;
import com.koreait.spring_boot_study.entity.User;
import com.koreait.spring_boot_study.exception.UserException;
import com.koreait.spring_boot_study.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResDto getUserInfo(int userId) {
        User user = userMapper.getUserById(userId)
                .orElseThrow(() -> new UserException(
                        "사용자를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        return UserResDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    @Transactional
    public void updateUserInfo(int userId, UpdateUserReqDto reqDto) {
        User user = userMapper.getUserById(userId)
                .orElseThrow(() -> new UserException(
                        "사용자를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        // 이메일 중복 체크 (본인 이메일이 아닌 경우만)
        if(!user.getEmail().equals(reqDto.getEmail())) {
            userMapper.getUserByEmail(reqDto.getEmail())
                    .ifPresent(existUser -> {
                        throw new UserException(
                                "이미 사용중인 이메일입니다.",
                                HttpStatus.BAD_REQUEST
                        );
                    });
        }

        userMapper.updateUserInfo(userId, reqDto.getName(), reqDto.getEmail());
    }

    @Transactional
    public void updatePassword(int userId, UpdatePasswordReqDto reqDto) {
        User user = userMapper.getUserById(userId)
                .orElseThrow(() -> new UserException(
                        "사용자를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        // 기존 비밀번호 확인
        if(!passwordEncoder.matches(reqDto.getCurrentPassword(), user.getPassword())) {
            throw new UserException(
                    "현재 비밀번호가 일치하지 않습니다.",
                    HttpStatus.BAD_REQUEST
            );
        }

        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(reqDto.getNewPassword());
        userMapper.updatePassword(userId, encodedPassword);
    }
}