package nbbang.com.nbbang.domain.bbangpan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.SendStatus;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendStatusChangeRequestDto {
    @NotNull(message = "송금 상태는 필수 값입니다.")
    private Boolean isSent;
}