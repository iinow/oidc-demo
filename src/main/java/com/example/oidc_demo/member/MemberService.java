package com.example.oidc_demo.member;

import com.example.oidc_demo.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public boolean existEmail(String email) {
        return findMemberByEmail(email).isPresent();
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findMember(String uuid) {
        return memberRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("member is not exist, uuid: " + uuid));
    }
}
