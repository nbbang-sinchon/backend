package nbbang.com.nbbang.domain.party.domain;

import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity @Getter
public class Party {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;

    private Integer goalNumber;

    @Enumerated(STRING)
    private Status status;

    private String place;

    private LocalDateTime cancelTime;

    private Integer deliveryFee;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member owner;

    private Boolean isBlocked;

}
