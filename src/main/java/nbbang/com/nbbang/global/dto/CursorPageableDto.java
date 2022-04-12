package nbbang.com.nbbang.global.dto;

import lombok.Data;

@Data
public abstract class CursorPageableDto extends PageableDto {
    private Long cursorId;

}
