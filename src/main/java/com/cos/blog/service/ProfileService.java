package com.cos.blog.service;

import com.cos.blog.model.Board;
import com.cos.blog.model.Profile;
import com.cos.blog.model.User;
import com.cos.blog.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private ProfileRepository profileRepository;


    @Transactional
    public void 프로필쓰기(Profile profile, User user) { // title, content
        profile.setUser(user);
        profileRepository.save(profile);
    }



    @Transactional(readOnly = true)
    public Profile 프로필보기(int id) {
        return profileRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("프로필보기 실패 : 아이디를 찾을 수 없습니다.");
                });
    }



    @Transactional
    public void 프로필수정하기(Profile requestProfile) {
        Profile profile = profileRepository.findById(requestProfile.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
                }); // 영속화 완료
        profile.setNickname(requestProfile.getNickname());
        profile.setIntroduce(requestProfile.getIntroduce());
    }



}




