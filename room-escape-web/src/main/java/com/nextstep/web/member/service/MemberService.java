package com.nextstep.web.member.service;

import com.nextstep.web.auth.JwtTokenProvider;
import com.nextstep.web.common.LoginMember;
import com.nextstep.web.member.dto.MemberRequest;
import com.nextstep.web.member.repository.MemberDao;
import nextstep.common.BusinessException;
import nextstep.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member read(LoginMember loginMember) {
        return memberDao.findById(Long.valueOf(loginMember.getId())).orElseThrow(() ->
                new BusinessException("")).fromThis();
    }
}
