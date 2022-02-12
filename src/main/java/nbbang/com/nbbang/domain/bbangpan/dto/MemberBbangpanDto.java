package nbbang.com.nbbang.domain.bbangpan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBbangpanDto{
    private String nickname;
    private Integer price;
    private String sendStatus;
}