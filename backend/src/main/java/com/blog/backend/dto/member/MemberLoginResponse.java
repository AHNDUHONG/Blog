package com.blog.backend.dto.member;

import com.blog.backend.domain.member.Member;
import com.blog.backend.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponse {

    private Long id;
    private String email;
    private String nickname;
    private Role role;

    public static MemberLoginResponse from(Member member) {
        return MemberLoginResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }

}
