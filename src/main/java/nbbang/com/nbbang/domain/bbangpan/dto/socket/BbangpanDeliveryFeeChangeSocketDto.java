package nbbang.com.nbbang.domain.bbangpan.dto.socket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BbangpanDeliveryFeeChangeSocketDto {
    private Integer deliveryFee;

    public static BbangpanDeliveryFeeChangeSocketDto createDto(Integer deliveryFee) {
        return BbangpanDeliveryFeeChangeSocketDto.builder().deliveryFee(deliveryFee).build();
    }
}
