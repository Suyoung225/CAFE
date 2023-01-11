package com.sy.cafe.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Menu(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Menu(String name, Long price) {
        this.name = name;
        if(price >= 0)
            this.price = price;
    }

    public void update(String name, Long price) {
        this.name = name;
        if(price >= 0)
            this.price = price;
    }

}
