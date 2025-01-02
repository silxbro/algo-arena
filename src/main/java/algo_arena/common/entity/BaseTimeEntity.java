package algo_arena.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.sql.Timestamp;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Timestamp createdDate;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp lastModifiedDate;
}