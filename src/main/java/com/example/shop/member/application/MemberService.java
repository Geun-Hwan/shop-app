package com.example.shop.member.application;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.application.dto.MemberCommand;
import com.example.shop.member.application.dto.MemberInfo;
import com.example.shop.member.domain.Member;
import com.example.shop.member.domain.MemberRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberService {


    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 목록 조회
    public ResponseEntity<List<MemberInfo>> getUsers(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        List<MemberInfo> members = page.stream().map(MemberInfo::from).toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), members, members.size());
    }

    // 회원 등록
    public ResponseEntity<MemberInfo> createUser(MemberCommand command) {
        Member member = Member.create(command.email(), command.name(), command.password(),
            command.phone(), command.saltKey(), command.flag());
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK.value(), MemberInfo.from(member), 1);
    }

    public ResponseEntity<MemberInfo> modifyUser(String id, MemberCommand command) {

        UUID uuid = UUID.fromString(id);
        Member member = memberRepository.findById(uuid)
            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));

        member.updateInfomation(command.email(), command.name(), command.password(),
            command.phone(), command.saltKey(), command.flag());

        Member updated = memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK.value(), MemberInfo.from(updated), 1);
    }

    // 회원 삭제
    public ResponseEntity<Void> removeUser(String id) {
        memberRepository.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
