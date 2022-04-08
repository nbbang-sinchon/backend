package nbbang.com.nbbang.domain.breadboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Account;

import javax.validation.constraints.NotBlank;

import static nbbang.com.nbbang.domain.breadboard.controller.BreadBoardResponseMessage.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadBoardAccountChangeRequestDto {
    @NotBlank(message =ILLEGAL_ARGUMENT_BANK)
    private String bank;

    @NotBlank(message =ILLEGAL_ARGUMENT_ACCOUNTNUMBER)
    private String accountNumber;

    public Account getAccount() {
        Account account = Account.createAccount(bank, accountNumber);
        return account;
    }
}