package uos.cineseoul.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "USERS")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_NUM")
    private Long userNum;

    /* Foreign Key */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false, insertable = false, updatable = false)
    private List<Ticket> tickets;
    /* */

    @Column(name="ID", nullable = true, unique = false, length = 100)
    private String id;

    @Column(name="PW", nullable = true, unique = false, length = 500)
    private String pw;

    @Column(name="NAME", nullable = true, unique = false, length = 100)
    private String name;

    @Column(name="RESIDENT_NUM", nullable = false, length = 13)
    private String residentNum;

    @Column(name = "PHONE_NUM", nullable = false, length = 11)
    private String phoneNum;

    @Column(name = "POINT", nullable = true)
    private Integer point;

    @Column(name = "ROLE", nullable = false, length = 1)
    private String role;

    @CreationTimestamp
    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}
