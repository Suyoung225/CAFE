package com.sy.cafe.global;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public abstract class CreatedTimeEntity {
    @CreatedDate
    private LocalDateTime createdTime;
}
