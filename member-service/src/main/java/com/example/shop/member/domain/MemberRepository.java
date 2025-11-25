package com.example.shop.member.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findById(UUID id);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    void deleteById(UUID id);
}