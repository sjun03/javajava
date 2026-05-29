package com.koreait.spring_boot_study.controller;

import com.koreait.spring_boot_study.dto.req.UpdatePasswordReqDto;
import com.koreait.spring_boot_study.dto.req.UpdateUserReqDto;
import com.koreait.spring_boot_study.dto.res.UserResDto;
import com.koreait.spring_boot_study.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // SecurityContext에서 userId 추출하는 헬퍼 메서드
    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Integer.parseInt(authentication.getName());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        int userId = getCurrentUserId();
        UserResDto resDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyInfo(
            @RequestBody @Valid UpdateUserReqDto reqDto
    ) {
        int userId = getCurrentUserId();
        userService.updateUserInfo(userId, reqDto);
        return ResponseEntity.ok("정보 수정 완료");
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody @Valid UpdatePasswordReqDto reqDto
    ) {
        int userId = getCurrentUserId();
        userService.updatePassword(userId, reqDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }


}