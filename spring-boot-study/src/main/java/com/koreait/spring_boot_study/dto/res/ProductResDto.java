package com.koreait.spring_boot_study.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class ProductResDto {
    private int id;
    private String name;
    private int price;
}