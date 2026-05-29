package com.koreait.spring_boot_study.repository.mapper;

import com.koreait.spring_boot_study.dto.req.SearchProductReqDto;
import com.koreait.spring_boot_study.dto.res.SearchProductResDto;
import com.koreait.spring_boot_study.entity.Product;
import com.koreait.spring_boot_study.model.Top3SellingProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


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

        Mybatis 내부 구현에 대한 간략한 이해
        - Mapper(interface: 추상체) --- dynamicProxy(Mybatis가 알아서) --- XML(실제 구현체)
        - 1. 서비스는 mapper interface만 알고 있음, 주입받고 있다.
        - 2. 실제로 Ioc 컨테이너에서 주입해주는 것은 mapper 인터페이스가 아니라, dynamicProxy 객체
        - 3. dynamicProxy 객체를 Mybatis가 XML을 보고 생성 & Bean 등록을 함
         */

    // 컨트롤러 -> 서비스 -> 레포지토리
    // 1. 다건조회(전체 조회)
    List<Product> findAllProducts();

    // 2. 단건조회(상품 하나만 조회)
    // id로 상품이름 단건 조회
    String findProductNameById(int id);

    // 상품 추가
    int insertProduct(
            @Param("name") String name, 
            @Param("price") int price
    );
    // @Param -> XML에서 매개변수 이름을 전달할 때 사용
    // 매개변수들을 HashMap 형태로 가져가게됨.
    // 우리가 @Param에 작성하는 것은 key 값이다.
    // XML에서는 해당 Key 값을 적어줘서 value값들을 동적으로 처리
    // @Param을 적어주지 않으면, 컴파일러 옵션에 따라서 작동할 때도 있고, 안될 때도 있다.
    // -> 매개변수가 2개 이상일 경우, 적어주는 것을 권장함.


    // 단건 삭제
    // id를 통해서 단건을 삭제하는 메서드
    int deleteProductById(int id);

    // 단건 업데이트
    int updateProduct(@Param("id") int id,
                      @Param("name") String name,
                      @Param("price") int price);

    // JOIN 결과 받아옴.
    // Top 3 상품 조회
    public List<Top3SellingProduct> findTop3SellingProducts();

    // productId로 판매량까지 같이 조회
    Product findProductWithQuantities(int productId);

    // 상품 상세 조회, 상품 이름, 최소가격, 최대가격 필터링 검색
    // WHERE product_name Like '% + {product_name} + %'
    List<Product> searchDetailProducts(
            @Param("nameKeyword") String nameKeyword,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    int insertProducts(List<Product> products);
}


