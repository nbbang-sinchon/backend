package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.bbangpan.domain.SendStatus;

@Data
@Builder
public class PartyMemberFieldChangeSocketDto {
    private Long changeMemberId;
    private String field;
    private Object value;

    public static PartyMemberFieldChangeSocketDto createDto(String field, Object value, Long changeMemberId) {
        return PartyMemberFieldChangeSocketDto.builder()
                .changeMemberId(changeMemberId)
                .field(field)
                .value(value)
                .build();
    }
}
