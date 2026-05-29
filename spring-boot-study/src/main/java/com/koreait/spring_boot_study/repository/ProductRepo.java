package com.koreait.spring_boot_study.repository;

import com.koreait.spring_boot_study.entity.Product;
import com.koreait.spring_boot_study.model.Top3SellingProduct;

import java.util.List;


public interface ProductRepo {

    // 컨트롤러 -> 서비스 -> 레포지토리
    // 1. 다건조회(전체 조회)
    List<Product> findAllProducts();

    // 2. 단건조회(상품 하나만 조회)
    // id로 상품이름 단건 조회
    String findProductNameById(int id);

    // 상품 추가
    int insertProduct(String name, int price);

    // 단건 삭제
    // id를 통해서 단건을 삭제하는 메서드
    int deleteProductById(int id);

    // 단건 업데이트
    int updateProduct(int id, String name, int price);
    
    // JOIN 결과를 받아옴
    // 판매량 기준 Top 3 받아옴

    public List<Top3SellingProduct> findTop3SellingProducts();


}
