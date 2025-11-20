package com.example.shop.member.infrastructure;

import com.example.shop.member.domain.Member;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, UUID> {

}
