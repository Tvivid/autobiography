<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form>
		<div class="form-group">
			<label for="username">Username</label> 
			<input type="text" class="form-control" placeholder="Enter username" id="username">
		</div>
		
		<div class="form-group">
			<label for="password">Password</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		
		<div class="form-group">
			<label for="email">Email</label> 
			<input type="email" class="form-control" placeholder="Enter email" id="email">
		</div>
		
	</form>
	<button id="btn-save" class="btn btn-primary">회원가입완료</button>
	<a href="https://kauth.kakao.com/oauth/authorize?client_id=7338375e54725825fcbef315481e6452&redirect_uri=http://localhost:8082/auth/kakao/callback&response_type=code"><img height="38px" src="\image\kakao_login_button.png"/></a>

</div>

<script src="/js/user.js"></script>



