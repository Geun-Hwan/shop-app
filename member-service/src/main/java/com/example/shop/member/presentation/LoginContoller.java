package com.example.shop.member.presentation;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.application.MemberService;
import com.example.shop.member.presentation.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.v1}")
public class LoginContoller {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody LoginRequest request){


        return memberService.login(request);
    }

    @GetMapping("/authorizations/check")
    public Boolean check(@RequestParam("httpMethod") String httpMethod, @RequestParam("requestPath") String requestPath){
        return memberService.check(httpMethod, requestPath);
    }


    @GetMapping("/refresh/token")
    public ResponseEntity<HashMap<String, Object>> refreshToken(@RequestHeader("Refresh-Token")String token){
        return memberService.refreshToken(token);
    }
}
