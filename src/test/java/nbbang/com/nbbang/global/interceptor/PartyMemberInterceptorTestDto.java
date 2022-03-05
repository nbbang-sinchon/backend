package nbbang.com.nbbang.global.interceptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyMemberInterceptorTestDto {
    private Long ownerId;
    private Long partyMemberId;
    private Long justMemberId;
    private Long partyId;

    public void change(Long ownerId, Long partyMemberId, Long justMemberId,Long partyId){
        this.ownerId = ownerId;
        this.partyMemberId = partyMemberId;
        this.justMemberId = justMemberId;
        this.partyId = partyId;
    }
}
