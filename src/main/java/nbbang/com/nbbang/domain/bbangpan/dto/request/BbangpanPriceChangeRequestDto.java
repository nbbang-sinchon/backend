package nbbang.com.nbbang.domain.bbangpan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static nbbang.com.nbbang.domain.bbangpan.controller.BbangpanResponseMessage.ILLEGAL_ARGUMENT_PRICE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BbangpanPriceChangeRequestDto{

    @NotNull(message =ILLEGAL_ARGUMENT_PRICE )
    private Integer price;
}