package nbbang.com.nbbang.domain.chat.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChatChangeGoalNumberRequestDto {
    @Max(10) @Min(2) @NotNull
    private Integer goalNumber;
}
