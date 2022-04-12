package nbbang.com.nbbang.domain.breadboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendStatusChangeRequestDto {
    @NotNull(message = "송금 상태는 필수 값입니다.")
    private Boolean isSent;
}