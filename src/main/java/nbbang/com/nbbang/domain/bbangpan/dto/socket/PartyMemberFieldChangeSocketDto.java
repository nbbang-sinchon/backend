package nbbang.com.nbbang.domain.bbangpan.dto.socket;

import lombok.Builder;
import lombok.Data;

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
