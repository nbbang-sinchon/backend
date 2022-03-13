package nbbang.com.nbbang.global.error;

public class GlobalErrorResponseMessage {

    public static final String ILLEGAL_ARGUMENT_ERROR = "올바르지 않은 요청 데이터입니다.";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String REQUEST_METHOD_ERROR = "정의되지 않은 요청입니다. HTTP 메소드를 확인해주세요.";
    public static final String REQUEST_URL_ERROR = "정의되지 않은 요청입니다. 요청 URL을 확인해주세요.";

    public static final String DB_ERROR = "데이터베이스 에러";

    public static final String ILLEGAL_TYPE_CONVERSION_ERROR = "값 타입 변환에 실패하였습니다. 올바른 값 타입인지 확인해 주세요. 예를 들어 party-id 는 Long 타입이어야 합니다.";

    public static final String NOT_OWNER_ERROR = "방장이 아닙니다.";
    public static final String NOT_PARTY_MEMBER_ERROR = "파티의 멤버가 아닙니다.";

    public static final String ILLEGAL_REQUEST_TYPE_ERROR = "올바르지 않은 요청입니다. 자료형 구조를 확인하세요.";

    public static final String UNAUTHORIZED_ERROR = "로그인이 필요합니다.";

    public static final String MAX_FILE_SIZE_ERROR = "허용된 파일 용량 (10 MB) 를 초과하였습니다.";
    public static final String BAD_MULTIPART_FILE_ERROR = "파일 변환에 실패하였습니다. 10MB 이하의 파일이고 올바른 확장자 (png, jpeg, jpg, jfif) 여야 합니다.";
}