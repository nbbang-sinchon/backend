package nbbang.com.nbbang.domain.bbangpan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.concurrent.ConcurrentHashMap;

import static nbbang.com.nbbang.domain.bbangpan.controller.BbangpanResponseMessage.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BbangpanAccountChangeRequestDto {
    @NotBlank(message =ELLEGAL_ARGUMENT_BANK)
    private String bank;

    @NotBlank(message =ELLEGAL_ARGUMENT_ACCOUNTNUMBER)
    private String accountNumber;

    public ConcurrentHashMap getAccountMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.compute("bank", (key, value) ->bank);
        map.compute("accountNumber", (key, value) ->accountNumber);
        return map;
    }
}