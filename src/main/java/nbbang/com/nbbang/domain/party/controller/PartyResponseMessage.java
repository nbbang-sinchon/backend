package nbbang.com.nbbang.domain.party.controller;

public class PartyResponseMessage {
    public static final String PARTY_FIND_SUCCESS = "파티 조회 성공";

    public static final String PARTY_CREATE_SUCCESS = "파티 생성 성공";

    public static final String PARTY_UPDATE_SUCCESS = "파티 수정 성공";
    public static final String PARTY_READ_SUCCESS = "파티 상세 조회 성공";
    public static final String PARTY_DELETE_SUCCESS = "파티 삭제 성공";

    public static final String PARTY_NOT_FOUND = "파티가 존재하지 않습니다.";
    public static final String HASHTAG_NOT_FOUND = "해시태그가 존재하지 않습니다.";

    // 파티 참여에 대한 메시지
    public static final String PARTY_JOIN_SUCCESS = "파티 참여 성공";
    public static final String PARTY_JOIN_ERROR = "파티에 참여할 수 없습니다.";
    public static final String PARTY_DUPLICATE_JOIN_ERROR = "이미 참여한 파티입니다.";
    public static final String PARTY_FULL_ERROR = "파티가 이미 찼습니다";
    public static final String PARTY_JOIN_BLOCKED_ERROR = "블락된 파티에 참여할 수 없습니다.";
    public static final String PARTY_JOIN_NONOPEN_ERROR = "STATUS 가 OPEN 인 파티만 참여할 수 있습니다.";


    // 파티 탈퇴에 대한 메시지
    public static final String PARTY_EXIT_SUCCESS = "파티 탈퇴 성공";
    public static final String PARTY_OWNER_EXIT_ERROR = "방장은 탈퇴할 수 없습니다.";

    // 파티 변경에 대한 메시지
    public static final String ILLEGAL_PARTY_STATUS = "파티 상태를 올바르게 입력하세요.";
    public static final String ILLEGAL_PARTY_GOAL_NUMBER = "파티 모집 회원 수를 올바르게 입력하세요.";
    public static final String HASHTAG_BLANK_ERROR = "해시태그는 빈 값이 허용되지 않습니다.";


    public static final String HASHTAG_NUMBER_ERROR = "해시태그는 최대 10개까지 가능합니다.";
    public static final String HASHTAG_DUPLICATE_ERROR = "해시태그는 중복이 허용되지 않습니다.";


    public static final String WISHLIST_DUPLICATE_ADD_ERROR = "이미 위시리스트에 추가한 파티입니다.";
    public static final String WISHLIST_NOT_FOUND = "위시리스트에 추가하지 않은 파티이거나 존재하지 않는 회원 또는 파티일 수 있습니다.";
    public static final String WISHLIST_ADD_SUCCESS = "위시리스트 추가 성공";
    public static final String WISHLIST_DELETE_SUCCESS = "위시리스트 삭제 성공";

}
