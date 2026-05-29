package com.koreait.spring_boot_study.diAndIoc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DiController {
    // Controller : 요청 수신 / 응답 송신 관련 코드 작성
    // -> Service : 비즈니스(핵심)로직 / 트랜잭션 관리
    // -> Repository : DB 연결 / DB와 관련된 코드 작성

    @GetMapping("di")
    public ResponseEntity<?> diTest(){
        // diTest() 컨트롤러는 DiService 객체에 의존하고 있다.
        // diService는 DiRepository 객체에 의존하고 있다.
        // 이 의존성을 내가 직접 코드로 컨트롤하고 있다 -> DI를 직접하고 있다.

        // getInstance()를 직접 호출함으로서,
        // 내가 직접 객체를 new하는 효과 -> 객체 생성도 직접 컨트롤 하고 있다.
        DiRepository diRepository = DiRepository.getInstance();
        DiService diService = DiService.getInstance(diRepository);

        int totalScore = diService.getTotal();
        double avgScore = diService.getAverage();

        Map <String, Object> resMap = Map.of(
                "총점", totalScore,
                "평균", avgScore
        );

        return ResponseEntity.ok(resMap);
    }
}
