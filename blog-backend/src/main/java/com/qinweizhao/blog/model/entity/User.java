package com.qinweizhao.blog.model.entity;

import com.qinweizhao.blog.utils.DateUtils;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * User entity
 *
 * @author ryanwang
 * @date 2019-03-12
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * User name.
     */
    @Column(name = "username", length = 50, nullable = false)
    private String username;

    /**
     * User nick name,used to display on page.
     */
    @Column(name = "nickname", nullable = false)
    private String nickname;

    /**
     * Password.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User email.
     */
    @Column(name = "email", length = 127)
    private String email;

    /**
     * User avatar.
     */
    @Column(name = "avatar", length = 1023)
    private String avatar;

    /**
     * User description.
     */
    @Column(name = "description", length = 1023)
    private String description;

    /**
     * Expire time.
     */
    @Column(name = "expire_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;


    @Override
    public void prePersist() {
        super.prePersist();

        if (email == null) {
            email = "";
        }

        if (avatar == null) {
            avatar = "";
        }

        if (description == null) {
            description = "";
        }

        if (expireTime == null) {
            expireTime = DateUtils.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
