package com.koreait.spring_boot_study.repository;

import com.koreait.spring_boot_study.entity.Product;

import java.util.List;

// Product 테이블을 SELECT한 결과
public class ResultSetSample {
    // 필드로 전체 ROW(객체)들을 담은 List를 가지고 있음
    private List<Product> products;
    // 현재 조회중인 List의 index
    // 최초) 조회하지 않았을 때 -1, 조회 시작 시 index 0부터 시작
    private int cursor =  -1;

    // DB에 저장된 컬럼명과 동일한 필드명을 가지고 있다.
    // -> Map 형태로 key로 컬럼명, value로 해당 ROW의 컬럼값을 저장하고 있음.
    private int product_id;
    private String product_name;
    private int product_price;

    public boolean next(){
        if(cursor + 1 < products.size()) {
            cursor++;

            // next() 메서드가 호출될 때마다
            // List 한칸씩 이동하면서 해당 객체의 필드값들을 가져온다.
            Product product = products.get(cursor);
            this.product_id = product.getId();
            this.product_name = product.getName();
            this.product_price = product.getPrice();

            return true;
        }

        return false;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    /*
    ResultSet re
    while(rs.next()) {
    rs.getProduct_id() -> 테이블에 기록되어있던 product_id
    rs.getProduct_name() -> 테이블에 기록되어있던 product_name
    rs.getProduct_price() -> 테이블에 기록되어있던 product_price
    }
     */
}
