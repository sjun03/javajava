package com.koreait.spring_boot_study.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    private int commentId;
    private String commentContent;

    // private int postId;
    private Post post; // FK 대신에 객체를 필드로 가지고 있어야 한다.
    // comment.getPost().getId(); -> 계속된 참조로 탐색하는 것 (객체 그래프 탐색)
    
    // 하나의 글에는 여러개의 댓글이 있다.
    // -> 1개의 Post는 여러개의 Comment를 가질 수 있다.
    // 1. FK는 누가 가지고 있는가? comment가 PostId(FK)를 가지고 있어야 한다.
    // 2. 하나의 댓글은 하나의 글에만 달릴 수 있다.
    // -> Post : Comment = 1 : N 관계
    
    /*
    테이블 설계 노하우
    FK를 설정하는 순간 -> 1 : N(FK를 가진 테이블) 관계를 만들겠다는 것

    N : M 관계도 설정 가능함
    학생 : 강의 -> 학생이 강의 FK, 강의도 학생 FK (N : M 관계)
    "등록" 테이블 생성(학생 FK, 강의 FK)
    학생 : 등록(1 : N), 강의 : 등록 (1 : M)
    -> 이론적으로 모든 테이블을 1 : N 관계로 설정 가능하다.
     */
}
