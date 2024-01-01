<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<button class="btn btn-secondary" onclick="history.back()">돌아가기</button>


		<a href="/profile/${profile.id}/updateForm" class="btn btn-warning">수정</a>
		<button id="btn-delete" class="btn btn-danger">삭제</button>

	<br /> <br />
	<div>
		글 번호 : <span id="id"><i>${board.id} </i></span> 작성자 : <span><i>${board.user.username} </i></span>
	</div>
	<br />
	<div>
		<h3>${board.title}</h3>
	</div>
	<hr />
	<div>
		<div>${board.content}</div>
	</div>
	<hr />

	<div class="card">
	    <form>
	        <input type="hidden" id="userId" value="${principal.user.id}" />
		    <input type="hidden" id="boardId" value="${board.id}" />
			<div class="card-body">
				<textarea id="reply-content" class="form-control" rows="1"></textarea>
			</div>
			<div class="card-footer">
				<button type="button" id="btn-reply-save" class="btn btn-primary">등록</button>
			</div>
		</form>
	</div>
	<br />

</div>
<script src="/js/board.js"></script>