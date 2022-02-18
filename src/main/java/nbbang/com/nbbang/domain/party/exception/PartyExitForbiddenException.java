package nbbang.com.nbbang.domain.party.exception;

/**
 * 파티 탈퇴에 실패하였을 때에 대한 예외입니다.
 */
public class PartyExitForbiddenException extends IllegalArgumentException {
    public PartyExitForbiddenException(String message) {
        super(message);
    }
}