package com.cos.blog.controller;

import com.cos.blog.service.BoardService;
import com.cos.blog.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // 컨트롤로에서 세션을 어떻게 찾는지?
    // @AuthenticationPrincipal PrincipalDetail principal



    @GetMapping("/profile/updateForm")
    public String updateForm() {
        return "profile/updateForm";
    }

    // USER 권한이 필요
    @GetMapping("/profile/saveForm")
    public String saveForm() {
        return "profile/saveForm";
    }



}
