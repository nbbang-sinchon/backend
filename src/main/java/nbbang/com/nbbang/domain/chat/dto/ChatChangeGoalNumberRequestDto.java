package nbbang.com.nbbang.domain.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChatChangeGoalNumberRequestDto {
    private Integer goalNumber;
}
