package com.sy.cafe.domain;

import com.sy.cafe.exception.ErrorCode;
import com.sy.cafe.exception.RequestException;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Menu extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    public Menu(String name, Long price) {
        if(name.length() <= 30)
            this.name = name;
        else
            throw new RequestException(ErrorCode.BAD_REQUEST);

        if(price >= 0)
            this.price = price;
        else
            throw new RequestException(ErrorCode.BAD_REQUEST);
    }

    public void update(String name, Long price) {
        if(name.length() <= 30)
            this.name = name;
        else
            throw new RequestException(ErrorCode.BAD_REQUEST);

        if(price >= 0)
            this.price = price;
        else
            throw new RequestException(ErrorCode.BAD_REQUEST);
    }

}
