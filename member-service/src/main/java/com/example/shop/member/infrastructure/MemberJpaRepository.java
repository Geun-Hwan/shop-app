package com.example.shop.member.infrastructure;

import com.example.shop.member.domain.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);
}
