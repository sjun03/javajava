package com.koreait.spring_boot_study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
- DB 테이블과 1:1 매핑된 엔티티가 아니다.
- 기술적인 부분과 상관이 없음 -> Top3를 뽑아내라는 요구사항은 업계의 요구(비즈니스로직)
- 클라이언트가 요구한 것(Top3 판매량 순위 조회)
- 이런 것을 domain model이라고 한다.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Top3SellingProduct {
    private int productId; // product_id
    private String productName; // product_name
    private int totalSoldCount; // total_sold_count(집계함수 결과)
}
