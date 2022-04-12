package nbbang.com.nbbang.global.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

/**
 * abstract 를 사용한 이유
 * 1. 이 클래스 자체가 dto 로 쓰여선 안됩니다
 * 2. can do 의 의미보다 is a 의 의미가 더 맞는거 같아서 interface 보다 abstract 를 선택했어요
 */

@Data
public abstract class PageableDto {
    private Integer pageNumber;
    private Integer pageSize;

    public PageRequest createPageRequest() {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return PageRequest.of(pageNumber, pageSize);
    }
}
