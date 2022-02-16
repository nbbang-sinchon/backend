package nbbang.com.nbbang.domain.chat.domain;

import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    private LocalDateTime createTime;

    private Integer readNumber;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isPicture;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

}
