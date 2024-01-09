package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
    Page<Board> findByUser(User user, Pageable pageable);
    Page<Board> findBySecretTrue(Pageable pageable);


}
