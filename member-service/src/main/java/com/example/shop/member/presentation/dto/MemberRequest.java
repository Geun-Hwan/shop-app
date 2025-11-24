package com.example.shop.member.presentation.dto;

import com.example.shop.member.application.dto.MemberCommand;

public record MemberRequest(String email, String name, String password, String phone) {

    public MemberCommand toCommand() {

        return new MemberCommand(email, name, password, phone);
    }
}