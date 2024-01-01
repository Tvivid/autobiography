package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Profile;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileApiController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/api/profile")
    public ResponseDto<Integer> save(@RequestBody Profile profile, @AuthenticationPrincipal PrincipalDetail principal) {
        profileService.프로필쓰기(profile, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }



    @PutMapping("/profile")
    public ResponseDto<Integer> update(@RequestBody Profile profile){
        profileService.프로필수정하기(profile);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }


}