package nbbang.com.nbbang.domain.bbangpan.dto.socket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartyFieldChangeSocketDto {
    private String field;
    private Object value;

    public static PartyFieldChangeSocketDto createDto(String field, Object value) {
        return PartyFieldChangeSocketDto.builder()
                .field(field)
                .value(value)
                .build();
    }
}
