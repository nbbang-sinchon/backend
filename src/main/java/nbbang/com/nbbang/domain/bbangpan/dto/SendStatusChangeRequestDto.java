package nbbang.com.nbbang.domain.bbangpan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.SendStatus;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendStatusChangeRequestDto {
    @ValueOfEnum(enumClass = SendStatus.class) @Schema(description = "올바른 status 값: NONE, SEND, CHECK")
    private String sendStatus;

    public SendStatus createStatus() {
        System.out.println("sendStatus = " + sendStatus);
        return SendStatus.valueOf(sendStatus.toUpperCase(Locale.ROOT));
    }
}