package com.example.shop.service;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.Member;
import com.example.shop.member.MemberRepository;
import com.example.shop.member.MemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {


    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 목록 조회
    public ResponseEntity<List<Member>> getUsers() {
        List<Member> members = memberRepository.findAll();
        return new ResponseEntity<>(HttpStatus.OK.value(), members, members.size());
    }

    // 회원 등록
    public ResponseEntity<Member> createUser(MemberRequest request) {
        Member member = new Member(
                UUID.randomUUID(),
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );

        return new ResponseEntity<>(HttpStatus.OK.value(), memberRepository.save(member), 1);
    }

    public ResponseEntity<Member> modifyUser(String id, MemberRequest request) {
        Member member = new Member(
                UUID.fromString(id),
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );

        return new ResponseEntity<>(HttpStatus.OK.value(), memberRepository.save(member), 1);
    }

    // 회원 삭제
    public ResponseEntity<Void> removeUser(String id) {
        memberRepository.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
