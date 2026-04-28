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

    private String accessToken;
    private String tokenType;

    public static MemberLoginResponse of(Member member, String accessToken) {
        return MemberLoginResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
