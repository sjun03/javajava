package com.koreait.spring_boot_study.diAndIoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/*
@Component 역할을 하는 어노테이션들
1. @Component  -> 특별한 역할 X
2. @RestController -> HTTP 요청/응답 처리
3. @Repository -> DB와의 작업을 담당
5. @Configuration -> 직접 Bean을 등록하는 설정클래스
 */
@Service
public class IocService {
    /*
    스프링부트에서는 객체생성(new) / 의존성주입(di)의 주도권이 내가 아니라 프레임워크(스프링부트)에 있다.
    언제 생성되는지, 소멸되는지 스프링부트가 결정한다. -> Inversion Of Control(IOC)

    스프링부트에는 IOC 컨테이너라는게 존재한다.
    이 컨테이너에 어노테이션(@Component)가 붙은 클래스들을 싱글톤 객체로 생성해서 보관한다.

    1. SpringApplication.run() 실행시, Ioc 컨테이너 객체를 생성한다.(싱글톤)
    2. 컴포넌트스캔(@Component 붙은 클래스 탐색)
    // 탐색범위 - run()한 곳이 포함된 패키지를 기준으로 하위 전체.
    3. 스캔이 끝나면, 그 클래스들로 부터 싱글톤 객체를 생성한다. : Bean이라고 부른다. -- 시점(1)
    4. 생성한 Bean들을 Ioc 컨테이너에 보관
    5. 필요한 곳에 해당 Bean을 주입 -- 시점(2)
     */
    
    @Autowired // 생성자 시점(1)에서 주입
    public IocService(IocRepository iocRepository){
        this.iocRepository = iocRepository;
    }
    /*
    순환 참조 (A, B라는 Bean을 예시로)
    A는 B를 필드로 가지고 있음.
    B도 A를 필드로 가지고 있음.
    A를 만들려고 하니 B가 필요하네? B를 만들자
    B를 만들려고 하니 A가 필요하네? A를 만들자 - 무한 반복
    시점(1)이 좋은 이유 -> 미리 탐지 가능, 예방 가능
     */

    // @Autowired // 시점(2)에서 주입
    private IocRepository iocRepository;

    // 총점 구하는 메서드
    public int getTotal(){
        List<Integer> scores = iocRepository.getScores();
        int total = 0;
        for(Integer score : scores){
            total += score;
        }
        return total;
    }

    // 평균 구하는 메서드
    public double getAverage(){
        List<Integer> scores = iocRepository.getScores();
        double avg = (double) getTotal() / scores.size();
        return avg;
    }

    private static IocService instance;

    private IocService(DiRepository diRepository) {
        this.iocRepository = iocRepository;
    }

    // 외부에서 getInstance를 호출헤서 DiRepository를 주입
    public static IocService getInstance(DiRepository diRepository){
        if(instance == null){
            instance = new IocService(diRepository);
        }
        return instance;
    }
}
