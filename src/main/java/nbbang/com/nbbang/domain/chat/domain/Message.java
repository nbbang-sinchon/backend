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
public class Message implements Comparable<Message> {
    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createTime;

    private Integer notReadNumber;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member sender;

    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    protected Message() {}

    public static Message createMessage(Member sender, Party party, String content, MessageType type, Integer notReadNumber) {
        return Message.builder()
                .sender(sender)
                .party(party)
                .content(content)
                .type(type)
                .notReadNumber(notReadNumber)
                .build();
    }

    public static Message createMessage(Member member, Party party, String content, LocalDateTime createTime) {
        return Message.builder()
                .sender(member)
                .party(party)
                .content(content)
                .createTime(createTime)
                .type(MessageType.CHAT)
                .build();
    }


    public int compareTo(Message o) {
        if (this.id < o.getId()) {
            return -1;
        } else if (this.id > o.getId()) {
            return 1;
        }
        return 0;
    }


}
