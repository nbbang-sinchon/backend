package nbbang.com.nbbang.domain.bbangpan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static nbbang.com.nbbang.domain.bbangpan.controller.BbangpanResponseMessage.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BbangpanAccountChangeRequestDto {
    @NotBlank(message =ILLEGAL_ARGUMENT_BANK)
    private String bank;

    @NotBlank(message =ILLEGAL_ARGUMENT_ACCOUNTNUMBER)
    private String accountNumber;

    public Account getAccount() {
        Account account = Account.createAccount(bank, accountNumber);
        return account;
    }
}