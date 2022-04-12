package nbbang.com.nbbang.domain.breadboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBreadBoardDto {
    private String nickname;
    private Integer price;
    private String sendStatus;
}