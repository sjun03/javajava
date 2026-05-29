package com.koreait.spring_boot_study.repository;

import com.koreait.spring_boot_study.entity.Post;

import java.util.List;
import java.util.Optional;


public interface PostRepo {

    // 전체게시글 조회 구현, 글제목 조회
    public List<Post> findAllPosts();
    // 게시글 단건 조회 구현, 글제목 조회
    public Optional<Post> findPostById(int id);
    // 단건 추가
    public int insertPost(String title, String content);
    // 단건 삭제
    public int deletePostById(int id);
    // 단건 업데이트
    public int updatePost(int id, String title, String content);
}
