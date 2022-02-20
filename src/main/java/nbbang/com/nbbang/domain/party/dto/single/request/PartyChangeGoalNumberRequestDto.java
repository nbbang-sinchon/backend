package nbbang.com.nbbang.domain.party.dto.single.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyChangeGoalNumberRequestDto {
    @Max(10) @Min(2) @NotNull
    private Integer goalNumber;
}
