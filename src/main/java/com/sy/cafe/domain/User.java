package com.sy.cafe.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long point;

    public User(String nickname) {
        this.nickname = nickname;
        this.point = 0L;
    }

    public Long chargePoint(Long point){
        this.point += point;
        return this.point;
    }

    public Long usePoint(Long point){
        this.point -= point;
        return this.point;
    }

}
