package nbbang.com.nbbang.domain.party.dto.single;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PartyChangeGoalNumberRequestDto {
    @Max(10) @Min(2) @NotNull
    private Integer goalNumber;
}
