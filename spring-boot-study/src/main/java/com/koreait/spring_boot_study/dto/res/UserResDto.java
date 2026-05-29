package com.koreait.spring_boot_study.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResDto {
    private int userId;
    private String username;
    private String name;
    private String email;
    private String roleName;
    }