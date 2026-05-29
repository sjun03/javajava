package com.koreait.spring_boot_study.repository.mapper;

import com.koreait.spring_boot_study.entity.Product;
import com.koreait.spring_boot_study.model.Top3SellingProduct;

import java.util.List;

// XML 파일과 1:1 매칭되는 자바파일
// XML을 통해 DB에서 가져온 결과(rs)를 자바 객체로 가져오는 심부름 역할
@Mapper
public interface ProductMapper { 
        /*
        1. conn, ps, rs try-catch-finally
        -> 이런 코드들이 통째로 보일러 플레이트 코드다.
           이런 코드는 자동완성이 되었으면 좋겠다. (캡슐화)
           : 개발자는 SQL만 신경썻으면 좋겠다.
        
        2. SQL을 String 자료형으로 작성했었음.
        -> JAVA랑 SQL은 독립적인데 왜 JAVA 코드로 작성해야하는가?
           SQL이 길어지면, JAVA 코드가 어지러워진다. (JAVA와 분리)
           : JAVA 파일 말고, XML로 분리시키겠다.

        3. JDBC에서 사용하던 rsToProduct() 메서드를 자동으로 지원하겠다.
        -> 객체간 참고(그래프 탐색)을 지원하겠다.
           DB의 테이블과 1:1 대응되는 것이 entity -> fk 컬럼을 id필드로 가지고 있음
           객체지향적(그래프 탐색) entity -> fk 컬럼을 객체자체를 필드로 가지고 있음. (연관관계 설정)
         */

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

        public List<Top3SellingProduct> findTop3SellingProducts();
}
