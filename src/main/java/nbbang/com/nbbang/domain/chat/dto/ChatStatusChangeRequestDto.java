package nbbang.com.nbbang.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

@Data
public class ChatStatusChangeRequestDto {
    @ValueOfEnum(enumClass = PartyStatus.class) @Schema(description = "올바른 status 값: ON, SOON, FULL, FINISH, CANCEL")
    private String status;
}

