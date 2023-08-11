package com.sy.cafe.menu.domain;

import com.sy.cafe.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Menu extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Long price;

    public Menu(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public void update(String name, Long price) {
        this.name = name;
        this.price = price;
    }

}
