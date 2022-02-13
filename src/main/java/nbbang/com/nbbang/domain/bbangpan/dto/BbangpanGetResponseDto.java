package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.controller.BbangpanController;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BbangpanGetResponseDto{
    private List<MemberBbangpanDto> memberBbangpanDtos;
    private Integer totalFoodPrice;
    private Integer fee;
    private Integer orderPrice;

    // 유저별 금액, 송금 상태, 유저 금액 합계, 배달비, 총 금액
}