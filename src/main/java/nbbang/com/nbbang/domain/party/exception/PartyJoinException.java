package nbbang.com.nbbang.domain.party.exception;

import org.springframework.validation.BindingResult;

/**
 * 파티 참여에 실패하였을 때에 대한 예외입니다.
 */
public class PartyJoinException extends IllegalArgumentException {
    public PartyJoinException(String message) {
        super(message);
    }
}