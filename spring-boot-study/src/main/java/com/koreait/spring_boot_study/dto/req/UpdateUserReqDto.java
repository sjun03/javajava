package com.koreait.spring_boot_study.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserReqDto {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
}