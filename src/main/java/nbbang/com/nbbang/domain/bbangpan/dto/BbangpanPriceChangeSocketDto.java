package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseSenderDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BbangpanPriceChangeSocketDto {
    private Long changeMemberId;
    private Integer price;

    public static BbangpanPriceChangeSocketDto createDto(Long changeMemberId, Integer price) {
        return BbangpanPriceChangeSocketDto.builder()
                .changeMemberId(changeMemberId)
                .price(price)
                .build();
    }

}
