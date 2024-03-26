package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String introduce;



    @OneToOne(fetch = FetchType.EAGER)  // Many = Many,  User = One
    @JoinColumn(name = "userId")
    private User user; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.

    @Lob
    private byte[] profilePicture;
}