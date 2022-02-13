package nbbang.com.nbbang.domain.party.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data @Builder
public class PartyListResponseDto {
    List<PartyFindResponseDto> parties;

    public static PartyListResponseDto createMock() {
        List<String> hashtags = Arrays.asList("BHC", "치킨");
        PartyFindResponseDto partyFindResponseDto = new PartyFindResponseDto("연희동 올리브영 앞 BHC 7시", LocalDateTime.now(), 3, 4, "마감 임박", hashtags);
        List<PartyFindResponseDto> collect = Arrays.asList(partyFindResponseDto);
        return PartyListResponseDto.builder()
                .parties(collect)
                .build();
    }
}
