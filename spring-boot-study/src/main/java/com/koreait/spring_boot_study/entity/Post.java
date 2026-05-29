package com.koreait.spring_boot_study.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Post { // 테이블명: post -> 자바: Post
    private int id; // 컬럼명: post_id -> 자바: PostId
    private String title; // 컬럼명: post_title -> 자바: PostTitle
    private String content; // 컬럼명: post_content -> 자바: PostContent

    public Post(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /*
        PK를 FK로 들고 있는 쪽이 N이다.
        Post : Comment = 1 : N
    */

    private List<Comment> comments;
}
