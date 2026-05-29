package com.koreait.spring_boot_study.controller;

import com.koreait.spring_boot_study.dto.req.AddPostReqDto;
import com.koreait.spring_boot_study.dto.req.StudyReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/study3")
@Slf4j
public class StudyRestController3 {
    /*
    HTTP 요청(GET, POST, PUT, PATCH, DELETE)
    GET 요청을 제외한 모든 요청메서드에는 body가 존재한다.
    body의 특징
    - url과 무관하게 데이터를 송신가능 -> 보안이 좋다.
    JSON을 body에 담아서 송신
    */

    /*
    POST -> http://localhost:8080/study3/test1
    BODY -> raw

    {
    "data1": "데이터1",
    "data2": "데이터2"
    }
     */
    @PostMapping("/test1")
    public String test1(@RequestBody Map<String, Object> data){
        // @RequestBody -> body에 작성되어 있는 json 데이터를 알아서 자바객체로 바꿔준다.
        // 참고) Jackson 라이브러리가 개입해서 자동으로 변환해준다.
        log.info("test1 컨트롤러 수신");
        log.info("들어온 데이터: {}", data);
        return "성공";
    }

    @PostMapping("/test2")
    public String test2(@RequestBody StudyReqDto dto){
        // @RequestBody -> body에 작성되어 있는 json 데이터를 알아서 자바객체로 바꿔준다.
        // 참고) Jackson 라이브러리가 개입해서 자동으로 변환해준다.
        // 주의) 필드명과 JSON key 이름이 동일해야한다.
        log.info("test2 컨트롤러 수신");
        log.info("들어온 데이터: {}", dto);
        return "성공";
    }

    // 실습) test3라는 이름으로 AddPostReqDto 타입의 데이터를 수신
    /*
    {
    "title": "제목",
    "content": "내용"
    }
     */
    @PostMapping("/test3")
    public ResponseEntity<?> test3(@RequestBody AddPostReqDto dto2){
        // ResponseEntity: HTTP 응답을 자바에서 커스터마이징하기 편하게 만드 클래스
        // 제너릭타입을 받는다 -> body에 들어가는 데이터타입
        // HTTP 상태코드, body, header 등을 쉽게 지정할 수 있다.
        log.info("test3 컨트롤러 수신");
        log.info("돌아온 데이터: {}", dto2);
        /*
        HTTP 상태코드
        200 -> 성공
        400 -> 요청을 잘못했다.
        500 -> 서버가 잘못했다.
        200: 성공 OK, 201: 생성 성공 CREATED
        400: 잘못된 요청 BAD_REQUEST, 401: 인증실패 UNAUTHORIZED
        403: 권한없음 FORBIDDEN, 404: 리소스가 없다(주소 입력 잘못됨) NOT_FOUND
        500: 서버 내부 오류(코드 문제, 예외처리 불량, DB에러가 서버까지 올라온 경우)
         */
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }
    /*
    - 어차피 RequestBody로 id를 전달하면 되지 않나?
    - 기술적으로는 RequestBody로 id까지 객체로 데이터를 받아 코딩 가능
    - RESTful 설계
        - URL: "어떤 자원을 다룰 것인가?"(식별) -> id
        - METHOD: "뭘 할 건가?"(행위) -> put(수정)
        - body: "어떤 데이터로?"(내용) -> 내가보낸 dto로 바꿔줘
    */
    // localhost:8080/study3/test4/1
    @PutMapping("/test4/{id}")
    public ResponseEntity<?> test4(@PathVariable int id, @RequestBody StudyReqDto dto){
        log.info("test4 컨트롤러 수신");
        log.info("돌아온 데이터: id = {}, dto = {}", id, dto);
        return ResponseEntity.ok("성공"); // 상태코드 OK + body
    }

    // PUT, PATCH -> 둘 다 수정하겠다는 의미
    // PUT -> 들어온 dto 데이터로 모두 바꾸겠다.
    // PATCH -> 들어온 dto 데이터 중 일부만 반영하겠다.
    
    //DELETE -> body를 사용할 수 있는데, 보통 사용하지 않음
    @DeleteMapping("/test5")
    public ResponseEntity<?> test5(@PathVariable int id){
        log.info("test5 컨트롤러 수신");
        log.info("돌아온 데이터: id = {}", id);
        return ResponseEntity.ok("삭제 완료!");
    }
}
