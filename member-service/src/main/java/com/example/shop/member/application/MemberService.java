package com.example.shop.member.application;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.application.dto.LoginAuthentication;
import com.example.shop.member.application.dto.MemberCommand;
import com.example.shop.member.application.dto.MemberInfo;
import com.example.shop.member.domain.Member;
import com.example.shop.member.domain.MemberRepository;
import com.example.shop.member.presentation.dto.LoginRequest;
import com.example.shop.member.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원 목록 조회
    public ResponseEntity<List<MemberInfo>> getUsers(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        List<MemberInfo> members = page.stream().map(MemberInfo::from).toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), members, members.size());
    }

    // 회원 등록
    public ResponseEntity<MemberInfo> createUser(MemberCommand command) {
        String encodedPwd = passwordEncoder.encode(command.password());
        Member member = Member.create(command.email(), command.name(), encodedPwd,
            command.phone());
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK.value(), MemberInfo.from(member), 1);
    }

    public ResponseEntity<MemberInfo> modifyUser(String id, MemberCommand command) {
        UUID uuid = UUID.fromString(id);
        Member member = memberRepository.findById(uuid)
            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));

        String encodedPwd =
            StringUtils.hasText(command.password()) ? passwordEncoder.encode(command.password())
                : member.getPassword();

        member.updateInfomation(command.email(), command.name(), command.password(),
            command.phone());

        Member updated = memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK.value(), MemberInfo.from(updated), 1);
    }

    // 회원 삭제
    public ResponseEntity<Void> removeUser(String id) {
        memberRepository.deleteById(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }

    public ResponseEntity<HashMap<String, Object>> login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.email()).orElse(null);
        HashMap<String, Object> map = new HashMap<>();
        if (member != null) {
            if (passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
                Authentication authentication = new LoginAuthentication(member.getId().toString(),
                    null);

                map.put("Access-Token", jwtProvider.generateToken(authentication));
                map.put("Refresh-Token", jwtProvider.generateRefreshToken(authentication));

                return new ResponseEntity<>(HttpStatus.OK.value(), map, 1);

            } else {
                throw new IllegalArgumentException("pwd ");
            }
        }

        return null;
    }

    public ResponseEntity<HashMap<String, Object>> refreshToken(String token) {

//        String token =request.getHeader("Refresh-Token");
        String subject = jwtProvider.getUserDataFromJwt(token);

        UUID id = UUID.fromString(subject);

        Member member = memberRepository.findById(id).orElse(null);

        HashMap<String,Object> map = new HashMap<>();


        if (member!=null){
            Authentication authentication = new LoginAuthentication(subject,null);
            map.put("Access-Token", jwtProvider.generateToken(authentication));

        }


        return new ResponseEntity<>(HttpStatus.OK.value(), map, 1);


    }

    public Boolean check(String httpMethod, String requestPath) {
        return true;
    }

}
