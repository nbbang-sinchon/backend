package nbbang.com.nbbang.domain.member.controller;

public class MemberResponseMessage {
    //member
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_MEMBER = "회원 정보 조회 성공";

    public static final String READ_MY_PARTY = "나의 파티 목록 조회 성공";

    public static final String NOT_FOUND_MEMBER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_MEMBER = "회원 가입 성공";
    public static final String UPDATE_MEMBER = "회원 정보 수정 성공";

    public static final String DELETE_MEMBER = "회원 탈퇴 성공";

    public static final String MEMBER_LOCATION_SUCCESS = "회원 위치 정보 조회 성공"; // 멤버 쪽으로

    public static final String ILLEGAL_MEMBER_UPDATE_REQUEST = "회원 정보 수정을 올바르게 입력하세요.";
    public static final String MEMBER_NOT_FOUND = "존재하지 않는 회원입니다.";

}
