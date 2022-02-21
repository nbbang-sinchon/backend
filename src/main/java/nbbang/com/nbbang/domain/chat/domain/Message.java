package nbbang.com.nbbang.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity @Getter @Builder
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
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
    private Member sender;

    protected Message() {}

    public static Message createMessage(Member member, Party party, String content, LocalDateTime createTime) {
        return Message.builder()
                .sender(member)
                .party(party)
                .content(content)
                .createTime(createTime)
                .build();
    }

}
