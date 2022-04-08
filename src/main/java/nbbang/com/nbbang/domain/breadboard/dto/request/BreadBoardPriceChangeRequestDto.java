package nbbang.com.nbbang.domain.breadboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static nbbang.com.nbbang.domain.breadboard.controller.BreadBoardResponseMessage.ILLEGAL_ARGUMENT_PRICE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadBoardPriceChangeRequestDto {

    @NotNull(message =ILLEGAL_ARGUMENT_PRICE )
    private Integer price;
}