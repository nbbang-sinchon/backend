package nbbang.com.nbbang.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;


// deprecated in favor of in memory operation
@Entity
@Getter
@Builder
@AllArgsConstructor
public class ChatSession {
    @Id
    @GeneratedValue
    private Long id;

    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    protected ChatSession() {}

}