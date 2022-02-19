package nbbang.com.nbbang.domain.party.dto.single.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import java.util.Locale;

@Data
public class PartyStatusChangeRequestDto {
    @ValueOfEnum(enumClass = PartyStatus.class) @Schema(description = "올바른 status 값: OPEN, FULL, CLOSED")
    private String status;

    public PartyStatus createStatus() {
        return PartyStatus.valueOf(status.toUpperCase(Locale.ROOT));
    }
}
