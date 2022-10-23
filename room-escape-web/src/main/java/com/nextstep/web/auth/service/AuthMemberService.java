package com.nextstep.web.auth.service;

import com.nextstep.web.auth.JwtTokenProvider;
import com.nextstep.web.auth.TokenRequest;
import com.nextstep.web.auth.TokenResponse;
import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import nextstep.common.BusinessException;
import nextstep.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthMemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthMemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = validate(request);
        String token = jwtTokenProvider.createToken(member.getId().toString(), null);
        return new TokenResponse(token);
    }

    private Member validate(TokenRequest request) {
        MemberEntity memberEntity = memberDao.findByUsername(request.getUsername()).orElseThrow(()->
                new BusinessException(""));

        Member member = memberEntity.fromThis();
        member.validatePassword(request.getPassword());
        return member;
    }
}
