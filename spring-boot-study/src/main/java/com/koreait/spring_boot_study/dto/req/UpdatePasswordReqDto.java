package com.koreait.spring_boot_study.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePasswordReqDto {

    @NotBlank(message = "현재 비밀번호를 입력하세요")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력하세요")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 8자 이상, 영문/숫자/특수문자 포함이어야 합니다"
    )
    private String newPassword;
}