package com.koreait.spring_boot_study.controller;

import com.koreait.spring_boot_study.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/practice")
@Slf4j
@RestController
// 스프링부트는 톰캣(자바언어로 만든 서버)을 내장
// 로컬에서 8080포트로 실행된다.
// localhost:8080 -> 스프링부트 주소
// localhost:8080/practice/add -> add 메서드 실행
// localhost:8080/practice/add?num1=10&num2=20
// localhost:8080/practice/average?num1=10&num2=20&num3=30&num4=40
public class StudyRestController2 {

    // 실습) 컨트롤러 2개 만들어주세요.
    // 1. 쿼리스트링으로 숫자 2개를 받아서 더한 결과를 응답하는 컨트롤러

    @GetMapping ("/add")
    public int add( int num1, int num2){
        log.info("number 컨트롤러 수신");
        log.info("들어온 데이터: {}, {}", num1, num2);
        return num1 + num2;
    }
    // 2. 쿼리스트링으로 숫자 4개를 받아서 평균을 응답하는 컨트롤러
    // localhost:8080/practice/average?num1=10&num2=20&num3=30&num4=40
    @GetMapping("/average")
    public double average(int num1, int num2, int num3, int num4){
        log.info("average 컨트롤러 수신");
        log.info("들어온 데이터: {}, {}, {}, {}", num1, num2, num3, num4);
        return (num1 + num2 + num3 + num4) / 4.0;
    }
    /* 3. 포스트맨 응답으로 
    [
        {
            "name": "홍길동",
            "address": ["부산시", "연제구]"
         },
         {
            "name": "고길동",
            "address": "부산시 부산진구"
         },
         
    ]
    */
    @GetMapping("/profiles")
    public List<?> profile(){
        log.info("profile 수신");
        List<Map<String, Object>> profileData = new ArrayList<>();
        Map<String, Object> data1 = Map.of(
                "name","홍길동",
                "address", "부산시 연제구"
        );

        Map<String, Object> data2 = Map.of(
                "name","고길동",
                "address", "부산시 부산진구"
        );
        
        profileData.add(data1);
        profileData.add(data2);
        return profileData;
    }

    @GetMapping("/profiles2")
    public List<?> profile2(){
        log.info("profile2 수신");
        return List.of(
                Map.of(
                        "name","홍길동",
                        "address", "부산시 연제구"
                ),
                Map.of(
                        "name","고길동",
                        "address", "부산시 부산진구"
                )
        );
    }

    // 게시물 조회 컨트롤러를 완성해 주세요.
    @GetMapping("/post/{id}")
    public Map<String, Object> getPost(@PathVariable("id") int id) {
        List<Post> postList = List.of(
                new Post(1, "페이커 그는 신인가", "ㅇㅈ?"),
                new Post(2, "구마유시 그는 신인가", "ㅇㅈ?"),
                new Post(3, "케리아 그는 신인가", "ㅇㅈ?"),
                new Post(4, "오너 그는 신인가", "ㅇㅈ?"),
                new Post(4, "도란 그는 신인가", "ㅇㅈ?")
        );

        //Optional -> null일수도 있는 값을 포장한 컨테이너클래스
        Optional<Post> optionalPost = postList.stream() // List -> stream
                .filter(post -> post.getId() == id) // id가 같은 것 제외 모두 삭제
                .findFirst(); // 처음 찾은 것을 가져오세요. 타입(옵셔널)

        if(optionalPost.isEmpty()){ // 옵셔널이 가지고 있는 값이 null이라면
            return Map.of("error", "해당 id의 게시글은 존재하지 않습니다.");
        }
        Post target = optionalPost.get(); // 옵셔널에 포장된 실제값 꺼내기
        return Map.of("success", target);
    }

    @GetMapping("/post/title/{keyword}")
    public Map<String, Object> searchByTitle(@PathVariable("keyword") String keyword){
        List<Post> postList = List.of(
                new Post(1, "페이커 그는 신인가", "ㅇㅈ?"),
                new Post(2, "구마유시 그는 신인가", "ㅇㅈ?"),
                new Post(3, "케리아 그는 신인가", "ㅇㅈ?"),
                new Post(4, "오너 그는 신인가", "ㅇㅈ?"),
                new Post(5, "도란 그는 신인가", "ㅇㅈ?")
        );

        List<Post> resultTitle = postList.stream()
                .filter(post -> post.getTitle().contains(keyword))
                .collect(Collectors.toList());
        if (resultTitle.isEmpty()) {
            return Map.of("error", "해당 키워드를 포함하는 게시글이 없습니다.");
        }

        // 여러 건이 나올 수 있으므로 리스트 그대로 반환
        return Map.of("success", resultTitle);
    }
}
