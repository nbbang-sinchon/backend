package nbbang.com.nbbang.domain.party.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

import static nbbang.com.nbbang.domain.breadboard.controller.BreadBoardResponseMessage.*;

@Getter
@Builder
public class Account {
    private String bank;
    private String accountNumber;

    public static Account createAccount(@NotBlank(message = ILLEGAL_ARGUMENT_BANK) String bank, @NotBlank(message = ILLEGAL_ARGUMENT_ACCOUNTNUMBER) String accountNumber) {
        Account account = Account.builder().bank(bank).accountNumber(accountNumber).build();
        return account;
    }

    public void changeAccount(String bank, String accountNumber){
        this.bank = bank;
        this.accountNumber = accountNumber;
    }
}
