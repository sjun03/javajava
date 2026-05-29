package com.koreait.spring_boot_study.diAndIoc;

import java.util.List;

public class DiService {
    // 싱글톤 적용

    private DiRepository diRepository;

    // 총점 구하는 메서드
    public int getTotal(){
        List<Integer> scores = diRepository.getScores();
        int total = 0;
        for(Integer score : scores){
            total += score;
        }
        return total;
    }

    // 평균 구하는 메서드
    public double getAverage(){
        List<Integer> scores = diRepository.getScores();
        double avg = (double) getTotal() / scores.size();
        return avg;
    }

    private static DiService instance;

    private DiService(DiRepository diRepository) {
        this.diRepository = diRepository;
    }

    // 외부에서 getInstance를 호출헤서 DiRepository를 주입
    public static DiService getInstance(DiRepository diRepository){
        if(instance == null){
            instance = new DiService(diRepository);
        }
        return instance;
    }
}
