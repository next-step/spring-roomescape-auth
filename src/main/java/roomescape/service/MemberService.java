package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.entities.Member;
import roomescape.repositories.MemberRepository;
import roomescape.ui.data.SignupRequest;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  public void save(SignupRequest signupRequest){
    memberRepository.save(
      signupRequest.getName(), signupRequest.getEmail(), signupRequest.getEmail(), signupRequest.getRole());
  }

  public Member findMemberByEmail(String email){
    return memberRepository.findByEmail(email);
  }
}
