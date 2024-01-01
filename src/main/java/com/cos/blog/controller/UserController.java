package com.cos.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static이하에 있는 /js/**, /css/**, /image/**

@Controller
@RequiredArgsConstructor
public class UserController {

	@Value("${cos.key}")
	private String cosKey;


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code){
		RestTemplate rt = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "7338375e54725825fcbef315481e6452");
		params.add("redirect_uri", "http://localhost:8082/auth/kakao/callback");
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
				HttpMethod.POST, // 요청할 방식
				kakaoTokenRequest, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);

		ObjectMapper objectMapper =new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e){
			e.printStackTrace();
		}catch (JsonProcessingException e){
			e.printStackTrace();
		}
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

		RestTemplate rt2 = new RestTemplate();

		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");


		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(params, headers2);

		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
				HttpMethod.POST, // 요청할 방식
				kakaoProfileRequest2, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);


		ObjectMapper objectMapper2 =new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e){
			e.printStackTrace();
		}catch (JsonProcessingException e){
			e.printStackTrace();
		}

		System.out.println("카카오 아이디(번호) : " +kakaoProfile.getId());
		System.out.println("카카오 이메일 : " +kakaoProfile.getKakao_account().getEmail());

		System.out.println("자서전 유저네임 : " +kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
		System.out.println("자서전 이메일 : "+kakaoProfile.getKakao_account().getEmail());
//        UUID garbagePassword = java.util.UUID.randomUUID();
		// UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		//garbagePassword는 값이 계속 바껴서 이걸로 로그인을 시킬 수가 없음
		System.out.println("자서전 패스워드 : " + cosKey);

		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
//                .password(garbagePassword.toString())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();


		// 가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername());

		if(originUser.getUsername() == null) {
			System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다");
			userService.회원가입(kakaoUser);
		}

		System.out.println("자동 로그인을 진행합니다.");
		// 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {	
	  return "user/updateForm";
	}
}
