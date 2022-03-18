package nbbang.com.nbbang.global.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class PageableDto {
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
