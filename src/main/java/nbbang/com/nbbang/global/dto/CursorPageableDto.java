package nbbang.com.nbbang.global.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class CursorPageableDto {
    private Integer pageNumber;
    private Integer pageSize;
    private Long cursorId;

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
