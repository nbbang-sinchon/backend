package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
public class ChatAlarmPartyDto {

    private Long id;
    private String title;
    public static ChatAlarmPartyDto createByParty(Party party) {
        return ChatAlarmPartyDto.builder().id(party.getId()).title(party.getTitle()).build();
    }
}
