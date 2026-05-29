package com.koreait.spring_boot_study.repository.impl;

import com.koreait.spring_boot_study.entity.Product;
import com.koreait.spring_boot_study.model.Top3SellingProduct;
import com.koreait.spring_boot_study.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Qualifier("jdbc")
@Repository
public class ProductJdbcRepo implements ProductRepo {

    // DB 경로 or 비밀번호와 같이 민감한 정보들을 소스코드로 노출되지 않게
    // yaml에 적어둔 DB 설정값을 스프링이 자동으로 읽어서
    // 그 값을 가진 DataSource 객체를 자동으로 만들어 Bean으로 등록해준다.
    private final DataSource dataSource;

    @Autowired
    public ProductJdbcRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private void close(AutoCloseable ac){
        // conn, ps, rs -> AutoCloseable이라는 인터페이스를 이식받고 있음
        // 그 인터페이스에서 close라는 추상메서드가 있음
        if(ac != null) {
            try {
                ac.close();
            } catch (Exception ig) { }
        }
    }

    public List<Product> findAllProducts(){
        // 리턴해줄 List
        List<Product> products = new ArrayList<>();
        // DB로 SQL 전송 / 응답
        // DB와 실제 연결을 수행하는 객체
        Connection conn = null;
        // Connection의 필드로 주입되어서 DB로 전송될 SQL 객체
        PreparedStatement ps = null;
        // DB에서 가져온 데이터를 자바에서 읽기 좋은 형태(자바 객체)로 제공하는 객체
        // SELECT할 때만 필요 -> 테이블을 결과로 받을때만 필요
        ResultSet rs = null;

        String sql = "SELECT product_id, product_name, product_price FROM product";

        // DB에서 올라오는 에러를 try-catch로 잡아줘야한다.
        try {
            // DB에서 제공하는 연결을 하나 대여해온다.
            conn = dataSource.getConnection();
            // prepareStatement에는 실제 문자열로 SQL 쿼리가 들어가야한다.
            ps = conn.prepareStatement(sql);
            // 작성한 prepareStatement를 DB에 전달하고, 실행시킨 결과를 가져온다.
            // DB에서 조회한 결과를 rs안에 테이블형태로 들고 온다고 보면 된다.
            rs = ps.executeQuery(); // SELECT -> executeQuery(): rs를 리턴
            // rs.next()는 테이블에서 한줄씩 읽어올건데, 그 다음 줄이 존재하는지 검사하는 메서드
            while (rs.next()){
                // next가 true면 해당 줄의 컬럼 값들을 가져올 수 있음
                // product_id 컬럼값을 읽어 오세요.
                int id = rs.getInt("product_id");
                // product_name 컬럼값을 읽어 오세요.
                String name = rs.getString("product_name");
                // product_price 컬럼값을 읽어 오세요.
                int price = rs.getInt("product_price");

                Product product = new Product(id, name, price);
                products.add(product);
            }
        } catch (SQLException e){
            // String SQL -> SQL을 잘 못 작성하였거나, DB 에러처리
            System.out.println(e.getStackTrace()); // DB에러들을 출력
        } finally {
            // 에러가 일어나도, 에러가 일어나지 않아도 실행되는 코드블럭
            // 대여했던 객체들을 반납
            // 순서 중요!) rs -> ps -> conn 순서로 close
            close(rs);
            close(ps);
            close(conn);
        }

        return products;
    }

    public String findProductNameById(int id){
        String sql = "SELECT product_name FROM product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            // 문자열 왼쪽부터 쭉 스캔해서 1번째로 나오는 ?에다가 매개변수로 들어온 id값을 넣으세요.
            // sql injection 공격 방지
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("product_name");
            }

        } catch (SQLException e){
            System.out.println(e.getStackTrace());
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return "해당 id의 상품은 존재하지 않습니다.";
    }

    @Override
    public int insertProduct(String name, int price) {
        String sql = "INSERT INTO product (product_name, product_price) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            
            // sql 문자열 왼쪽부터 스캔해서 1번째 나오는 ?에 매개변수 name값 주입
            ps.setString(1, name);
            // sql 문자열 왼쪽부터 스캔해서 2번째 나오는 ?에 매게변수 price값 주입
            ps.setInt(2, price);
            
            int successCount = ps.executeUpdate(); // 영향받은 ROW의 수
            return successCount;
        } catch (SQLException e){
            e.printStackTrace();
        }  finally {
            close(ps);
            close(conn);
        }

        return 0;
    }

    @Override
    public int deleteProductById(int id) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate(); // 쿼리로 영향받은 ROW 수를 DB가 리턴
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return 0;
    }

    @Override
    public int updateProduct(int id, String name, int price) {
        // StringBuilder 사용 -> 기존 객체를 변형하는 것
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE product");
        sb.append("SET product_name = ?, product_price = ?");
        sb.append("WHERE product_id = ?");
        String sql = sb.toString();

        // 문자열 덧셈 -> 계속해서 새로운 객체를 생성
        // 문자열도 결국 객체(참조자료형)
        // String c = a + b -> c는 a, b와는 무관한 독립적인 새로운 객체
        // String sql = "UPDATE product SET product_name = ?, product_price = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setInt(3, id);

            return ps.executeUpdate(); // 단건이라 1 리턴될 것 / id가 이상하면 0 리턴
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return 0;
    }

    @Override
    public List<Top3SellingProduct> findTop3SellingProducts() {
        List<Top3SellingProduct> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.product_id, p.product_name, SUM(od.quantity) AS 'total_sold_count' ");
        sb.append("FROM product p ");
        sb.append("JOIN order_details od ");
        sb.append("ON p.product_id = od.product_id ");
        sb.append("GROUP BY p.product_id, p.product_name ");
        sb.append("ORDER BY total_sold_count DESC ");
        sb.append("LIMIT 3");

        String sql = sb.toString();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                int totalSoldCount = rs.getInt("total_sold_count");

                result.add(new Top3SellingProduct(id, name, totalSoldCount));
            }

            return result;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return List.of();
    }
}
