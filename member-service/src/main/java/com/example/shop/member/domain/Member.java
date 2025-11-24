package com.example.shop.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "\"member\"", schema = "public")
@NoArgsConstructor
public class Member {

    @Id
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "\"name\"", length = 20)
    private String name;

    @Column(name = "\"password\"", nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "reg_id", nullable = false)
    private UUID regId;

    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "modify_id", nullable = false)
    private UUID modifyId;

    @Column(name = "modify_dt", nullable = false)
    private LocalDateTime modifyDt;

    @Column(name = "saltkey", nullable = false, length = 14)
    private String saltKey;

    @Column(name = "flag", length = 20)
    private String flag;

    private Member(UUID id, String email, String name, String password, String phone,
        String saltKey, String flag) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.saltKey = saltKey;
        this.flag = flag;
    }

    public static Member create(String email, String name, String password, String phone
        ) {
        String saltKey= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return new Member(UUID.randomUUID(), email, name, password, phone, saltKey, "");
    }

    public void updateInfomation(String email, String name, String password, String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;

    }

    @PrePersist
    public void prePersist() {
        if (regId == null) {
            regId = id != null ? id : UUID.randomUUID();
        }
        if (modifyId == null) {
            modifyId = regId;
        }
        if (regDt == null) {
            regDt = LocalDateTime.now();
        }
        if (modifyDt == null) {
            modifyDt = LocalDateTime.now();
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @PreUpdate
    public void preUpdate() {
        modifyDt = LocalDateTime.now();
        if (modifyId == null) {
            modifyId = id;
        }
    }

}
