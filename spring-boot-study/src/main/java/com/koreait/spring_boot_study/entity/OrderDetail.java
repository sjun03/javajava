package com.koreait.spring_boot_study.entity;

import com.koreait.spring_boot_study.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {
    private int orderDetailId;
    private int orderId; // FK
    // private int productId; // FK
    private int quantity;

    // 연관 관계를 가지려면, FK 대신 객체를 가지고 있으면 된다.
    // FK(다른 테이블의 PK)를 칼럼으로 가지고 있다. -> 하나의 product는 여러개의 orderDetail을 가질 수 있다.
    private Product product;
}
