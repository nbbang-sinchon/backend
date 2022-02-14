package nbbang.com.nbbang.global.dto;

import org.springframework.data.domain.PageRequest;

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
