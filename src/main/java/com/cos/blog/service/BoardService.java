package com.cos.blog.service;

import com.cos.blog.JWT.JwtTokenProvider;
import com.cos.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final JwtTokenProvider jwtTokenProvider;

	private final UserRepository userRepository;

	
//	public BoardService(BoardRepository bRepo, ReplyRepository rRepo) {
//		this.boardRepository = bRepo;
//		this.replyRepository = rRepo;
//	}

	@Transactional
	public void 글쓰기(Board board, String token) { // title, content


		String username = jwtTokenProvider.getAuthentication(token).getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("cannot find user"));
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findBySecretTrue(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Board> 내가쓴글목록(Pageable pageable, String token) {
		String username = jwtTokenProvider.getAuthentication(token).getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("cannot find user"));


		return boardRepository.findByUser(user, pageable);
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
				});
	}

	@Transactional(readOnly = true)
	public long 내가쓴글갯수(String token){
		String username = jwtTokenProvider.getAuthentication(token).getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("cannot find user"));
		return boardRepository.countByUser(user);
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		System.out.println("글삭제하기 : "+id);
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
				}); // 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		board.update();
		// 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동 업데이트가 됨. db flush
	}
	


}
