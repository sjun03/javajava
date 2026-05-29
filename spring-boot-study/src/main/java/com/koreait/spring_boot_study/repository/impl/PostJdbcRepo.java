package com.koreait.spring_boot_study.repository.impl;

import com.koreait.spring_boot_study.entity.Post;
import com.koreait.spring_boot_study.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class PostJdbcRepo implements PostRepo {

    private final DataSource dataSource;

    @Autowired
    public PostJdbcRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Post rsToPost(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        Post post = new Post(id, title, content);
        return post;
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

    // 실습) findAllPosts를 작성
    @Override
    public List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT id, title, content FROM post";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Post post = rsToPost(rs);
                posts.add(rsToPost(rs)); // post.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return posts;
    }

    // 실습) findPostById 작성
    @Override
    public Optional<Post> findPostById(int id) {
        String sql = "SELECT id, title, content FROM post WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null; // SELECT 할 경우에만

        try {
            conn = dataSource.getConnection(); // 도로를 깔고
            ps = conn.prepareStatement(sql); // 화물차에 sql문을 실어서 도로에 넣는다.

            // ? 대신에 값을 넣어주세요.
            // sql에서 왼쪽부터 시작해서 1번째 나오는 ?에다가 매개변수로 들어온 id값을 넣어주세요.
            ps.setInt(1, id);

            rs = ps.executeQuery(); // 화물차 출발하고 결과물(rs.ResultSet)을 가져온다.

            while (rs.next()){ // rs(테이블)에 다음 줄이 존재한다면 실행하세요.
                Post targetPost = rsToPost(rs);
                return Optional.of(targetPost); // targetPost를 Optional로 감싸서 리턴
            }
        }  catch (SQLException e){
            e.printStackTrace(); // 콘솔에 에러스택을 모두 출력.
        } finally {
            close(rs); // 결과 반납
            close(ps); // 화물차 반납
            close(conn); // 도로 반밥
        }
        
        return Optional.empty(); // Optional이 비어있다는 것을 명시적으로 리턴
        // 옵셔널.orElseThrow(() -> new 예외클래스() 작동한다.
        // 옵셔널.isEmpty() -> true
        // 옵셔널.isPresent() -> false
    }

    @Override
    public int insertPost(String title, String content) {
        String sql = "INSERT INTO post (title, content) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, content);

            int successCount = ps.executeUpdate();
            return successCount;

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return 0;
    }

    @Override
    public int deletePostById(int id) {
        /*
        entity 클레스 이름은 테이블명을 파스칼케이스로 작성
        컬럼명은 스네이크케이스로 작성
        필드명은 카멜케이스로 작성
         */
        String sql = "DELETE FROM post WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return 0;
    }

    @Override
    public int updatePost(int id, String title, String content) {
        // sql문을 문자열로 작성하는게 위험하다.
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE post ");
        sb.append("SET title = ?, content = ? ");
        sb.append("WHERE id = ?");
        String sql = sb.toString();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, id);
            return ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return 0;
    }
}
